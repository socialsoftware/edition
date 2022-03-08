import { Route, Routes } from 'react-router-dom';
import { lazy } from 'react';
import messages from './resources/constants';
import { useStore } from '../../store';
import './resources/edition.css';

const MainEdition = lazy(() => import('./page/MainEdition'));
const UserEditions = lazy(() => import('./page/UserEditions'));
const selector = (sel) => (state) => state[sel];

export default () => {
  const language = useStore(selector('language'));


  return (
    <div className="container" style={{marginBottom: "20px"}}>
      <Routes>
        <Route path='acronym/*' element={<MainEdition messages={messages?.[language]} />}/>
        <Route path='user/:username' element={<UserEditions messages={messages?.[language]}/>}/>
      </Routes>
    </div>
  );
};
