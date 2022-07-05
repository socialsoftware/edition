import { lazy } from 'react';
import messages from './resources/constants';
import { Route, Routes } from 'react-router-dom';

import './resources/reading.css';
import { storeStateSelector } from '../../store';

const Citations = lazy(() => import('./pages/Citations'));
const ReadingMain = lazy(() => import('./pages/ReadingMain'));

export default () => {
  const language = storeStateSelector('language')
  
  return (
  <div className="container" style={{marginBottom: "20px"}}>
    <Routes>
      <Route path="/*" element={<ReadingMain messages={messages[language]} />} />
      <Route path="citations" element={<Citations messages={messages[language]} />} />
    </Routes>
  </div>
)}
