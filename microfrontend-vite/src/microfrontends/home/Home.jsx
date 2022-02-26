import { Link, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import './resources/home.css';
import { excerpts } from './resources/constants/excerpts';
import { getLanguage } from '../../store';
import HomeInfo from './HomeInfo';

const getMessages = () =>
  import(`./resources/constants/messages-${getLanguage()}.jsx`);

export default () => {
  const navigate = useNavigate();
  const [messages, setMessages] = useState();

  useEffect(() => {
    getMessages().then((messages) => setMessages(messages));
  }, [getLanguage()]);

  useEffect(() => {
    window.scrollTo({ top: 0 });
  }, []);

  const excerpt = excerpts[parseInt(Math.random() * excerpts.length)];
  const readingBox = parseInt(Math.random() * 2) + 1;
  const documentsBox = parseInt(Math.random() * 2) + 1;
  const editionBox = parseInt(Math.random() * 2) + 1;
  const searchBox = parseInt(Math.random() * 2) + 1;
  const virtualBox = parseInt(Math.random() * 2) + 1;

  const readingFrag = (excerpt) => {
    const xmlid = excerpt?.path.split('/')[0];
    const urlid = excerpt?.path.split('/')[2];
    navigate(`/reading/${xmlid}/${urlid}`);
  }

  const boxUrl = (version, type, random) =>
    new URL(
      `./resources/assets/boxes/${version}-${getLanguage()}-${type}-${random}.svg`,
      import.meta.url
    ).href;
  const boxUrlH = (version, type, random) =>
    new URL(
      `./resources/assets/boxes/${version}-${getLanguage()}-${type}-${random}-h.svg`,
      import.meta.url
    ).href;

  return (
    <div className="ldod-default">
      <div className="container">
        <a onClick={() => readingFrag(excerpt)} className="frag-link">
          <div className="raw col-xs-12 frag-excerpt">
            <span className="frag-number font-egyptian">{excerpt.number}</span>
            <span className="frag-editor font-condensed">{excerpt.editor}</span>
          </div>
        </a>
        <div className="frag-excerpt-text font-grotesque">
          <p>{excerpt.text}</p>
        </div>
        <hr className="line-points" />
        <div className="about font-monospace">
          <p>{messages?.about}</p>
        </div>
        <hr className="line-x" />
        <div className="menu-boxes hidden-xs col-xs-12">
          <Link to="/reading">
            <div className="div-link">
              <img src={boxUrl('D', '01', readingBox)} />
              <img src={boxUrlH('D', '01', readingBox)} />
            </div>
          </Link>
          <hr className="line-points" />
          <Link to="/source/list">
            <div className="div-link">
              <img src={boxUrl('D', '02', documentsBox)} />
              <img src={boxUrlH('D', '02', documentsBox)} />
            </div>
          </Link>
          <hr className="line-points" />
          <Link to="/edition">
            <div className="div-link">
              <img src={boxUrl('D', '03', editionBox)} />
              <img src={boxUrlH('D', '03', editionBox)} />
            </div>
          </Link>
          <hr className="line-points" />
          <Link to="/search/simple">
            <div className="div-link">
              <img src={boxUrl('D', '04', searchBox)} />
              <img src={boxUrlH('D', '04', searchBox)} />
            </div>
          </Link>
          <hr className="line-points" />
          <Link to="/virtualeditions">
            <div className="div-link">
              <img src={boxUrl('D', '05', virtualBox)} />
              <img src={boxUrlH('D', '05', virtualBox)} />
            </div>
          </Link>
        </div>

        <div className="menu-boxes visible-xs-inline col-xs-12">
          <Link to="/reading">
            <div className="div-link">
              <img src={boxUrl('M', '01', readingBox)} />
              <img src={boxUrlH('M', '01', readingBox)} />
            </div>
          </Link>

          <hr className="line-points" />

          <Link to="/source/list">
            <div className="div-link">
              <img src={boxUrl('M', '02', documentsBox)} />
              <img src={boxUrlH('M', '02', documentsBox)} />
            </div>
          </Link>

          <hr className="line-points" />

          <Link to="/edition">
            <div className="div-link">
              <img src={boxUrl('M', '03', editionBox)} />
              <img src={boxUrlH('M', '03', editionBox)} />
            </div>
          </Link>

          <hr className="line-points" />
          <Link to="/search/simple">
            <div className="div-link">
              <img src={boxUrl('M', '04', searchBox)} />
              <img src={boxUrlH('M', '04', searchBox)} />
            </div>
          </Link>

          <hr className="line-points" />

          <Link to="/virtualeditions">
            <div className="div-link">
              <img src={boxUrl('M', '05', virtualBox)} />
              <img src={boxUrlH('M', '05', virtualBox)} />
            </div>
          </Link>
        </div>
        <HomeInfo info={messages?.info} />
      </div>
      <div className="bottom-bar"></div>
    </div>
  );
};
