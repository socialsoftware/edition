import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import {
  defaultHeaders, isAdmin, isAuthenticated, setEditionHeaders, setLanguage, storeStateSelector
} from '../../store';
import { LoggedIn, Login } from './LoginNavHeader';
import './resources/common.css';
import messages from './resources/constants';
import { navHeaders } from './resources/header_modules';

export default () => {
  const user = storeStateSelector('user');
  const editionHeaders = storeStateSelector('editionHeaders');
  const language = storeStateSelector('language')

  useEffect(() => {
    user && user?.selectedVE
      ? setEditionHeaders([
          ...defaultHeaders,
          ...user.selectedVE.map((ve) => ({
            name: ve,
            route: `/edition/acronym/${ve}`,
          })),
        ])
      : setEditionHeaders(defaultHeaders);
  }, [user?.selectedVE]);

  const changeLang = (lang) => {
    language !== lang && setLanguage(lang);
  };

  const isHeaderVisible = (key) => {
    if (key !== 'admin') return true;
    return isAdmin();
  };

  return (
    <nav
      className="ldod-navbar navbar navbar-default navbar-fixed-top"
      role="navigation"
    >
      <div className="container-fluid">
        <div className="container">
          <div className="navbar-header">
            <button
              type="button"
              className="navbar-toggle collapsed"
              data-toggle="collapse"
              data-target="#navbar-headers"
            >
              <span className="sr-only">Toggle navigation</span>
              <span className="icon-bar"></span>
              <span className="icon-bar"></span>
              <span className="icon-bar"></span>
            </button>
            <Link className="navbar-brand" to="/">
              {messages[language]['header_title']}
            </Link>
            <img
              alt="vite-logo"
              className="logo"
              src={
                new URL('./resources/assets/vite-logo.svg', import.meta.url)
                  .href
              }
            />
            <img
              alt="react-logo"
              className="logo"
              src={
                new URL('./resources/assets/react.svg', import.meta.url).href
              }
            />
            <ul className="nav navbar-nav navbar-right hidden-xs">
              {!isAuthenticated() ? (
                <Login messages={messages} />
              ) : (
                <LoggedIn
                  messages={messages}
                  classes={'dropdown login logged-in'}
                />
              )}
            </ul>
          </div>
        </div>
      </div>
      <div className="container">
        <div className="navbar-collapse collapse" id="navbar-headers">
          <ul className="nav navbar-nav navbar-nav-flex">
            {Object.entries(navHeaders).map(([key, module], index) => (
              <li
                key={index}
                className="dropdown"
                style={{ display: !isHeaderVisible(key) && 'none' }}
              >
                <a className="dropdown-toggle" data-toggle="dropdown">
                  {messages[language][module.name]}{' '}
                  <span className="caret"></span>{' '}
                </a>
                <ul key={index} className="dropdown-menu">
                  <div className="dropdown-menu-bg"></div>
                  {module.name === 'header_editions' ? (
                    <>
                      {editionHeaders?.map((page, index) => (
                        <li key={index} className={page?.className ?? ''}>
                          {page?.route && (
                            <Link to={page?.route}>
                              {messages[language][page?.id ?? ""] ?? page?.name ?? ""}
                            </Link>
                          )}
                        </li>
                      ))}
                    </>
                  ) : (
                    <>
                      {Object.values(module.pages).map((page, index) => (
                        <li key={index} className={page?.className ?? ''}>
                          {page?.route ? (
                            <Link to={page?.route}>
                              {messages[language][page?.id]}
                            </Link>
                          ) : (
                            <a
                              href="https://ldod.uc.pt/ldod-visual"
                              target="_blank"
                            >
                              {messages[language][page?.id]}
                            </a>
                          )}
                        </li>
                      ))}
                    </>
                  )}
                </ul>
              </li>
            ))}
            {!isAuthenticated() ? (
              <Login messages={messages} classes={'login visible-xs'} />
            ) : (
              <LoggedIn
                messages={messages}
                classes={'dropdown login logged-in visible-xs'}
              />
            )}
            <li className="nav-lang">
              <a
                className={language === 'pt' ? 'active' : ''}
                onClick={() => changeLang('pt')}
              >
                PT
              </a>
              <a
                className={language === 'en' ? 'active' : ''}
                onClick={() => changeLang('en')}
              >
                EN
              </a>
              <a
                className={language === 'es' ? 'active' : ''}
                onClick={() => changeLang('es')}
              >
                ES
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
};
