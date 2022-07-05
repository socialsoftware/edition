import { Route, Routes } from 'react-router-dom';
import { lazy } from 'react';
import { storeStateSelector } from '../../store';
import messages from './resources/constants'
import './resources/search.css'

const Simple = lazy(() => import('./pages/Simple'));
const Advanced = lazy(() => import('./pages/Advanced'));


export default () => {
  const language = storeStateSelector('language');

  return (
    <div className="container" style={{ marginBottom: '20px' }}>
      <Routes>
        <Route path="/simple" element={<Simple messages={messages[language]} />} />
        <Route path="/advanced" element={<Advanced messages={messages[language]} />} />
      </Routes>
    </div>
  );
};
