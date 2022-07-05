import {
  ALPHABETICAL_REGEX,
  ALPHANUMERIC_REGEX,
  EMAIL_REGEX,
  PASSWORD_MIN_LENGTH,
} from '../resources/constants/constants';
import { getLanguage } from '../../../store';
import { useState, lazy } from 'react';
import { signup } from '../api/users';
import { useNavigate } from 'react-router-dom';

const errorInitialState = {
  firstName: [],
  lastName: [],
  username: [],
  password: [],
  email: [],
  conduct: '',
};


export default ({messages}) => {
  const navigate = useNavigate();

  // TODO: dependecy from About MFE
  const Conduct = lazy(() => import(`../../about/pages/conduct/Conduct-${getLanguage()}.jsx`));


  const [signupData, setSignupData] = useState({
    firstName: '',
    lastName: '',
    username: '',
    password: '',
    email: '',
    conduct: false,
    socialMediaService: '',
    socialMediaId: '',
  });

  const [errors, setErrors] = useState(errorInitialState);

  const checkNonEmptyAndAlphabetic = (field) => {
      let valid = true;
    if (signupData[field].length === 0) {
        valid = false;
      setErrors((old) => ({
        ...old,
        [field]: [...old[field], messages[getLanguage()]['required']],
      }));
    }
    if (!ALPHABETICAL_REGEX.test(signupData[field])) {
      valid = false;
        setErrors((old) => ({
        ...old,
        [field]: [...old[field], messages[getLanguage()]['alphabetic_error']],
      }));
    }
    return valid;
  };

  const checkNonEmptyAndAlphanumeric = (field) => {
    let valid = true;
    if (signupData[field].length === 0) {
      valid = false;
        setErrors((old) => ({
        ...old,
        [field]: [...old[field], messages[getLanguage()]['required']],
      }));
    }
    if (!ALPHANUMERIC_REGEX.test(signupData[field])) {
      valid= false;
        setErrors((old) => ({
        ...old,
        [field]: [
          ...old[field],
          messages[getLanguage()]['edituserform_newusername_pattern'],
        ],
      }));
    }
    return valid;
  };

  const checkEmail = () => {
    let valid = true;
    if (signupData.email.length !== 0) {
      if (!EMAIL_REGEX.test(signupData.email)) {
          valid = false;
        setErrors((old) => ({
          ...old,
          email: [...old.email, messages[getLanguage()]['email']],
        }));
      }
    } else {
        valid = false;
      setErrors((old) => ({
        ...old,
        email: [...old.email, messages[getLanguage()]['required']],
      }));
    }
    return valid;
  };

  const checkData = () => {
    let valid = true;
    if (!checkNonEmptyAndAlphabetic('firstName')) valid = false;
    if (!checkNonEmptyAndAlphabetic('lastName')) valid = false;
    if (!checkNonEmptyAndAlphanumeric('username')) valid = false;
    if (signupData.password.length < PASSWORD_MIN_LENGTH) {
        valid = false;
      setErrors((old) => ({
        ...old,
        password: messages[getLanguage()]['size'],
      }));
    }
    if (!checkEmail()) valid = false;
    if (!signupData.conduct) {
        valid = false;
      setErrors((old) => ({
        ...old,
        conduct: messages[getLanguage()]['header_conduct_error'],
      }));
    }
    return valid;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors(errorInitialState);
    
    window.scrollTo({top:0, behavior: 'smooth'});
    if (checkData()) {
      try {
        let {data} = await signup(signupData);
        if (data === 'error') {
          setErrors((old) => ({
            ...old,
            username: [...old.email, messages[getLanguage()]['user_duplicateUsername']] 
          }));
        }
        else navigate('/auth/signin', {state: {message: 'signup_confirmation'}, replace: true})
      } catch (error) {
        console.error(error);        
      }
    }
  };

  return (
    <>
      <div className="row">
        <h3> {messages[getLanguage()]['signup']} </h3>
      </div>{' '}
      <div className="row">
        <br />
        <br />
        <form className="form-horizontal" onSubmit={handleSubmit}>
          <div className="form-group row">
            <label className="col-sm-4 control-label">
              {' '}
              {messages[getLanguage()]['user_firstName']}{' '}
            </label>
            <div className="col-sm-4">
              <input
                className="form-control"
                type="text"
                value={signupData.firstName}
                onChange={(e) =>
                  setSignupData((old) => ({
                    ...old,
                    firstName: e.target.value,
                  }))
                }
              />{' '}
            </div>
            <div className="col-sm-2">
              {errors.firstName.length > 0 &&
                errors.firstName.map((error, index) => (
                  <div key={index}>
                    {error}
                    <br />
                  </div>
                ))}
            </div>
          </div>
          <div className="form-group row">
            <label className="col-sm-4 control-label">
              {' '}
              {messages[getLanguage()]['user_lastName']}
            </label>{' '}
            <div className="col-sm-4">
              <input
                className="form-control"
                type="text"
                value={signupData.lastName}
                onChange={(e) =>
                  setSignupData((old) => ({
                    ...old,
                    lastName: e.target.value,
                  }))
                }
              />{' '}
            </div>{' '}
            <div className="col-sm-2">
              {errors.lastName.length > 0 &&
                errors.lastName.map((error, index) => (
                  <div key={index}>
                    {error}
                    <br />
                  </div>
                ))}
            </div>{' '}
          </div>{' '}
          <div className="form-group row">
            <label className="col-sm-4 control-label">
              {' '}
              {messages[getLanguage()]['login_username']}{' '}
            </label>{' '}
            <div className="col-sm-4">
              <input
                className="form-control"
                type="text"
                value={signupData.username}
                onChange={(e) =>
                  setSignupData((old) => ({
                    ...old,
                    username: e.target.value,
                  }))
                }
              />{' '}
            </div>{' '}
            <div className="col-sm-2">
              {errors.username.length > 0 &&
                errors.username.map((error, index) => (
                  <div key={index}>
                    {error}
                    <br />
                  </div>
                ))}
            </div>{' '}
          </div>{' '}
          <div className="form-group row">
            <label className="col-sm-4 control-label">
              {' '}
              {messages[getLanguage()]['login_password']}{' '}
            </label>{' '}
            <div className="col-sm-4">
              <input
                className="form-control"
                type="password"
                value={signupData.password}
                onChange={(e) =>
                  setSignupData((old) => ({
                    ...old,
                    password: e.target.value,
                  }))
                }
              />{' '}
            </div>{' '}
            <div className="col-sm-2">{errors.password}</div>{' '}
          </div>{' '}
          <div className="form-group row">
            <label className="col-sm-4 control-label">
              {' '}
              {messages[getLanguage()]['user_email']}{' '}
            </label>{' '}
            <div className="col-sm-4">
              <input
                className="form-control"
                type="text"
                value={signupData.email}
                onChange={(e) =>
                  setSignupData((old) => ({
                    ...old,
                    email: e.target.value,
                  }))
                }
              />{' '}
            </div>{' '}
            <div className="col-sm-2">
              {errors.email.length > 0 &&
                errors.email.map((error, index) => (
                  <div key={index}>
                    {error}
                    <br />
                  </div>
                ))}
            </div>{' '}
          </div>{' '}
          <div className="form-group row">
            <label className="col-sm-4 control-label">
              {' '}
              {messages[getLanguage()]['header_conduct']}{' '}
            </label>{' '}
            <div className="col-sm-4">
              <label className="form-check-label">
                <input
                  className="form-check-input"
                  type="checkbox"
                  checked={signupData.conduct}
                  onChange={(e) =>
                    setSignupData((old) => ({
                      ...old,
                      conduct: e.target.checked,
                    }))
                  }
                />{' '}
                {messages[getLanguage()]['header_conduct_accept']}{' '}
              </label>{' '}
            </div>{' '}
            <div className="col-sm-2">{errors.conduct}</div>{' '}
          </div>{' '}
          <div className="col-md-8 col-md-offset-2 text-left row">
            {' '}
            <Conduct />
          </div>{' '}
          <div className="form-group row">
            <div className="col-sm-12">
              <button type="submit" className="btn btn-primary">
                {' '}
                {messages[getLanguage()]['signup']}{' '}
              </button>{' '}
            </div>{' '}
          </div>{' '}
        </form>{' '}
      </div>{' '}
    </>
  );
};
