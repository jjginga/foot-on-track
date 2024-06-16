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
        if (!userId) return;

        const response = await analysisApi.get(`/performance/${userId}`);
        setPerformanceData(response.data);
      } catch (error) {
        console.error('Error fetching performance data:', error);
      }
    };

    fetchPerformance();
  }, []);

  return (
    <View style={styles.container}>
      {performanceData ? (
        <View>
          <Text style={styles.text}>5km Prediction: {performanceData.time5km} min</Text>
          <Text style={styles.text}>10km Prediction: {performanceData.time10km} min</Text>
          <Text style={styles.text}>Half Marathon Prediction: {performanceData.timeHalfMarathon} min</Text>
          <Text style={styles.text}>Marathon Prediction: {performanceData.timeMarathon} min</Text>
        </View>
      ) : (
        <Text>Loading performance data...</Text>
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
  text: {
    fontSize: 18,
    marginBottom: 8,
  },
});

export default PerformanceAnalysisScreen;
