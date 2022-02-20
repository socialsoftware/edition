import { Route, Routes } from 'react-router-dom';
import { lazy } from 'react';
import '../../resources/css/about.css';
import HomeInfo from '../home/HomeInfo';
import BottomBar from '../home/BottomBar';

const Archive = lazy(() => import('./pages/archive/Archive'));
const Videos = lazy(() => import('./pages/videos/Videos'));
const Conduct = lazy(() => import('./pages/conduct/Conduct'));
const Tutorials = lazy(() => import('./pages/tutorials/Tutorials'));

export default () => {
  const scroll = (ref) => {
    const section = document.querySelector(ref);
    section.scrollIntoView({ behavior: 'smooth', block: 'start' });
  };

  return (
    <div>
      <div className="container">
        <div className="col-md-8 col-md-offset-2 ldod-about">
          <Routes>
            <Route path="archive" element={<Archive />} />
            <Route path="videos" element={<Videos scroll={scroll} />} />
            <Route path="tutorials" element={<Tutorials scroll={scroll} />} />
            <Route path="encoding" element={<Videos />} />
            <Route path="articles" element={<Videos />} />
            <Route path="book" element={<Videos />} />
            <Route path="conduct" element={<Conduct />} />
            <Route path="privacy" element={<Videos />} />
            <Route path="team" element={<Videos />} />
            <Route path="acknowledgements" element={<Videos />} />
            <Route path="contact" element={<Videos />} />
            <Route path="copyright" element={<Videos />} />
          </Routes>
        </div>
        <div className="ldod-default col-md-8 col-md-offset-2 ldod-about">
          <HomeInfo />
        </div>
      </div>
      <BottomBar />
    </div>
  );
};
