import { lazy, useState, useEffect } from 'react';
import { Route, Routes } from 'react-router-dom';
import { getLanguage } from '../../store';
import './resources/css/reading.css';

const Citations = lazy(() => import('./pages/Citations'));
const ReadingMain = lazy(() => import('./pages/ReadingMain'));
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
        <Route path="/*" element={<ReadingMain  messages={messages}/>} />
        <Route path="citations" element={<Citations messages={messages}/>} />
      </Routes>
    </div>
  );
};
