import { Route, Routes } from 'react-router-dom';
import { lazy, useState, useEffect } from 'react';
import Reading from './Reading';
import { getLanguage } from '../../../store';

const Fragment = lazy(() => import('./Fragment'));

export const rightArrowUrl = new URL(
  '../resources/assets/arrow-right.svg',
  import.meta.url
).href;

export const leftArrowUrl = new URL(
  '../resources/assets/arrow-left.svg',
  import.meta.url
).href;

const getMessages = () =>
  import(`../resources/constants/messages-${getLanguage()}.js`);

export default () => {
  const [messages, setMessages] = useState();
  useEffect(() => {
    setMessages(getMessages().then(({ messages }) => setMessages(messages)));
  }, [getLanguage()]);

  return (
    <>
      <div className="row">
        <div className="main-content">
          <div className="row reading-grid">
            <Routes>
              <Route index element={<Reading messages={messages} />} />
              <Route
                path="/fragment/:xmlid/inter/:urlid"
                element={<Fragment messages={messages}/>}
              />
            </Routes>
          </div>
        </div>
      </div>
    </>
  );
};
