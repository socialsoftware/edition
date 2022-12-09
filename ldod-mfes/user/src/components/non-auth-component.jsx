import { userReferences } from "../user-references";

export const NonAuthComponent = () => (
  <a
    is="nav-to"
    id="login"
    class="login update-language"
    to={userReferences.signin()}></a>
);