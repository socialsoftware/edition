import { useState } from 'react';
import { PASSWORD_MIN_LENGTH } from '../resources/constants/constants';
import { useNavigate } from 'react-router-dom';
import { changePassword } from '../api/users';
import { useStore } from '../../../store';

export default ({ messages }) => {
  const navigate = useNavigate();
  const [currentPasswordErrors, setCurrentPasswordErrors] = useState(null);
  const [newPasswordErrors, setNewPasswordErrors] = useState([]);
  const [retypedPasswordErrors, setRetypedPasswordErrors] = useState([]);

  const [changePasswordData, setChangePasswordData] = useState({
    username: useStore().user.username,
    currentPassword: '',
    newPassword: '',
    retypedPassword: '',
  });

  const checkPasswords = () => {
    let result = true;
    if (changePasswordData.newPassword.length < PASSWORD_MIN_LENGTH) {
      setNewPasswordErrors((old) => [...old, messages?.['size']]);
      result = false;
    }

    if (changePasswordData.retypedPassword.length < PASSWORD_MIN_LENGTH) {
      setRetypedPasswordErrors((old) => [...old, messages?.['size']]);
      result = false;
    }

    if (changePasswordData.newPassword !== changePasswordData.retypedPassword) {
      setNewPasswordErrors((old) => [
        ...old,
        messages?.['different_changePasswordForm'],
      ]);
      setRetypedPasswordErrors((old) => [
        ...old,
        messages?.['different_changePasswordForm'],
      ]);
      result = false;
    }
    return result;
  };

  const cleanErrors = () => {
    setCurrentPasswordErrors(null);
    setNewPasswordErrors([]);
    setRetypedPasswordErrors([]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    cleanErrors();
    if (checkPasswords()) {
      let { data } = await changePassword(changePasswordData);
      if (data === 'error')
        setCurrentPasswordErrors(
          messages?.['doNotMatch_changePasswordForm']
        );
      else navigate('/', { replace: true });
    }
  };

  return (
    <div className="container text-center">
      <div className="row">
        <h3>{messages?.['user_password']}</h3>
      </div>
      <div className="row">
        <br />
        <br />
        <form className="form-horizontal" onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="col-sm-4 control-label">
              {messages?.['user_password_current']}
            </label>
            <div className="col-sm-4">
              <input
                className="form-control"
                type="password"
                value={changePasswordData.currentPassword}
                onChange={(e) =>
                  setChangePasswordData({
                    ...changePasswordData,
                    currentPassword: e.target.value,
                  })
                }
              />
            </div>
            <div className="col-sm-2">
              {currentPasswordErrors && (
                <span className="has-error">{currentPasswordErrors}</span>
              )}
            </div>
          </div>
          <div className="form-group">
            <label className="col-sm-4 control-label">
              {messages?.['user_password_new']}
            </label>
            <div className="col-sm-4">
              <input
                className="form-control"
                type="password"
                value={changePasswordData.newPassword}
                onChange={(e) =>
                  setChangePasswordData({
                    ...changePasswordData,
                    newPassword: e.target.value,
                  })
                }
              />
            </div>
            <div className="col-sm-2">
              {newPasswordErrors.length > 0 && (
                <span className="has-error">
                  {newPasswordErrors.map((er, index) => (
                    <div key={index}>
                      {er}
                      <br />
                    </div>
                  ))}
                </span>
              )}
            </div>
          </div>
          <div className="form-group">
            <label className="col-sm-4 control-label">
              {messages?.['user_password_retype']}
            </label>
            <div className="col-sm-4">
              <input
                className="form-control"
                type="password"
                value={changePasswordData.retypedPassword}
                onChange={(e) =>
                  setChangePasswordData({
                    ...changePasswordData,
                    retypedPassword: e.target.value,
                  })
                }
              />
            </div>
            <div className="col-sm-2">
              {retypedPasswordErrors.length > 0 && (
                <span className="has-error">
                  {retypedPasswordErrors.map((er, index) => (
                    <div key={index}>
                      {er}
                      <br />
                    </div>
                  ))}
                </span>
              )}
            </div>
          </div>
          <div className="form-group">
            <div className="col-sm-12">
              <button type="submit" className="btn btn-primary">
                {messages?.['general_update']}
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};
