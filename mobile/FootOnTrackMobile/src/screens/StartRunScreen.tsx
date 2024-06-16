import React, { useState, useEffect } from 'react';
import { View, Button, StyleSheet, Text, Modal } from 'react-native';
import Geolocation from '@react-native-community/geolocation';
import MapView, { Marker } from 'react-native-maps';
import trackingApi from '../utils/trackingApi';
import analysisApi from '../utils/analysisApi';

interface SessionAnalysis {
  time: number;
  distance: number;
}

const StartRunScreen = () => {
  const [position, setPosition] = useState({
    latitude: 0,
    longitude: 0,
    latitudeDelta: 0.01,
    longitudeDelta: 0.01,
  });
  const [sessionId, setSessionId] = useState(null);
  const [isRunning, setIsRunning] = useState(false);
  const [isPaused, setIsPaused] = useState(false);
  const [sessionAnalysis, setSessionAnalysis] = useState<SessionAnalysis | null>(null);
  const [modalVisible, setModalVisible] = useState(false);

  useEffect(() => {
    let watchId: number | null = null;
    let positionInterval: NodeJS.Timeout;
    let sendInterval: NodeJS.Timeout;

    if (isRunning && !isPaused) {
      watchId = Geolocation.watchPosition(
        async (position) => {
          const { latitude, longitude } = position.coords;
          setPosition((prevState) => ({
            ...prevState,
            latitude,
            longitude,
          }));

          if (sessionId) {
            try {
              await trackingApi.post(`/running-sessions/${sessionId}/update`, {
                latitude,
                longitude,
              });
            } catch (error) {
              console.error('Error updating location:', error);
            }
          }
        },
        (error) => console.error(error),
        { enableHighAccuracy: true, distanceFilter: 1, interval: 1000 } // Coleta de dados a cada segundo
      );

      positionInterval = setInterval(async () => {
        Geolocation.getCurrentPosition(
          async (position) => {
            const { latitude, longitude } = position.coords;
            setPosition((prevState) => ({
              ...prevState,
              latitude,
              longitude,
            }));

            if (sessionId) {
              try {
                await trackingApi.post(`/running-sessions/${sessionId}/update`, {
                  latitude,
                  longitude,
                });
              } catch (error) {
                console.error('Error updating location:', error);
              }
            }
          },
          (error) => console.error(error),
          { enableHighAccuracy: true }
        );
      }, 1000);

      sendInterval = setInterval(async () => {
        const { latitude, longitude } = position;
        if (sessionId) {
          try {
            await trackingApi.post(`/running-sessions/${sessionId}/update`, {
              latitude,
              longitude,
            });
          } catch (error) {
            console.error('Error sending location:', error);
          }
        }
      }, 30000);
    } else if (watchId) {
      Geolocation.clearWatch(watchId);
    }

    return () => {
      if (watchId) {
        Geolocation.clearWatch(watchId);
      }
      if (positionInterval) {
        clearInterval(positionInterval);
      }
      if (sendInterval) {
        clearInterval(sendInterval);
      }
    };
  }, [isRunning, isPaused, sessionId, position]);

  const startRun = async () => {
    try {
      const response = await trackingApi.post('/running-sessions/start');
      setSessionId(response.data.id);
      setIsRunning(true);
    } catch (error) {
      console.error('Error starting run:', error);
    }
  };

  const stopRun = async () => {
    try {
      if (sessionId) {
        await trackingApi.post(`/running-sessions/${sessionId}/stop`);
        const analysisResponse = await analysisApi.get(`/session/${sessionId}`);
        setSessionAnalysis(analysisResponse.data);
        setIsRunning(false);
        setIsPaused(false);
        setSessionId(null);
        setModalVisible(true);
      }
    } catch (error) {
      console.error('Error stopping run:', error);
    }
  };

  const pauseRun = async () => {
    try {
      if (sessionId) {
        await trackingApi.post(`/running-sessions/${sessionId}/pause`);
      }
      setIsPaused(true);
    } catch (error) {
      console.error('Error pausing run:', error);
    }
  };

  const resumeRun = async () => {
    try {
      if (sessionId) {
        await trackingApi.post(`/running-sessions/${sessionId}/resume`);
      }
      setIsPaused(false);
    } catch (error) {
      console.error('Error resuming run:', error);
    }
  };

  return (
    <View style={styles.container}>
      <MapView style={styles.map} region={position}>
        <Marker coordinate={position} />
      </MapView>
      <View style={styles.buttonContainer}>
        <Button title="Start Run" onPress={startRun} disabled={isRunning} />
        <Button title="Pause Run" onPress={pauseRun} disabled={!isRunning || isPaused} />
        <Button title="Resume Run" onPress={resumeRun} disabled={!isPaused} />
        <Button title="Stop Run" onPress={stopRun} disabled={!isRunning} />
      </View>
      {isRunning && <Text>Running Session ID: {sessionId}</Text>}

      <Modal visible={modalVisible} animationType="slide" transparent={true}>
        <View style={styles.modalView}>
          {sessionAnalysis && (
            <>
              <Text>Time: {sessionAnalysis.time} seconds</Text>
              <Text>Distance: {sessionAnalysis.distance} meters</Text>
              <Text>Average Speed: {(1000 / (sessionAnalysis.distance / sessionAnalysis.time) / 60).toFixed(2)} min/km</Text>
              <Button title="Close" onPress={() => setModalVisible(false)} />
            </>
          )}
        </View>
      </Modal>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  map: {
    flex: 1,
  },
  buttonContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    padding: 16,
  },
  modalView: {
    margin: 20,
    backgroundColor: 'white',
    borderRadius: 20,
    padding: 35,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 5,
  },
});

export default StartRunScreen;
