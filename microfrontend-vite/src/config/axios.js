import axios from 'axios';
import { getToken, setToken, setLoading, setError } from '../store';

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
    if (!response.data) {
      const error = Error("No data fetched")
      setLoading(false);
      setError(error);
      return Promise.reject(error);
    } 
    const { accessToken } = response.data;
    accessToken && setToken(accessToken);
    setLoading(false);
    return response;
  },
  (error) => {
    setLoading(false);
    setError(true);
    return Promise.reject(error);
  }
);

export default fetcher;
