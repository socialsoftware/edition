/** @format */

export const navbarHtml = root => /*html*/ `
<nav
  role="navigation"
  class="ldod-navbar navbar-default navbar navbar-fixed-top"
>
  <div class="container-fluid">
    <div class="container">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a
          is="nav-to"
          id="header_title"
          to="/"
          class="navbar-brand update-language"
        >
          ${root.constants['header_title']}
        </a>
        <ul class="nav navbar-nav user-component hidden-xs">
          <li
            class="dropdown"
            is="user-component"
            language=${root.language}
          ></li>
        </ul>
      </div>
    </div>
  </div>
  <div class="container">
    <div class="navbar-collapse collapse" aria-expanded="false">
      <ul class="nav navbar-nav navbar-nav-flex">
        <li
          class="dropdown visible-xs"
          is="user-component"
          language=${root.language}
        ></li>
        <li is="lang-menu" class="nav-lang" language=${root.language}></li>
      </ul>
    </div>
  </div>
</nav>
`;
