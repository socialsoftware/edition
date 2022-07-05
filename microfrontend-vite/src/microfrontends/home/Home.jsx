import { Link, useNavigate } from 'react-router-dom';
import React, { useEffect } from 'react';
import './resources/home.css';
import { excerpts } from './resources/constants/excerpts';
import { getLanguage } from '../../store';
import HomeInfo from './HomeInfo';
import messages from './resources/constants/constants';

export default () => {
  const navigate = useNavigate();

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
    navigate(`/reading/fragment/${xmlid}/inter/${urlid}`);
  };

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
        <div onClick={() => readingFrag(excerpt)} className="frag-link">
          <div className="raw col-xs-12 frag-excerpt">
            <span className="frag-number font-egyptian">{excerpt.number}</span>
            <span className="frag-editor font-condensed">{excerpt.editor}</span>
          </div>
        </div>
        <div className="frag-excerpt-text font-grotesque">
          <p>{excerpt.text}</p>
        </div>
        <hr className="line-points" />
        <div className="about font-monospace">
          <p>{messages?.[getLanguage()].about}</p>
        </div>
        <hr className="line-x" />
        <div className="menu-boxes hidden-xs col-xs-12">
          <Link to="/reading" aria-label='reading module redirection'>
            <div className="div-link">
              <img src={boxUrl('D', '01', readingBox)} alt="reading-box"/>
              <img src={boxUrlH('D', '01', readingBox)} alt="reading-box"/>
            </div>
          </Link>
          <hr className="line-points" />
          <Link to="/documents/source/list" aria-label='documents source list redirection'>
            <div className="div-link">
              <img src={boxUrl('D', '02', documentsBox)} alt="documents-box"/>
              <img src={boxUrlH('D', '02', documentsBox)} alt="documents-box"/>
            </div>
          </Link>
          <hr className="line-points" />
          <Link to="/edition" aria-label='edition module redirection'>
            <div className="div-link">
              <img src={boxUrl('D', '03', editionBox)} alt="edition-box"/>
              <img src={boxUrlH('D', '03', editionBox)} alt="edition-box"/>
            </div>
          </Link>
          <hr className="line-points" />
          <Link to="/search/simple" aria-label='search module redirection'>
            <div className="div-link">
              <img src={boxUrl('D', '04', searchBox)} alt="search-box"/>
              <img src={boxUrlH('D', '04', searchBox)} alt="search-box"/>
            </div>
          </Link>
          <hr className="line-points" />
          <Link to="/virtualeditions" aria-label='virtual editions module redirection'>
            <div className="div-link">
              <img src={boxUrl('D', '05', virtualBox)} alt="virtual-edition-box"/>
              <img src={boxUrlH('D', '05', virtualBox)} alt="virtual-edition-box"/>
            </div>
          </Link>
        </div>

        <div className="menu-boxes visible-xs-inline col-xs-12">
          <Link to="/reading">
            <div className="div-link" aria-label='mobile reading module redirection'>
              <img src={boxUrl('M', '01', readingBox)} alt="reading-box"/>
              <img src={boxUrlH('M', '01', readingBox)} alt="reading-box"/>
            </div>
          </Link>

          <hr className="line-points" />

          <Link to="/source/list">
            <div className="div-link" aria-label='mobile document source list module redirection'>
              <img src={boxUrl('M', '02', documentsBox)} alt="documents-box"/>
              <img src={boxUrlH('M', '02', documentsBox)} alt="documents-box"/>
            </div>
          </Link>

          <hr className="line-points" />

          <Link to="/edition" aria-label='mobile edition module redirection'>
            <div className="div-link">
              <img src={boxUrl('M', '03', editionBox)} alt="edition-box"/>
              <img src={boxUrlH('M', '03', editionBox)} alt="edition-box"/>
            </div>
          </Link>

          <hr className="line-points" />
          <Link to="/search/simple" aria-label='mobile simple search module redirection'>
            <div className="div-link">
              <img src={boxUrl('M', '04', searchBox)} alt="search-box"/>
              <img src={boxUrlH('M', '04', searchBox)} alt="search-box"/>
            </div>
          </Link>

          <hr className="line-points" />

          <Link to="/virtualeditions" aria-label='mobile virtual editions module redirection'>
            <div className="div-link">
              <img src={boxUrl('M', '05', virtualBox)} alt="virtual-edition-box"/>
              <img src={boxUrlH('M', '05', virtualBox)} alt="virtual-edition-box"/>
            </div>
          </Link>
        </div>
        <HomeInfo info={messages?.[getLanguage()].info} />
      </div>
      <div className="bottom-bar"></div>
    </div>
  );
};
