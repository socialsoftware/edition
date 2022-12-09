import { userReferences } from './user-references';
let User;

if (typeof window !== 'undefined') {
  await import('./events-modules');
  await import('./store');
  await import('./bootstrap');
}

const loadUser = async () => {
  if (User) return;
  User = await import('./user-router.jsx');
};

export default {
  path: '/user',
  references: userReferences,
  bootstrap: () => import('./components/UserComponent'),
  mount: async (lang, ref) => {
    await loadUser();
    await User.mount(lang, ref);
  },
  unMount: async () => {
    await loadUser();
    await User.unMount();
  },
};
