/** @format */

import { userReferences } from '../user-references';
import constants from './constants';

export default language => /* html */ `
  <a
    data-user-key="login"
    is="nav-to"
    id="login"
    key="user"
    class="dropdown-item nav-link"
    to="${userReferences.signin()}">
    ${constants[language].login}
  </a>
`;
