import React, { useEffect, useState } from 'react';
import { View, Text, FlatList, StyleSheet, TouchableOpacity, Animated } from "react-native";
import { StackNavigationProp } from '@react-navigation/stack';
import { RouteProp } from '@react-navigation/native';
import { format } from 'date-fns';
import analysisApi from '../utils/analysisApi';
import AsyncStorage from '@react-native-async-storage/async-storage';
import ScrollView = Animated.ScrollView;

type RootStackParamList = {
  History: undefined;
  Details: { sessionId: string };
};

type HistoryScreenNavigationProp = StackNavigationProp<RootStackParamList, 'History'>;

type HistoryScreenRouteProp = RouteProp<RootStackParamList, 'History'>;

type Props = {
  navigation: HistoryScreenNavigationProp;
  route: HistoryScreenRouteProp;
};

interface RunningSession {
  id: string;
  startTime: string;
  endTime: string;
  distance: number;
  duration: number;
}


interface CurrentAnalysisResult {
  time: number;
  distance: number;
}

const HistoryScreen = () => {
  const [runningSessions, setRunningSessions] = useState<RunningSession[]>([]);

  useEffect(() => {
    const fetchRunningSessions = async () => {
      try {
        const userId = await AsyncStorage.getItem('userId');
        if (!userId) return;

        const response = await analysisApi.get(`analysis/history/${userId}`);

        const sessions = response.data;

        const sessionsWithAnalysis = await Promise.all(sessions.map(async (session: RunningSession) => {
          const analysisResponse = await analysisApi.get(`/analysis/session/${session.id}`);
          const analysis: CurrentAnalysisResult = analysisResponse.data;
          return {
            ...session,
            distance: analysis.distance.toFixed(2),
            duration: analysis.time,
          };
        }));

        const sortedSessions = sessionsWithAnalysis.sort(
          (a: RunningSession, b: RunningSession) =>
            new Date(b.startTime).getTime() - new Date(a.startTime).getTime()
        );
        setRunningSessions(sortedSessions);
      } catch (error) {
        console.error('Error fetching running sessions:', error);
      }
    };

    fetchRunningSessions();
  }, []);

  const convertMinutesToHHMMSS = (minutes: number): string => {
    const h = Math.floor(minutes / 60);
    const m = Math.floor(minutes % 60);
    const s = Math.floor((minutes * 60) % 60);
    return `${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
  };

  const calculatePace = (duration: number, distance: number): string => {
    const pace = duration / distance;
    const m = Math.floor(pace);
    const s = Math.floor((pace - m) * 60);
    return `${m}:${s.toString().padStart(2, '0')} min/km`;
  };

  return (
    <ScrollView style={styles.container}>
      {runningSessions.map((session) => (
        <View key={session.id} style={styles.sessionContainer}>
          <Text style={styles.text}>Date: {format(new Date(session.startTime), 'dd/MM/yyyy')}</Text>
          <Text style={styles.text}>Distance: {session.distance} km</Text>
          <Text style={styles.text}>Duration: {convertMinutesToHHMMSS(session.duration)}</Text>
          <Text style={styles.text}>Pace: {calculatePace(session.duration, session.distance)}</Text>
        </View>
      ))}
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF',
    padding: 16,
  },
  sessionContainer: {
    backgroundColor: '#0000FF',
    padding: 16,
    marginBottom: 16,
    borderRadius: 8,
  },
  text: {
    color: '#FFFFFF',
    fontSize: 18,
    marginBottom: 8,
  },
});


export default HistoryScreen;

