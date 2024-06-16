import React, { useEffect, useState } from 'react';
import { View, Text, FlatList, StyleSheet, TouchableOpacity } from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RouteProp } from '@react-navigation/native';
import analysisApi from '../utils/analysisApi';
import AsyncStorage from '@react-native-async-storage/async-storage';

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
  date: string;
  distance?: number;
  duration?: number;
}

interface CurrentAnalysisResult {
  time: number;
  distance: number;
}

const HistoryScreen: React.FC<Props> = ({ navigation }) => {
  const [sessions, setSessions] = useState<RunningSession[]>([]);

  useEffect(() => {
    const fetchHistory = async () => {
      try {
        const userId = await AsyncStorage.getItem('userId');
        if (!userId) return;

        const response = await analysisApi.get(`/history/${userId}`);
        const sessionData: RunningSession[] = await Promise.all(
          response.data.map(async (session: RunningSession) => {
            const analysisResponse = await analysisApi.get(`/session/${session.id}`);
            const analysis: CurrentAnalysisResult = analysisResponse.data;
            return {
              ...session,
              distance: analysis.distance,
              duration: analysis.time,
            };
          })
        );

        setSessions(sessionData);
      } catch (error) {
        console.error('Error fetching history:', error);
      }
    };

    fetchHistory();
  }, []);

  const renderItem = ({ item }: { item: RunningSession }) => (
    <TouchableOpacity
      style={styles.item}
      onPress={() => navigation.navigate('Details', { sessionId: item.id })}
    >
      <Text style={styles.title}>{item.date}</Text>
      <Text style={styles.details}>Distance: {item.distance} km</Text>
      <Text style={styles.details}>
        Pace: {(item.duration && item.distance) ? (item.duration / 60 / item.distance).toFixed(2) : 'N/A'} min/km
      </Text>
    </TouchableOpacity>
  );

  return (
    <View style={styles.container}>
      <FlatList
        data={sessions}
        renderItem={renderItem}
        keyExtractor={(item) => item.id}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
  },
  item: {
    padding: 16,
    marginVertical: 8,
    backgroundColor: '#f9c2ff',
    borderRadius: 8,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
  },
  details: {
    fontSize: 16,
  },
});

export default HistoryScreen;

