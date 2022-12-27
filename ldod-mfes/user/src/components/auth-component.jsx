import { userFullName } from '../store';
import { userReferences } from '../user-references';

export const AuthComponent = ({ root }) => (
  <>
    <a id="loggedIn" class="dropdown-toggle">
      {userFullName()}
      <span class="caret"></span>
    </a>
    <ul class="dropdown-menu">
      <li>
        <a class="update-language" id="logout" onClick={root.logoutHandler}></a>
      </li>
      <li>
        <a
          is="nav-to"
          class="update-language"
          id="change-password"
          to={userReferences.password()}></a>
      </li>
    </ul>
  </>
);
