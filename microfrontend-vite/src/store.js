import create from 'zustand';
import { persist } from 'zustand/middleware';
import { devtools } from 'zustand/middleware';

export const defaultHeaders = [
  { id: 'general_editor_prado', route: '/edition/acronym/JPC' },
  { id: 'general_editor_cunha', route: '/edition/acronym/TSC' },
  { id: 'general_editor_zenith', route: '/edition/acronym/RZ' },
  { id: 'general_editor_pizarro', route: '/edition/acronym/JP' },
  { className: 'divider' },
  { id: 'header_title', route: '/edition/acronym/LdoD-Arquivo' },
  { className: 'divider' },
];

export const useStore = create(
  devtools(
    persist(
      () => ({
        language: 'pt',
        token: undefined,
        user: undefined,
        loading: false,
        error: false,
        editionHeaders: defaultHeaders,
      }),
      {
        name: 'ldod-state-storage',
      }
    ),
    { name: 'store' }
  )
);

export const storeStateSelector = (sel) => useStore((state) => state[sel]);
export const state = () => useStore.getState();
export const getUser = () => state().user;
export const isAdmin = () =>
  getUser()?.roles.includes('ROLE_ADMIN') ? true : false;
export const getName = () =>
  getUser() && `${getUser().firstName} ${getUser().lastName}`;
export const getToken = () => state().token;
export const getLanguage = () => state().language;
export const isAuthenticated = () => getUser() ?? false;
export const isLoading = () => state().loading;
export const setLoading = (loading) => useStore.setState({ loading });
export const setError = (error) => useStore.setState({ error });
export const setLanguage = (language) => useStore.setState({ language });
export const setUser = (user) => useStore.setState({ user });
export const setToken = (token) => useStore.setState({ token });
export const removeUser = () => useStore.setState({ user: undefined });
export const removeToken = () => useStore.setState({ token: undefined });
export const setEditionHeaders = (headers) =>
  useStore.setState({ editionHeaders: headers });

export const userSelectedVE = () => useStore.getState()?.user?.selectedVE ?? [];
export const logout = () => {
  removeToken();
  removeUser();
};
