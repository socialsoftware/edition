import { Route, Routes } from 'react-router-dom';
import { lazy, Suspense } from 'react';
import './resources/about.css';
import messages from './resources/constants';

// TODO: dependecy from Home MFE
import HomeInfo from '../home/HomeInfo';
import { getLanguage, storeStateSelector } from '../../store';

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
const Copyright = lazy(() => import('./pages/copyright/Copyright'));

const scroll = (ref) => {
  const section = document.querySelector(ref);
  section.scrollIntoView({ behavior: 'smooth', block: 'start' });
};

const getLazyContact = (lang) => {
  const Contact = () => messages[lang]['contact'];
  return <Contact />;
};

export default () => {
  const language = storeStateSelector('language');

  return (
    <div className="ldod-default">
      <div className="container">
        <div className="col-md-8 col-md-offset-2 ldod-about">
          <Routes>
            <Route path="archive" element={<Archive language={language} />} />
            <Route
              path="videos"
              element={<Videos scroll={scroll} language={language} />}
            />
            <Route
              path="tutorials"
              element={<Tutorials scroll={scroll} language={language} />}
            />
            <Route
              path="faq"
              element={<Faq scroll={scroll} language={language} />}
            />
            <Route path="encoding" element={<Encoding language={language} />} />
            <Route
              path="articles"
              element={<Articles scroll={scroll} language={language} />}
            />
            <Route path="book" element={<Book language={language} />} />
            <Route
              path="conduct"
              element={<Conduct messages={messages} language={language} />}
            />
            <Route path="privacy" element={<Privacy language={language} />} />
            <Route
              path="team"
              element={<Team scroll={scroll} language={language} />}
            />
            <Route
              path="acknowledgements"
              element={<Ack language={language} />}
            />
            <Route
              path="copyright"
              element={<Copyright language={language} />}
            />
            <Route path="contact" element={<>{getLazyContact(language)}</>} />
          </Routes>
        </div>
        <div className="ldod-default col-md-8 col-md-offset-2 ldod-about">
          <HomeInfo info={messages[getLanguage()].info} />
        </div>
      </div>
      <div className="bottom-bar"></div>
    </div>
  );
};
