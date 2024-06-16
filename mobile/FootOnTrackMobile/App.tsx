/**
 * Foot on track mobile app
 * This is the main file of the app
 * It contains the navigation stack for the app
 */

import * as React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';

type RootStackParamList = {
  Login: undefined;
  Register: undefined;
  Home: undefined;
  StartRun: undefined;
  History: undefined;
  PerformanceAnalysis: undefined;
  TrainingPlan: undefined;
};

const Stack = createStackNavigator<RootStackParamList>();

import LoginScreen from './src/screens/LoginScreen';
import RegisterScreen from './src/screens/RegisterScreen';
import HomeScreen from './src/screens/HomeScreen';
import StartRunScreen from './src/screens/StartRunScreen';
import HistoryScreen from './src/screens/HistoryScreen';
import PerformanceAnalysisScreen from './src/screens/PerformanceAnalysisScreen';
import TrainingPlanScreen from './src/screens/TrainingPlanScreen';


function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="Login">
        <Stack.Screen name="Login" component={LoginScreen} />
        <Stack.Screen name="Register" component={RegisterScreen} />
        <Stack.Screen name="Home" component={HomeScreen} />
        <Stack.Screen name="StartRun" component={StartRunScreen} />
        <Stack.Screen name="History" component={HistoryScreen} />
        <Stack.Screen name="PerformanceAnalysis" component={PerformanceAnalysisScreen} />
        <Stack.Screen name="TrainingPlan" component={TrainingPlanScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default App;
