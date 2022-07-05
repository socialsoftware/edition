import { Link } from 'react-router-dom';
import { getName, logout, getLanguage } from '../../store';

export const Login = ({ messages, classes }) => (
  <li className={classes}>
    <Link to="/auth/signin">{messages[getLanguage()]['header_login']}</Link>
  </li>
);


export const LoggedIn = ({ messages, classes }) => (
  <li className={classes}>
    <a className="dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
      {getName()}
      <span className="caret"></span>
    </a>
    <ul className="dropdown-menu">
      <li>
        <Link to="/auth/change-password">
          {messages[getLanguage()]['user_password']}
        </Link>
      </li>
      <li>
        <Link to="/auth/signin" onClick={logout}>
          {messages[getLanguage()]['header_logout']}
        </Link>
      </li>
    </ul>
  </li>
);
