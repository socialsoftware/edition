import { lazy } from 'react';
import { Route, Routes } from 'react-router-dom';
import '../../resources/css/reading.css'


const Citations = lazy(() => import('./pages/citations'));
const Reading = lazy(() => import('./pages/reading'));

export default () => {
  return (
    <div className="ldod-default container">
      <Routes>
        <Route path="/" element={<Reading />} />
        <Route path="citations" element={<Citations />} />
        <Route path="ldod-visual" />
      </Routes>
    </div>
  );
};
