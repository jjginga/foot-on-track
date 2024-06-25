import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { API_BASE_URL } from '@env';

const analysisApi = axios.create({
  baseURL: `${API_BASE_URL}:8092`,
});

analysisApi.interceptors.request.use(
  async (config) => {
    const token = await AsyncStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;

    }
    config.headers.Accept = '*/*';
    console.log(config)
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default analysisApi;
