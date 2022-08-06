import { getLanguage } from '../../../store';
import { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { authenticate } from '../api/users';
import '../resources/user.css';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faTwitter,
  faGoogle,
  faFacebookF,
  faLinkedinIn,
} from '@fortawesome/free-brands-svg-icons';

export default ({ messages }) => {
  const location = useLocation();
  const [error, setError] = useState(location.state?.message);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    let data = { username, password };
    try {
      await authenticate(data);
    } catch (error) {
      setError('login_error');
    }
  };

  return (
    <>
      {error && (
        <div className="row text-error">{messages[getLanguage()][error]}</div>
      )}
      <div className="row">
        <div className="login-form">
          <h2>{messages[getLanguage()]['header_title']}</h2>
          <form className="form-horizontal" role="form" onSubmit={handleSubmit}>
            <div className="form-group">
              <div className="col-md-offset-4 col-md-4">
                <input
                  type="text"
                  className="form-control"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder={messages[getLanguage()]['login_username']}
                />
              </div>
              <br />
              <br />
              <div className="col-md-offset-4 col-md-4">
                <input
                  type="password"
                  className="form-control"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder={messages[getLanguage()]['login_password']}
                />
              </div>
            </div>
            <div className="form-group">
              <div className="col-md-offset-5 col-md-2">
                <button className="btn btn-primary form-control" type="submit">
                  {messages[getLanguage()]['general_signin']}
                </button>
              </div>
            </div>
          </form>
        </div>
        <div className="row">
          <form id="tw_signin">
            <div className="col-md-offset-5 col-md-2">
              <button
                className="btn btn-block btn-social btn-twitter"
                type="submit">
                {' '}
                <i>
                  <FontAwesomeIcon icon={faTwitter} className="social-icon" />
                </i>
                Twitter
              </button>
            </div>
          </form>
        </div>

        <br />

        <div className="row">
          <form id="gg_signin">
            <div className="col-md-offset-5 col-md-2">
              <button
                className="btn btn-block btn-social btn-google-plus"
                type="submit">
                <i>
                  <FontAwesomeIcon icon={faGoogle} className="social-icon" />
                </i>
                Google
              </button>
            </div>
          </form>
        </div>

        <br />

        <div className="row">
          <form id="fb_signin" action="/signin/facebook" method="POST">
            <div className="col-md-offset-5 col-md-2">
              <button
                className="btn btn-block btn-social btn-facebook"
                type="submit">
                <i>
                  <FontAwesomeIcon icon={faFacebookF} className="social-icon" />
                </i>
                Facebook
              </button>
            </div>
          </form>
        </div>
        <br />

        <div className="row">
          <form id="li_signin" action="/signin/linkedin" method="POST">
            <div className="col-md-offset-5 col-md-2">
              <button
                className="btn btn-block btn-social btn-linkedin"
                type="submit">
                <i>
                  <FontAwesomeIcon
                    icon={faLinkedinIn}
                    className="social-icon"
                  />
                </i>
                Linkedin
              </button>
            </div>
          </form>
        </div>
        <br />
        <div className="row">
          <Link to="/auth/signup">
            {messages[getLanguage()]['signup_message']}
          </Link>
        </div>
      </div>
    </>
  );
};
