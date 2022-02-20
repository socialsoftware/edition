import fetcher from './axios';
import { setUser } from '../store';
import axios from 'axios';

export const getUser = async () => {
  const { data } = await fetcher.get(`${import.meta.env.VITE_URL}/user`);
  Object.keys(data).includes('roles') && setUser(data);
};

export const authenticate = async (data) => 
  await fetcher.post(`${import.meta.env.VITE_URL}/auth/signin`, data);

export const changePassword = async (data) =>
  await fetcher.post(`${import.meta.env.VITE_URL}/auth/changePassword`, data);

export const signup = async (data) =>
  await axios.post(`${import.meta.env.VITE_URL}/auth/signup`, data);
