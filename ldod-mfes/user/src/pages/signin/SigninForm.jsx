import check from '@src/resources/icons/check-circle.svg';
import exclamation from '@src/resources/icons/exclamation-circle.svg';
import eye from '@src/resources/icons/eye-solid.svg';
import google from '@src/resources/icons/google.svg';
import { socialAuth } from '@src/socialAuth.js';
import { userReferences } from '../../user';
import { capitalizeFirstLetter } from '../../utils';

export default ({ node }) => {
  return (
    <div class="row">
      <div class="login-form">
        <h2 data-key="signin-title">{node.getConstants('signin-title')}</h2>
        <form role="form" onSubmit={node.handleSubmit} class="form">
          <div class="col-md-offset-4 col-md-4">
            <div class="form-floating">
              <input
                class="form-control"
                id="username"
                type="text"
                autoComplete="username"
                name="username"
                value={node.username}
                onKeyUp={({ target: { value } }) => (node.username = value)}
                placeholder={node.getConstants('username')}
                title={node.getConstants('required')}
              />
              <label data-key="username" for="username">
                {node.getConstants('username')}
              </label>
              <img src={check} class="icon-validation valid" />
              <img src={exclamation} class="icon-validation invalid" />
              <small data-key="required"></small>
            </div>
          </div>
          <div class="col-md-offset-4 col-md-4">
            <div class="form-floating">
              <input
                id="password"
                class="form-control"
                type="password"
                autoComplete="current-password"
                name="password"
                value={node.password}
                onKeyUp={({ target: { value } }) => (node.password = value)}
                placeholder={node.getConstants('password')}
                title={node.getConstants('required')}
              />
              <label data-key="password" for="password">
                {node.getConstants('password')}
              </label>
              <img
                src={eye}
                class="icon"
                onPointerDown={node.revealPassword}
                onPointerUp={node.hidePassword}
              />
              <img src={check} class="icon-validation valid" />
              <img src={exclamation} class="icon-validation invalid" />
              <small data-key="required"></small>
            </div>
          </div>
          <div class="col-md-offset-5 col-md-2">
            <button
              data-key="sign-in"
              class="btn btn-primary"
              type="submit"
              style={{ width: '100%' }}>
              {node.getConstants('sign-in')}
            </button>
          </div>
        </form>
        <div style={{ padding: '4px 16px' }}>
          {[['google', google]].map(([provider, src]) => (
            <div class="col-md-offset-5 col-md-2">
              <button
                class={`btn btn-outline-primary social ${provider}`}
                type="button"
                onClick={() => socialAuth(provider, node)}
                style={{ width: '100%' }}>
                <img src={src} class="social-icon" />
                {capitalizeFirstLetter(provider)}
              </button>
            </div>
          ))}
        </div>
      </div>
      <div class="row">
        <a data-key="signup" is="nav-to" to={userReferences.signup}>
          {node.getConstants('signup') ?? ''}
        </a>
      </div>
    </div>
  );
};
