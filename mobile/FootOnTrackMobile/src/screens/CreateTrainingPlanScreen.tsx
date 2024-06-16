import React, { useState } from 'react';
import { View, Text, TextInput, Button, StyleSheet } from 'react-native';
import trainingApi from '../utils/trainingPlanApi.tsx';
import { useNavigation } from '@react-navigation/native';
import { StackNavigationProp } from '@react-navigation/stack';

type RootStackParamList = {
  TrainingPlanning: undefined;
};

type CreateTrainingPlanScreenNavigationProp = StackNavigationProp<RootStackParamList, 'TrainingPlanning'>;

const CreateTrainingPlanScreen: React.FC = () => {
  const [planName, setPlanName] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const navigation = useNavigation<CreateTrainingPlanScreenNavigationProp>();

  const createPlan = async () => {
    try {
      await trainingApi.post('/create', { name: planName, startDate, endDate });
      navigation.navigate('TrainingPlanning');
    } catch (error) {
      console.error('Error creating plan:', error);
    }
  };

  return (
    <View style={styles.container}>
      <TextInput
        style={styles.input}
        placeholder="Plan Name"
        value={planName}
        onChangeText={setPlanName}
      />
      <TextInput
        style={styles.input}
        placeholder="Start Date"
        value={startDate}
        onChangeText={setStartDate}
      />
      <TextInput
        style={styles.input}
        placeholder="End Date"
        value={endDate}
        onChangeText={setEndDate}
      />
      <Button title="Create Plan" onPress={createPlan} />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
  },
  input: {
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    marginBottom: 12,
    paddingHorizontal: 8,
  },
});

export default CreateTrainingPlanScreen;
