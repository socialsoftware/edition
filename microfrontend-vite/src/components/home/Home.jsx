import { Link } from 'react-router-dom';
import { useEffect } from 'react';
import '../../resources/css/home.css';
import { excerpts } from '../../resources/excerpt';
import { useStore } from '../../store';
import HomeInfo from './HomeInfo';
import HomeAbout from './HomeAbout';
import BottomBar from './BottomBar';

export default () => {
  const { language } = useStore();

  useEffect(() => {
    window.scrollTo({ top: 0 });
  }, []);

  const excerpt = excerpts[parseInt(Math.random() * excerpts.length)];
  const readingBox = parseInt(Math.random() * 2) + 1;
  const documentsBox = parseInt(Math.random() * 2) + 1;
  const editionBox = parseInt(Math.random() * 2) + 1;
  const searchBox = parseInt(Math.random() * 2) + 1;
  const virtualBox = parseInt(Math.random() * 2) + 1;

  const boxUrl = (version, type, random) =>
    new URL(
      `../../resources/assets/boxes/${version}-${language}-${type}-${random}.svg`,
      import.meta.url
    ).href;
  const boxUrlH = (version, type, random) =>
    new URL(
      `../../resources/assets/boxes/${version}-${language}-${type}-${random}-h.svg`,
      import.meta.url
    ).href;

  return (
    <div className="ldod-default">
      <div className="container">
        <Link to={`/reading/fragment/${excerpt.excerpt}`} className="frag-link">
          <div className="raw col-xs-12 frag-excerpt">
            <span className="frag-number font-egyptian">{excerpt.number}</span>
            <span className="frag-editor font-condensed">{excerpt.editor}</span>
          </div>
        </Link>
        <div className="frag-excerpt-text font-grotesque">
          <p>{excerpt.text}</p>
        </div>
        <hr className="line-points" />
        <HomeAbout />
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
        <HomeInfo />
      </div>
      <BottomBar />
    </div>
  );
};
