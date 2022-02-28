import { lazy, useState, useEffect } from 'react';
import { Route, Routes } from 'react-router-dom';
import { getLanguage } from '../../store';
import './resources/documents.css'

const SourceList = lazy(() => import('./pages/SourceList'));
const Fragments = lazy(() => import('./pages/Fragments'));
export const getMessages = () =>
  import(`./resources/constants/messages-${getLanguage()}.js`);

export default () => {

  const [messages, setMessages] = useState();

  useEffect(() => {
    getMessages().then(({ messages }) => setMessages(messages));
  }, [getLanguage()]);


  return (
    <div className="container">
      <Routes>
        <Route path="/source/list" element={<SourceList  messages={messages}/>} />
        <Route path="/fragments/*" element={<Fragments messages={messages}/>} />
      </Routes>
    </div>
  );
};
