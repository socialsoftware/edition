import fetcher from '../../../config/axios';
import { setUser } from '../../../store';

const AUTH_BASE_URL = '/auth';


export const getUser = async () => {
  const { data } = await fetcher.get('/user');
  Object.keys(data).includes('roles') && setUser(data);
};

export const authenticate = async (data) => 
  await fetcher.post(`${AUTH_BASE_URL}/signin`, data);

export const changePassword = async (data) =>
  await fetcher.post(`${AUTH_BASE_URL}/changePassword`, data);

export const signup = async (data) =>
  await fetcher.post(`${AUTH_BASE_URL}/signup`, data);
