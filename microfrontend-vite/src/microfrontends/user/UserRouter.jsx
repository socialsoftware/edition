import { Route, Routes } from 'react-router-dom';
import { lazy, useState, useEffect } from 'react';
import { getLanguage } from '../../store';

const Signin = lazy(() => import('./pages/Signin'));
const SignUp = lazy(() => import('./pages/SignUp'));
const ChangePassword = lazy(() => import('./pages/ChangePassword'));
const getMessages = () =>
  import(`./resources/constants/messages-${getLanguage()}.js`);

export default () => {
  const [messages, setMessages] = useState();
  useEffect(() => {
    getMessages().then(({ messages }) => setMessages(messages));
  }, [getLanguage()]);

  return (
    <Routes>
      <Route path="signin" element={<Signin messages={messages}/>} />
      <Route path="change-password" element={<ChangePassword messages={messages}/>} />
      <Route path="signup" element={<SignUp messages={messages}/>} />
    </Routes>
  );
};
