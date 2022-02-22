import '../resources/css/navbar.css';
import { messages } from '../resources/constants';
import {
  useStore,
  setLanguage,
  logout,
  isAuthenticated,
  isAdmin,
  getName,
} from '../store';
import { modules } from '../resources/header_modules';
import { Link } from 'react-router-dom';

export default () => {
  const { language } = useStore();

  const changeLang = (lang) => language !== lang && setLanguage(lang);

  const isHeaderVisible = (key) => {
    if (key !== 'admin') return true;
    return isAuthenticated() && isAdmin();
  };

  const getLoginElement = () => (
    <Link to="/auth/signin">{messages[language]['header_login']}</Link>
  );

  const getLoggedInElement = () => (
    <>
      <a
        href="#"
        className="dropdown-toggle"
        data-toggle="dropdown"
        aria-expanded="false"
      >
        {getName()}
        <span className="caret"></span>
      </a>
      <ul className="dropdown-menu">
        <li>
          <Link to="/auth/change-password">
            {messages[language]['user_password']}
          </Link>
        </li>
        <li>
          <Link to="/auth/signin" onClick={logout}>
            {messages[language]['header_logout']}
          </Link>
        </li>
      </ul>
    </>
  );

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
                new URL('../resources/assets/vite-logo.svg', import.meta.url)
                  .href
              }
            />
            <img
              alt="react-logo"
              className="logo"
              src={
                new URL('../resources/assets/react.svg', import.meta.url).href
              }
            />
            <ul className="nav navbar-nav navbar-right hidden-xs">
              {!isAuthenticated() && <li>{getLoginElement()}</li>}
              {isAuthenticated() && (
                <li className="dropdown login logged-in">
                  {getLoggedInElement()}
                </li>
              )}
            </ul>
          </div>
        </div>
      </div>
      <div className="container">
        <div className="navbar-collapse collapse" id="navbar-headers">
          <ul className="nav navbar-nav navbar-nav-flex">
            {Object.entries(modules).map(([key, module], index) => {
              return (
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
                    {Object.values(module.pages).map((page, index) => (
                      <li key={index}>
                        {page.route ? (
                          <Link to={page.route}>
                            {messages[language][page.id]}
                          </Link>
                        ) : (
                          <a
                            href="https://ldod.uc.pt/ldod-visual"
                            target="_blank"
                          >
                            {messages[language][page.id]}
                          </a>
                        )}
                      </li>
                    ))}
                  </ul>
                </li>
              );
            })}
            {!isAuthenticated() && (
              <li className="login visible-xs">{getLoginElement()}</li>
            )}
            {isAuthenticated() && (
              <li className="dropdown login logged-in visible-xs">
                {getLoggedInElement()}
              </li>
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
