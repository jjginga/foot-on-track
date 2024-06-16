import React, { useEffect, useState } from 'react';
import { View, Text, FlatList, Button, StyleSheet, TextInput } from 'react-native';
import { RouteProp, useNavigation } from '@react-navigation/native';
import trainingApi from '../utils/trainingPlanApi';
import { StackNavigationProp } from '@react-navigation/stack';

type RootStackParamList = {
  TrainingDetails: { planId: string };
};

type TrainingDetailsScreenNavigationProp = StackNavigationProp<RootStackParamList, 'TrainingDetails'>;
type TrainingDetailsScreenRouteProp = RouteProp<RootStackParamList, 'TrainingDetails'>;

type Props = {
  route: TrainingDetailsScreenRouteProp;
};

interface TrainingSession {
  id: string;
  name: string;
}

interface TrainingPlan {
  id: string;
  name: string;
  sessions: TrainingSession[];
}

const TrainingDetailsScreen: React.FC<Props> = ({ route }) => {
  const { planId } = route.params;
  const [plan, setPlan] = useState<TrainingPlan | null>(null);
  const [newSession, setNewSession] = useState('');
  const navigation = useNavigation<TrainingDetailsScreenNavigationProp>();

  useEffect(() => {
    const fetchPlanDetails = async () => {
      try {
        const response = await trainingApi.get(`/${planId}`);
        setPlan(response.data);
      } catch (error) {
        console.error('Error fetching plan details:', error);
      }
    };

    fetchPlanDetails();
  }, [planId]);

  const addSession = async () => {
    try {
      const response = await trainingApi.post('/training-sessions', {
        planId,
        name: newSession,
      });
      setPlan((prevPlan) => prevPlan ? ({
        ...prevPlan,
        sessions: [...prevPlan.sessions, response.data],
      }) : null);
      setNewSession('');
    } catch (error) {
      console.error('Error adding session:', error);
    }
  };

  const deleteSession = async (sessionId: string) => {
    try {
      await trainingApi.delete(`/training-sessions/${sessionId}`);
      setPlan((prevPlan) => prevPlan ? ({
        ...prevPlan,
        sessions: prevPlan.sessions.filter((session) => session.id !== sessionId),
      }) : null);
    } catch (error) {
      console.error('Error deleting session:', error);
    }
  };

  if (!plan) {
    return <Text>Loading...</Text>;
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>{plan.name}</Text>
      <FlatList
        data={plan.sessions}
        renderItem={({ item }) => (
          <View style={styles.item}>
            <Text>{item.name}</Text>
            <Button title="Delete" onPress={() => deleteSession(item.id)} />
          </View>
        )}
        keyExtractor={(item) => item.id}
      />
      <TextInput
        style={styles.input}
        placeholder="New session"
        value={newSession}
        onChangeText={setNewSession}
      />
      <Button title="Add Session" onPress={addSession} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 16,
  },
  item: {
    padding: 16,
    marginVertical: 8,
    backgroundColor: '#f9c2ff',
    borderRadius: 8,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  input: {
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    marginBottom: 12,
    paddingHorizontal: 8,
  },
});

export default TrainingDetailsScreen;
