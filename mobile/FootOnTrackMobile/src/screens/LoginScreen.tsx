import React, { useState } from 'react';
import { View, TextInput, Button, StyleSheet } from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RouteProp } from '@react-navigation/native';
import authApi from '../utils/authApi';
import styles from '../styles/styles';
import AsyncStorage from "@react-native-async-storage/async-storage";

type RootStackParamList = {
  Login: undefined;
  Register: undefined;
  Home: undefined;
};

type LoginScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Login'>;

type LoginScreenRouteProp = RouteProp<RootStackParamList, 'Login'>;

type Props = {
  navigation: LoginScreenNavigationProp;
  route: LoginScreenRouteProp;
};

const LoginScreen: React.FC<Props> = ({ navigation }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async () => {
    try {
      const response = await authApi.post('/auth/login',
        {
          username,
          password,
        },
      );
      if (response.status === 200) {
        const { token } = response.data;
        await AsyncStorage.setItem('authToken', token);
        console.log('Token saved, navigating to Home');
        navigation.navigate('Home');
      } else {
        console.error('Login failed');
      }
    } catch (error) {
      console.error('An error occurred during login', error);
    }
  };

  return (
    <View style={styles.container}>
      <TextInput
        style={styles.input}
        placeholderTextColor="blue"
        placeholder="Username"
        value={username}
        onChangeText={setUsername}
      />
      <TextInput
        style={styles.input}
        placeholderTextColor="blue"
        placeholder="Password"
        value={password}
        onChangeText={setPassword}
        secureTextEntry
      />
      <Button title="Login" onPress={handleLogin} />
      <Button title="Register" onPress={() => navigation.navigate('Register')} />
    </View>
  );
};

export default LoginScreen;
