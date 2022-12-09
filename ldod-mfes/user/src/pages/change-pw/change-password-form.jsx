import eye from '@src/resources/icons/eye-solid.svg';
import check from '@src/resources/icons/check-circle.svg';
import exclamation from '@src/resources/icons/exclamation-circle.svg';

export default ({ node }) => {
  return (
    <>
      <div class="row">
        <h3 data-key="change-password">
          {node.getConstants('change-password')}
        </h3>
      </div>
      <div class="row">
        <form onSubmit={node.handleSubmit} role="form" class="form">
          <input
            name="username"
            autoComplete="username"
            type="hidden"
            value={node.username}
          />
          <div class="col-md-offset-4 col-md-4">
            <div class="form-floating">
              <input
                id="current"
                class="form-control"
                name="current"
                type="password"
                autoComplete="current-password"
                value={node.current.value}
                onKeyUp={({ target: { value } }) =>
                  (node.current.value = value)
                }
                placeholder={node.getConstants('current')}
              />
              <label data-key="current" for="current">
                {node.getConstants('current')}
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
              <small data-key="required"></small>
            </div>
          </div>
          <div class="col-md-offset-4 col-md-4">
            <div class="form-floating">
              <input
                id="new"
                class="form-control"
                name="new"
                type="password"
                autoComplete="new-password"
                value={node.new.value}
                onKeyUp={({ target: { value } }) => (node.new.value = value)}
                placeholder={node.getConstants('new')}
              />
              <label data-key="new" for="new">
                {node.getConstants('new')}
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
              <small data-key="minCurrent"></small>
            </div>
          </div>
          <div class="col-md-offset-4 col-md-4">
            <div class="form-floating">
              <input
                id="confirm"
                class="form-control"
                name="confirm"
                type="password"
                autoComplete="new-password"
                onKeyUp={({ target: { value } }) =>
                  (node.confirm.value = value)
                }
                value={node.confirm.value}
                placeholder={node.getConstants('confirm')}
              />
              <label data-key="confirm" for="confirm">
                {node.getConstants('confirm')}
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
              <small data-key="confirmPattern"></small>
            </div>
          </div>

          <div class="form-group row">
            <div class="col-sm-12">
              <button data-key="update" class="btn btn-primary" type="submit">
                {node.getConstants('update')}
              </button>
            </div>
          </div>
        </form>
      </div>
    </>
  );
};
