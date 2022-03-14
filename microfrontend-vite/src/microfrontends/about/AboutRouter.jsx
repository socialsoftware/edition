import { Route, Routes } from 'react-router-dom';
import { lazy } from 'react';
import './resources/about.css';
import messages from './resources/constants';

// TODO: dependecy from Home MFE
import HomeInfo from '../home/HomeInfo';
import { storeStateSelector } from '../../store';

const Archive = lazy(() => import('./pages/archive/Archive'));
const Videos = lazy(() => import('./pages/videos/Videos'));
const Tutorials = lazy(() => import('./pages/tutorials/Tutorials'));
const Faq = lazy(() => import('./pages/Faq/Faq'));
const Encoding = lazy(() => import('./pages/enconding/Encoding'));
const Articles = lazy(() => import('./pages/articles/Articles'));
const Book = lazy(() => import('./pages/book/Book'));
const Conduct = lazy(() => import('./pages/conduct/Conduct'));
const Privacy = lazy(() => import('./pages/privacy/Privacy'));
const Team = lazy(() => import('./pages/team/Team'));
const Ack = lazy(() => import('./pages/Ack/Ack'));
const Contact = () => messages?.[getLanguage()]['contact'];
const Copyright = lazy(() => import('./pages/copyright/Copyright'));



export default () => {
  const language = storeStateSelector('language')

  const scroll = (ref) => {
    const section = document.querySelector(ref);
    section.scrollIntoView({ behavior: 'smooth', block: 'start' });
  };

  return (
    <div className="ldod-default">
      <div className="container">
        <div className="col-md-8 col-md-offset-2 ldod-about">
          <Routes>
            <Route path="archive" element={<Archive />} />
            <Route path="videos" element={<Videos scroll={scroll} />} />
            <Route path="tutorials" element={<Tutorials scroll={scroll} />} />
            <Route path="faq" element={<Faq scroll={scroll} />} />
            <Route path="encoding" element={<Encoding />} />
            <Route path="articles" element={<Articles scroll={scroll} />} />
            <Route path="book" element={<Book />} />
            <Route path="conduct" element={<Conduct messages={messages} language={language}/>} />
            <Route path="privacy" element={<Privacy />} />
            <Route path="team" element={<Team />} />
            <Route path="acknowledgements" element={<Ack />} />
            <Route path="contact" element={<Contact />} />
            <Route path="copyright" element={<Copyright />} />
          </Routes>
        </div>
        <div className="ldod-default col-md-8 col-md-offset-2 ldod-about">
          <HomeInfo info={messages[language].info} />
        </div>
      </div>
      <div className="bottom-bar"></div>
    </div>
  );
};
