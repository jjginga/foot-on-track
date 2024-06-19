import React from 'react';
import { View, Button, StyleSheet } from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RouteProp } from '@react-navigation/native';

type RootStackParamList = {
  Home: undefined;
  StartRun: undefined;
  History: undefined;
  PerformanceAnalysis: undefined;
  TrainingPlan: undefined;
};

type HomeScreenNavigationProp = StackNavigationProp<
  RootStackParamList,
  'Home'
>;

type HomeScreenRouteProp = RouteProp<RootStackParamList, 'Home'>;

type Props = {
  navigation: HomeScreenNavigationProp;
  route: HomeScreenRouteProp;
};

const HomeScreen: React.FC<Props> = ({ navigation }) => {
  return (
    <View style={styles.container}>
      <Button
        title="Start Run"
        onPress={() => navigation.navigate('StartRun')}
      />
      <Button
        title="Run History"
        onPress={() => navigation.navigate('History')}
      />
      <Button
        title="Performance Analysis"
        onPress={() => navigation.navigate('PerformanceAnalysis')}
      />
      <Button
        title="Plan Workouts"
        onPress={() => navigation.navigate('TrainingPlan')}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    paddingHorizontal: 16,
  },
  button: {
    marginBottom: 12,
  },
});

export default HomeScreen;
