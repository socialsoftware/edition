import { lazy } from 'react';
import messages from './resources/constants';
import { Route, Routes } from 'react-router-dom';

import './resources/reading.css';

const Citations = lazy(() => import('./pages/Citations'));
const ReadingMain = lazy(() => import('./pages/ReadingMain'));

export default () => (
  <div className="container">
    <Routes>
      <Route path="/*" element={<ReadingMain messages={messages} />} />
      <Route path="citations" element={<Citations messages={messages} />} />
    </Routes>
  </div>
);
