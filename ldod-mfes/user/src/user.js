await import('./store.js').catch((e) => console.error(e));
await import('@src/components/UserComponent.jsx').catch((e) =>
  console.error(e)
);
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
