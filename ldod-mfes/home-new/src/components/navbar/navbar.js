import headers from './headers.js';
import style from './style.js';
import dropdownMenu from './dropdown-menu.js';
import langMenu from './lang-menu.js';
const ssrLang = 'en';
const ssrConstants = (await import('../../resources/constants-en.js')).default;

function getItems(pages, constants) {
  return pages.filter(Boolean).map((keys) => ({
    name: constants[keys.id] || keys.id,
    ...keys,
  }));
}

const references =
  typeof window !== 'undefined' ? window.references : globalThis.references;

export const navbarHtml = (
  constants = ssrConstants,
  language = ssrLang,
  isAdmin = false
) => {
  const res = /*html*/ `<style>${style}</style>
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
            ${constants['header_title']}
          </a>
          <ul class="nav navbar-nav user-component hidden-xs">
            <li
              class="dropdown"
              is="user-component"
              language=${language}
            ></li>
          </ul>
        </div>
      </div>
    </div>
    <div class="container">
      <div class="navbar-collapse collapse" aria-expanded="false">
        <ul class="nav navbar-nav navbar-nav-flex">
        ${Object.entries(headers(references || {}))
          .map(([key, value]) => {
            if (!value) return;
            const { name, pages } = value;
            return /*html*/ `
            <li
              is="dropdown-menu"
              class="dropdown"
              id=${key}
              language=${language}
              menu-name=${constants[name]}
              aria-hidden=${key === 'admin' && !isAdmin}>
          ${dropdownMenu(constants[name], getItems(pages, constants))}
          </li>`;
          })
          .join('\n')}
        <li
            class="dropdown visible-xs"
            is="user-component"
            language=${language}
          ></li>
          <li is="lang-menu" class="nav-lang" language=${language}>
          ${langMenu}
          </li>
        </ul>
      </div>
    </div>
  </nav>
  `;
  return res;
};
