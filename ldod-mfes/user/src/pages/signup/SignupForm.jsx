import check from '@src/resources/icons/check-circle.svg';
import exclamation from '@src/resources/icons/exclamation-circle.svg';
import eye from '@src/resources/icons/eye-solid.svg';

export default ({ node }) => {
  return (
    <>
      <div class="row">
        <h3 data-key="register">{node.getConstants('register')}</h3>
      </div>
      <div class="row">
        <form onSubmit={node.handleSubmit} role="form" class="form">
          <div class="col-md-offset-4 col-md-4">
            <div class="form-floating">
              <input
                id="firstname"
                class="form-control"
                type="text"
                autoComplete="first-name"
                name="firstname"
                value={node.firstname.value}
                onKeyUp={({ target: { value } }) =>
                  (node.firstname.value = value)
                }
                placeholder={node.getConstants('firstname')}
                title={node.firstname.message()}
              />
              <label data-key="firstname" for="firstname">
                {node.getConstants('firstname')}
              </label>
              <img src={check} class="icon-validation valid" />
              <img src={exclamation} class="icon-validation invalid" />
              <small data-key="req-alphabetic"></small>
            </div>
          </div>
          <div class="col-md-offset-4 col-md-4">
            <div class="form-floating">
              <input
                id="lastname"
                class="form-control"
                type="text"
                autoComplete="family-name"
                name="lastname"
                value={node.lastname.value}
                onKeyUp={({ target: { value } }) =>
                  (node.lastname.value = value)
                }
                placeholder={node.getConstants('lastname')}
                title={node.lastname.message()}
              />
              <label data-key="lastname" for="lastname">
                {node.getConstants('lastname')}
              </label>

              <img src={check} class="icon-validation valid" />
              <img src={exclamation} class="icon-validation invalid" />
              <small data-key="req-alphabetic"></small>
            </div>
          </div>
          <div class="col-md-offset-4 col-md-4">
            <div class="form-floating">
              <input
                id="username"
                class="form-control"
                type="text"
                autoComplete="username"
                name="username"
                value={node.username.value}
                onKeyUp={({ target: { value } }) =>
                  (node.username.value = value)
                }
                placeholder={node.getConstants('username')}
                title={node.username.message()}
              />
              <label data-key="username" for="username">
                {node.getConstants('username')}
              </label>
              <img src={check} class="icon-validation valid" />
              <img src={exclamation} class="icon-validation invalid" />
              <small data-key="req-alphanumeric"></small>
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
                value={node.password.value}
                onKeyUp={({ target: { value } }) =>
                  (node.password.value = value)
                }
                placeholder={node.getConstants('password')}
                title={node.password.message()}
              />
              <label data-key="password" for="password">
                {node.getConstants('password')}
              </label>

              <img
                src={eye}
                alt="eye icon"
                class="icon"
                onPointerDown={node.revealPassword}
                onPointerUp={node.hidePassword}
              />
              <img src={check} class="icon-validation valid" />
              <img src={exclamation} class="icon-validation invalid" />
              <small data-key="min-6"></small>
            </div>
          </div>
          <div class="col-md-offset-4 col-md-4">
            <div class="form-floating">
              <input
                id="email"
                class="form-control"
                type="text"
                autoComplete="email"
                name="email"
                value={node.email.value}
                onKeyUp={({ target: { value } }) => (node.email.value = value)}
                placeholder={node.getConstants('email')}
                title={node.email.message()}
              />
              <label data-key="email" for="email">
                {node.getConstants('email')}
              </label>

              <img src={check} class="icon-validation valid" />
              <img src={exclamation} class="icon-validation invalid" />
              <small data-key="email-pattern"></small>
            </div>
          </div>
          <div class="col-md-offset-4 col-md-4">
            <div class="form-floating">
              <input
                type="checkbox"
                name="conduct"
                value={node.conduct.value}
                onChange={({ target }) => (node.conduct.value = target.checked)}
              />
              <span data-key="conduct">{node.getConstants('conduct')}</span>
              <img src={check} class="icon-validation valid" />
              <img src={exclamation} class="icon-validation invalid" />
              <img src={check} class="icon-validation valid" />
              <img src={exclamation} class="icon-validation invalid" />
              <small data-key="conduct-check"></small>
            </div>
          </div>
          <div class="col-sm-12">
            <button data-key="register" class="btn btn-primary" type="submit">
              {node.getConstants('register')}
            </button>
          </div>
        </form>
      </div>
    </>
  );
};
