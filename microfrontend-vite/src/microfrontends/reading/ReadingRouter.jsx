import { lazy } from 'react';
import { Route, Routes } from 'react-router-dom';
import './resources/css/reading.css';

const Citations = lazy(() => import('./pages/Citations'));
const ReadingMain = lazy(() => import('./pages/ReadingMain'));

export default () => {
  return (
    <div className="ldod-default container">
      <Routes>
        <Route path="/*" element={<ReadingMain />} />
        <Route path="citations" element={<Citations />} />
      </Routes>
    </div>
  );
};
