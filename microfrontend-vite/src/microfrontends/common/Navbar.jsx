import './resources/common.css';
import messages from './resources/constants';
import {
  setLanguage,
  isAuthenticated,
  isAdmin,
  getLanguage,
  useStore,
  setEditionHeaders,
  defaultHeaders,
} from '../../store';
import { navHeaders } from './resources/header_modules';
import { Link } from 'react-router-dom';
import { Login, LoggedIn } from './Login';
import { useEffect } from 'react';
const selector = (sel) => (state) => state[sel];

export default () => {
  const user = useStore(selector('user'));
  const editionHeaders = useStore(selector('editionHeaders'));

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
    getLanguage() !== lang && setLanguage(lang);
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
              {messages[getLanguage()]['header_title']}
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
                  {messages[getLanguage()][module.name]}{' '}
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
                              {messages[getLanguage()][page?.id ?? ""] ?? page?.name ?? ""}
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
                              {messages[getLanguage()][page?.id]}
                            </Link>
                          ) : (
                            <a
                              href="https://ldod.uc.pt/ldod-visual"
                              target="_blank"
                            >
                              {messages[getLanguage()][page?.id]}
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
                className={getLanguage() === 'pt' ? 'active' : ''}
                onClick={() => changeLang('pt')}
              >
                PT
              </a>
              <a
                className={getLanguage() === 'en' ? 'active' : ''}
                onClick={() => changeLang('en')}
              >
                EN
              </a>
              <a
                className={getLanguage() === 'es' ? 'active' : ''}
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
