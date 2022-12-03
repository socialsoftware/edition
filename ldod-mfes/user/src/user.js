export let userReferences;
if (typeof window === 'object') {
  await import('./store');
  await import('./bootstrap');
  await import('@src/components/UserComponent');
  userReferences = (await import('./userReferences')).userReferences;
}
let User;

const loadUser = async () => {
  if (User) return;
  User = await import('./userRouter.jsx');
};

export default {
  path: '/user',
  mount: async (lang, ref) => {
    await loadUser();
    await User.mount(lang, ref);
  },
  unMount: async () => {
    await loadUser();
    await User.unMount();
  },
};
