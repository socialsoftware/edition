import { userReferences } from '../user-references';
import constants from './constants';

export default (name, language) => /*html*/ `
  <a id="user-logged-in" class="dropdown-toggle">
    ${name}
    <span class="caret"></span>
  </a>
  <ul class="dropdown-menu">
    <li>
      <a  id="user-logout" data-user-key="logout">${constants[language].logout}</a>
    </li>
    <li>
      <a
        is="nav-to"
        data-user-key="changePassword"
        to="${userReferences.password()}">
        ${constants[language].changePassword}
        </a>
    </li>
  </ul>
`;
