import axios from 'axios';
import { getToken, setToken } from '../store';

const fetcher = axios.create({
    baseURL: import.meta.env.VITE_URL,
});

fetcher.interceptors.request.use((config) => {
    const token = getToken();
    config.headers.Authorization = token ? `Bearer ${token}` : '';
    return config;
});

fetcher.interceptors.response.use(
    async (response) => {
        const { accessToken } = response.data;
        accessToken && setToken(accessToken);
        return response;
    },
    (error) => Promise.reject(error)
);

export default fetcher;