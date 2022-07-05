import { Route, Routes } from 'react-router-dom';
import { lazy } from 'react';
import messages from './resources/constants';
import { storeStateSelector } from '../../store';
import './resources/edition.css';
import EditionIndex from './page/EditionIndex';

const MainEdition = lazy(() => import('./page/MainEdition'));
const UserEditions = lazy(() => import('./page/UserEditions'));

export default () => {
  const language = storeStateSelector('language');


  return (
    <div className="container" style={{ marginBottom: '20px' }}>
      <Routes>
        <Route index element={<EditionIndex messages={messages[language]}/>} />
        <Route
          path="acronym/*"
          element={<MainEdition messages={messages[language]} />}
        />
        <Route
          path="user/:username"
          element={<UserEditions messages={messages[language]} />}
        />
      </Routes>
    </div>
  );
};
