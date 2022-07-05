import { Route, Routes } from 'react-router-dom';
import { lazy } from 'react';
import messages from './resources/constants/messages';

const Signin = lazy(() => import('./pages/Signin'));
const SignUp = lazy(() => import('./pages/SignUp'));
const ChangePassword = lazy(() => import('./pages/ChangePassword'));

export default () => (
  <div className="container text-center">
    <Routes>
      <Route path="signin" element={<Signin messages={messages} />} />
      <Route
        path="change-password"
        element={<ChangePassword messages={messages} />}
      />
      <Route path="signup" element={<SignUp messages={messages} />} />
    </Routes>
  </div>
);
