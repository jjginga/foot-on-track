import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import analysisApi from '../utils/analysisApi';
import AsyncStorage from '@react-native-async-storage/async-storage';

interface AnalysisResult {
  time5km: number;
  time10km: number;
  timeHalfMarathon: number;
  timeMarathon: number;
}

const PerformanceAnalysisScreen = () => {
  const [performanceData, setPerformanceData] = useState<AnalysisResult | null>(null);

  useEffect(() => {
    const fetchPerformance = async () => {
      try {
        const userId = await AsyncStorage.getItem('userId');
        if (!userId) {
          console.error('User ID not found in AsyncStorage');
          return;
        }

        const response = await analysisApi.get(`analysis/performance/${userId}`);
        console.log('API response:', response.data);
        setPerformanceData(response.data);
      } catch (error) {
        console.error('Error fetching performance data:', error);
      }
    };

    fetchPerformance();
  }, []);

  const convertMinutesToHHMMSS = (minutes: number): string => {
    const h = Math.floor(minutes / 60);
    const m = Math.floor(minutes % 60);
    const s = Math.floor((minutes * 60) % 60);
    return `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
  };



  return (
    <View style={styles.container}>
      {performanceData ? (
        <View>
          <Text style={styles.textBlue}>5km Prediction: {convertMinutesToHHMMSS(performanceData.time5km)} </Text>
          <Text style={styles.textBlue}>10km Prediction: {convertMinutesToHHMMSS(performanceData.time10km)} </Text>
          <Text style={styles.textBlue}>Half Marathon Prediction: {convertMinutesToHHMMSS(performanceData.timeHalfMarathon)} </Text>
          <Text style={styles.textBlue}>Marathon Prediction: {convertMinutesToHHMMSS(performanceData.timeMarathon)} </Text>
        </View>
      ) : (
        <Text style={styles.textBlack}>Loading performance data...</Text>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  textBlack: {
    fontSize: 18,
    marginBottom: 8,
    color: 'black',
  },
  textBlue: {
    fontSize: 18,
    marginBottom: 8,
    color: 'blue',
  },
});


export default PerformanceAnalysisScreen;
