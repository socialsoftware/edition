import { Route, Routes } from 'react-router-dom';
import { lazy } from 'react';
import { useStore } from '../../store';
import messages from './resources/constants';
import './resources/fragment.css';

const FragmentMain = lazy(() => import('./pages/FragmentMain'));
const FragmentPage = lazy(() => import('./pages/FragmentPage'));
const languageSel = (state) => state.language;


export default () => {
  const language = useStore(languageSel)

  return (
    <div className="container">
      <div className="row no-margins">
        <Routes>
          <Route
            path="/fragment/:xmlid"
            element={<FragmentMain messages={messages[language]} />}
          />
          <Route
            path="/fragment/:xmlid/inter/:urlid/*"
            element={<FragmentPage messages={messages[language]} />}
          />
        </Routes>
      </div>
    </div>
  );
};
