import React, { useEffect, useState } from 'react';
import { View, Text, FlatList, Button, StyleSheet, TouchableOpacity, Alert } from 'react-native';
import { useNavigation, NavigationProp } from '@react-navigation/native';
import trainingApi from '../utils/trainingPlanApi';

type RootStackParamList = {
  TrainingDetails: { planId: string };
  CreateTrainingPlan: undefined;
  TrainingPlanning: undefined;
};

type TrainingPlanningScreenNavigationProp = NavigationProp<RootStackParamList, 'TrainingPlanning'>;

const TrainingPlanningScreen: React.FC = () => {
  const [plans, setPlans] = useState<any[]>([]);
  const navigation = useNavigation<TrainingPlanningScreenNavigationProp>();

  useEffect(() => {
    const fetchTrainingPlans = async () => {
      try {
        const response = await trainingApi.get('/get-all');
        setPlans(response.data);
      } catch (error) {
        console.error('Error fetching training plans:', error);
      }
    };

    fetchTrainingPlans();
  }, []);

  const handleDeletePlan = async (id: string) => {
    try {
      await trainingApi.delete(`/${id}`);
      setPlans(plans.filter(plan => plan.id !== id));
      Alert.alert('Success', 'Training plan deleted successfully');
    } catch (error) {
      console.error('Error deleting training plan:', error);
      Alert.alert('Error', 'Could not delete training plan');
    }
  };

  const renderItem = ({ item }: { item: any }) => (
    <TouchableOpacity
      style={styles.item}
      onPress={() => navigation.navigate('TrainingDetails', { planId: item.id })}
    >
      <Text style={styles.title}>{item.name}</Text>
      <Text style={styles.details}>Start Date: {item.startDate}</Text>
      <Text style={styles.details}>End Date: {item.endDate}</Text>
      <Button title="Delete" onPress={() => handleDeletePlan(item.id)} />
    </TouchableOpacity>
  );

  return (
    <View style={styles.container}>
      <Button
        title="Create New Training Plan"
        onPress={() => navigation.navigate('CreateTrainingPlan')}
      />
      <FlatList
        data={plans}
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

export default TrainingPlanningScreen;
