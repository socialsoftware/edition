if (typeof window === 'object') {
  await import('./store.js');
  await import('@src/components/UserComponent');
}

let User;
const loadUser = async () => {
  User = await import('./userRouter.jsx');
};

export default {
  path: '/user',
  mount: async (lang, ref) => {
    if (!User) await loadUser();
    await User.mount(lang, ref);
  },
  unMount: async () => {
    if (!User) await loadUser();
    await User.unMount();
  },
};
