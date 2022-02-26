import create from 'zustand';
import { persist } from 'zustand/middleware';

export const useStore = create(
    persist(
        (set, get) => ({
            language: 'pt',
            token: undefined,
            user: undefined,
            loading: false,
        }), {
            name: 'ldod-state-storage',
        }
    )
);

export const state = () => useStore.getState();
export const getUser = () => state().user;
export const isAdmin = () => getUser() && getUser().roles.includes('ROLE_ADMIN') ? true : false;  
export const getName = () => getUser() && `${getUser().firstName} ${getUser().lastName}`;
export const getToken = () => state().token;
export const getLanguage = () => state().language;
export const isAuthenticated = () => getUser() ? true : false;
export const isLoading = () => state().loading;
export const setLoading = (bool) => useStore.setState({loading: bool});
export const setLanguage = (language) => useStore.setState({ language });
export const setUser = (user) => useStore.setState({ user });
export const setToken = (token) => useStore.setState({ token });
export const removeUser = () => useStore.setState({ user: undefined });
export const removeToken = () => useStore.setState({ token: undefined });

export const logout = () => {
    removeToken();
    removeUser();
};