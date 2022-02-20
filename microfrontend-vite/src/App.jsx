import { Routes, Route, useNavigate } from 'react-router-dom';
import { lazy, Suspense, useEffect } from 'react';
import Navbar from './components/Navbar';
import Home from './components/home/Home';
import './resources/css/app.css';
import { getToken, isAuthenticated, logout, useStore } from './store';
import { getUser } from './api/user-api';

const User = lazy(() => import('./components/user/User'));
const About = lazy(() => import('./components/about/About'));

function App() {
  const navigate = useNavigate();

  useEffect(async () => {
    if (getToken()) {
      try {
        await getUser();
        isAuthenticated() && navigate('/', { replace: true });        
      } catch (error) {
        logout();
        navigate('auth/signin', { replace: true })        
      }
    }
  }, [useStore().token]);

  return (
    <>
      <Navbar />
      <Suspense fallback={<div>Loading</div>}>
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='auth/*' element={<User />} /> 
          <Route path='about/*' element={<About />}/>        
        </Routes>
      </Suspense>
    </>
  );
}

export default App;
