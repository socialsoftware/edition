import axios from 'axios';
import { getToken, setToken, setLoading } from '../store';

const fetcher = axios.create({
  baseURL: import.meta.env.VITE_URL,
});

fetcher.interceptors.request.use((config) => {
  const token = getToken();
  config.headers.Authorization = token ? `Bearer ${token}` : '';
  setLoading(true);
  return config;
});

fetcher.interceptors.response.use(
  async (response) => {
    const { accessToken } = response.data;
    accessToken && setToken(accessToken);
    setLoading(false);
    return response;
  },
  (error) => {
    setLoading(false);
    return Promise.reject(error);
  }
);

export default fetcher;
