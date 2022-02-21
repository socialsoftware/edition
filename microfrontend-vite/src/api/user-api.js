import fetcher from './axios';
import { setUser } from '../store';

export const getUser = async () => {
  const { data } = await fetcher.get('/user');
  Object.keys(data).includes('roles') && setUser(data);
};

export const authenticate = async (data) => 
  await fetcher.post('/auth/signin', data);

export const changePassword = async (data) =>
  await fetcher.post('/auth/changePassword', data);

export const signup = async (data) =>
  await fetcher.post('/auth/signup', data);
