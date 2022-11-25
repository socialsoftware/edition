if (typeof window === 'object') {
  await import('./store');
  await import('@src/components/UserComponent');
}
let User;

const loadUser = async () => {
  User = await import('./userRouter.jsx');
};

export const userReferences = {
  manageUsers: '/user/manage-users',
  signin: '/user/signin',
  signup: '/user/signup',
  password: '/user/change-password',
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
