import { Routes, Route, useNavigate } from 'react-router-dom';
import { lazy, Suspense, useEffect } from 'react';
import './resources/css/app.css';
import { getToken, isAuthenticated, logout, useStore } from './store';
import { getUser } from './microfrontends/user/api/users';
import Navbar from './microfrontends/common/Navbar';
import Home from './microfrontends/home/Home';
import NoPage from './microfrontends/common/NoPage';
import Loading from './microfrontends/common/Loading';
import LoadingModal from './microfrontends/common/LoadingModal';

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


function App() {
  const navigate = useNavigate();

  useEffect(async () => {
    getToken() &&
      getUser()
        .then(() => isAuthenticated() && navigate('/', { replace: true }))
        .catch((error) => {
          console.error(error);
          logout();
          navigate('auth/signin', { replace: true });
        });
  }, [useStore().token]);

  return (
    <>
      <Navbar />
      <LoadingModal />
      <Suspense fallback={<Loading />}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="auth/*" element={<UserRouter />} />
          <Route path="about/*" element={<AboutRouter />} />
          <Route path="reading/*" element={<ReadingRouter />} />
          <Route path="documents/*" element={<DocumentsRouter />} />
          <Route path="/fragments/*" element={<FragmentRouter />} />
          <Route path="*" element={<NoPage />} />
        </Routes>
      </Suspense>
    </>
  );
}

export default App;
