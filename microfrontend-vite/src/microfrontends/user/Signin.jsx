import { messages } from '../../resources/constants';
import { useStore  } from '../../store';
import { useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import '../../resources/css/bootstrap-social.css';
import { authenticate } from './api/users';

export default () => {
  const location = useLocation();
  const { language } = useStore();
  const [error, setError] = useState(location.state?.message);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');


    const handleSubmit = async (e) => {
    e.preventDefault();
    let data  = { username: username, password: password };
    try {
      await authenticate(data);
    } catch (error) {
      setError('login_error');
    }
  };

  return (
    <div className="container text-center">
      {error && (
        <div className="row text-error">
          {messages[language][error]}
        </div>
      )}
      <div className="row">
        <div className="login-form">
          <h2>{messages[language]['header_title']}</h2>
          <form
            className="form-horizontal"
            role="form"
            onSubmit={handleSubmit}
          >
            <div className="form-group">
              <div className="col-md-offset-4 col-md-4">
                <input
                  type="text"
                  className="form-control"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  placeholder={messages[language]['login_username']}
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
                  placeholder={messages[language]['login_password']}
                />
              </div>
            </div>
            <div className="form-group">
              <div className="col-md-offset-5 col-md-2">
                <button className="btn btn-primary form-control" type="submit">
                  {messages[language]['general_signin']}
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
                type="submit"
              >
                <span className="fab fa-twitter"></span> Twitter
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
                type="submit"
              >
                <span className="fab fa-google"></span>Google
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
                type="submit"
              >
                <span className="fab fa-facebook-f"></span>Facebook
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
                type="submit"
              >
                <span className="fab fa-linkedin-in"></span>Linkedin
              </button>
            </div>
          </form>
        </div>
        <br />
        <div className="row">
          <Link to="/auth/signup">{messages[language]['signup_message']}</Link>
        </div>
      </div>
    </div>
  );
};
