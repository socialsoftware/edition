import { lazy } from 'react';
import { Route, Routes } from 'react-router-dom';
import { storeStateSelector } from '../../store';
import { documentStateSelector } from './documentsStore';
import messages from './resources/constants';
import './resources/documents.css';

const SourceList = lazy(() => import('./pages/SourceList'));
const Fragments = lazy(() => import('./pages/Fragments'));

export default () => {

  return (
    <div className="container" style={{ marginBottom: '20px' }}>
      <Routes>
        <Route
          path="/source/list"
          element={
            <SourceList
              messages={messages}
              language={storeStateSelector('language')}
            />
          }
        />
        <Route
          path="/fragments/*"
          element={
            <Fragments
              messages={messages}
              language={storeStateSelector('language')}
            />
          }
        />
      </Routes>
    </div>
  );
};
