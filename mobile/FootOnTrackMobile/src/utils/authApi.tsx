import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { API_BASE_URL } from '@env';

const authApi = axios.create({
  baseURL: `${API_BASE_URL}:8090`,
});
authApi.interceptors.request.use(request => {
  console.log('Starting Request', JSON.stringify(request, null, 2));
  return request;
});
authApi.interceptors.request.use(
  async (config) => {
    const token = await AsyncStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

authApi.interceptors.request.use(request => {
  console.log('Starting Request', JSON.stringify(request, null, 2));
  return request;
});

authApi.interceptors.response.use(response => {
  console.log('Response:', JSON.stringify(response, null, 2));
  return response;
}, error => {
  console.log('Error Response:', JSON.stringify(error, null, 2));
  return Promise.reject(error);
});

export default authApi;
