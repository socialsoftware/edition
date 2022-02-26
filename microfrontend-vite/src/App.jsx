import { Routes, Route, useNavigate } from 'react-router-dom';
import { lazy, Suspense, useEffect } from 'react';
import Navbar from './microfrontends/Navbar';
import Home from './microfrontends/home/Home';
import './resources/css/app.css';
import { getToken, isAuthenticated, logout, useStore } from './store';
import { getUser } from './microfrontends/user/api/users';
import NoPage from './microfrontends/NoPage';
import Loading from './microfrontends/Loading';
import LoadingModal from './microfrontends/LoadingModal';

const UserRouter = lazy(() => import('./microfrontends/user/UserRouter'));
const AboutRouter = lazy(() => import('./microfrontends/about/AboutRouter'));
const Reading = lazy(() => import('./microfrontends/reading/ReadingRouter'));


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
          <Route path='/' element={<Home />} />
          <Route path='auth/*' element={<UserRouter />} /> 
          <Route path='about/*' element={<AboutRouter />}/>
          <Route path='reading/*' element={<Reading />}/>
          <Route path="*" element={<NoPage />} />
        </Routes>
      </Suspense>
    </>
  );
}

export default App;
