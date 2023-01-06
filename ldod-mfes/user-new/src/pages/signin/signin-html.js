import constants from '../constants';
import formsStyle from '@shared/bootstrap/forms.js';
import buttonsStyle from '@shared/bootstrap/buttons.js';
import style from './style.css?inline';
export default language => /*html*/ `
    <div>
        <style>${formsStyle}${style}${buttonsStyle}</style>
        <h2 data-user-key="title"  >${constants[language].title}</h2>
        <form id="login-form" class="needs-validation" novalidate>
            <div id="login-form-username" class="form-floating">
                <input class="form-control" name="username" type="text" placeholder="username" required/>
                <label for="login-form-username" data-user-key="username">${constants[language].username}</label>
            </div>
            <div class="input-group">
            <div id="login-form-password" class="form-floating">
                <input class="form-control" name="password" type="password" placeholder="password" required/>
                <label for="login-form-password" data-user-key="password">${constants[language].password}</label>
            </div>
            <button class="btn btn-outline-secondary" type="button">
            <span is="ldod-span-icon" icon="eye"></span>
            </button>
            </div>
          
            <button type="submit" class="btn btn-primary" data-user-key="signin">${constants[language].signin}</button>
        </form>
    </div>

`;
