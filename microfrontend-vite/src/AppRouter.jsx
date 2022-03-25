import { lazy, Suspense, useEffect } from 'react';
import { Route, Routes, useNavigate } from 'react-router-dom';
import Error from './pages/Error';
import Loading from './shared/Loading';
import LoadingModal from './pages/LoadingModal';
import Navbar from './microfrontends/common/Navbar';
import NoPage from './pages/NoPage';
import Home from './microfrontends/home/Home';
import { getUser } from './microfrontends/user/api/users';
import './resources/css/app.css';
import {
  getToken,
  isAuthenticated,
  logout,
  setError,
  storeStateSelector,
} from './store';

const UserRouter = lazy(() => import('./microfrontends/user/UserRouter'));
const AboutRouter = lazy(() => import('./microfrontends/about/AboutRouter'));
const ReadingRouter = lazy(() =>
  import('./microfrontends/reading/ReadingRouter')
);
const DocumentsRouter = lazy(() =>
  import('./microfrontends/documents/DocumentsRouter')
);
const FragmentRouter = lazy(() =>
  import('./microfrontends/fragment/FragmentRouter')
);
const EditionRouter = lazy(() =>
  import('./microfrontends/editions/EditionRouter')
);
const SearchRouter = lazy(() => import('./microfrontends/search/SearchRouter'));

function App() {
  const navigate = useNavigate();
  const error = storeStateSelector('error');
  const token = storeStateSelector('loading');

  useEffect(() => {
    error && navigate('/error', { replace: true });
    setError();
  }, [error]);

  useEffect(() => {
    getToken() &&
      getUser()
        .then(() => isAuthenticated() && navigate('/', { replace: true }))
        .catch((error) => {
          console.error(error);
          logout();
          navigate('auth/signin', { replace: true });
        });
  }, [token]);

  return (
    <>
      <Navbar />
      <LoadingModal />
      <Suspense fallback={<Loading />}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/auth/*" element={<UserRouter />} />
          <Route path="/about/*" element={<AboutRouter />} />
          <Route path="/reading/*" element={<ReadingRouter />} />
          <Route path="/documents/*" element={<DocumentsRouter />} />
          <Route path="/fragments/*" element={<FragmentRouter />} />
          <Route path="/edition/*" element={<EditionRouter />} />
          <Route path="/edition/*" element={<EditionRouter />} />
          <Route path="/search/*" element={<SearchRouter />} />
          <Route path="/error" element={<Error />} />
          <Route path="*" element={<NoPage />} />
        </Routes>
      </Suspense>
    </>
  );
}

export default App;
