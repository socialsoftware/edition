import { Route, Routes } from 'react-router-dom';
import { useStore } from '../../store';
import FragmentPage from './pages/FragmentPage';
import messages from './resources/constants';
import './resources/fragment.css';
const language = (state) => state.language;

export default () => {
  return (
    <div className="container">
      <div className="row no-margins">
        <Routes>
          <Route
            path="/fragment/:xmlid"
            element={
              <FragmentPage messages={messages} language={useStore(language)} />
            }
          />
        </Routes>
      </div>
    </div>
  );
};
