import React, { useState, useEffect } from 'react';
import { View, Button, StyleSheet, Text, Modal } from 'react-native';
import Geolocation from '@react-native-community/geolocation';
import MapView, { Marker } from 'react-native-maps';
import trackingApi from '../utils/trackingApi';
import analysisApi from '../utils/analysisApi';
import AsyncStorage from "@react-native-async-storage/async-storage";

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
        { enableHighAccuracy: true, distanceFilter: 1, interval: 1000 }
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
      const userId = await AsyncStorage.getItem('userId');
      const currentTime = new Date().toISOString();

      const response = await trackingApi.post('/running-sessions/start', {
        userId: userId,
        time: currentTime
      });

      setSessionId(response.data.id);
      setIsRunning(true);
    } catch (error) {
      console.error('Error starting run:', error);
    }
  };

  const stopRun = async () => {
    try {
      if (sessionId) {
        const currentTime = new Date().toISOString();

        await trackingApi.post(`/running-sessions/${sessionId}/stop`, {
          time: currentTime
        });

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
        <View style={styles.buttonWrapper}>
          <Button title="Start Run" onPress={startRun} disabled={isRunning} />
        </View>
        <View style={styles.buttonWrapper}>
          <Button title="Pause Run" onPress={pauseRun} disabled={!isRunning || isPaused} />
        </View>
        <View style={styles.separator} />
        <View style={styles.buttonWrapper}>
          <Button title="Stop Run" onPress={stopRun} disabled={!isRunning} />
        </View>
        <View style={styles.buttonWrapper}>
          <Button title="Resume Run" onPress={resumeRun} disabled={!isPaused} />
        </View>

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
    backgroundColor: '#0000FF',
    padding: 16,
  },
  map: {
    flex: 1,
  },
  separator: {
    width: '100%',
    height: 1,
    backgroundColor: '#FFFFFF',
    marginVertical: 8,
  },
  buttonContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-around',
    padding: 16,
  },
  buttonWrapper: {
    width: '45%',
    marginBottom: 16,
  },
  modalView: {
    margin: 20,
    backgroundColor: '#0000FF',
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
  text: {
    color: '#FFFFFF',
    fontSize: 18,
    marginBottom: 8,
  },
});

export default StartRunScreen;
