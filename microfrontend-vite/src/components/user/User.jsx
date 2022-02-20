import { Route, Routes } from 'react-router-dom';
import { lazy } from 'react';

const Signin = lazy(() => import('./Signin'));
const SignUp = lazy(() => import('./SignUp'));
const ChangePassword = lazy(() => import('./ChangePassword'));

export default () => {
  return (
    <Routes>
      <Route path="signin" element={<Signin />} />
      <Route path="change-password" element={<ChangePassword />} />
      <Route path="signup" element={<SignUp />} />
    </Routes>
  );
};
