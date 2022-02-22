import { Routes, Route, useNavigate } from 'react-router-dom';
import { lazy, Suspense, useEffect } from 'react';
import Navbar from './microfrontends/Navbar';
import Home from './microfrontends/home/Home';
import './resources/css/app.css';
import { getToken, isAuthenticated, logout, useStore } from './store';
import { getUser } from './microfrontends/user/api/users';

const User = lazy(() => import('./microfrontends/user/User'));
const About = lazy(() => import('./microfrontends/about/About'));
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
      <Suspense fallback={<div>Loading</div>}>
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='auth/*' element={<User />} /> 
          <Route path='about/*' element={<About />}/>
          <Route path='reading/*' element={<Reading />}/>
        </Routes>
      </Suspense>
    </>
  );
}

export default App;
