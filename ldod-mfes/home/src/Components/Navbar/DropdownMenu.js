import { parseHTML } from 'shared/utils.js';

class DropdownMenu extends HTMLLIElement {
  static get observedAttributes() {
    return ['language'];
  }

  get menuName() {
    return this.getAttribute('menu-name');
  }

  set menuName(name) {
    this.setAttribute('menu-name', name);
  }

  connectedCallback() {
    this.render();
  }
  attributeChangedCallback(name, oldValue, newValue) {
    if (oldValue === newValue || !oldValue) return;
    this.setDropDownMenuName();
    this.setAnchorItemsNames();
  }

  disconnectedCallback() {}

  render() {
    this.classList.add('dropdown');
    this.innerHTML = html`
      <a class="dropdown-toggle" data-toggle="dropdown"
        >${this.menuName}<span class="caret"></span>
      </a>
      <ul class="dropdown-menu">
        <div class="dropdown-menu-bg"></div>
        ${this.items?.reduce(
          (prev, { id, route, link, name, clazz, selected }) =>
            prev.concat(html`
              <li
                ${clazz ? `class=${clazz}` : ''}
                ${selected ? 'default-selected' : ''}
              >
                ${route
                  ? html`<a is="nav-to" to=${route} id=${id ?? ''}
                      >${name ?? ''}</a
                    >`
                  : link
                  ? html`<a href=${link} target="_blank" id=${id}>${name}</a>`
                  : ''}
              </li>
            `),
          ''
        )}
      </ul>
    `;
  }

  addSelectedEditions(editions) {
    editions.forEach((edition) => {
      this.querySelector('ul.dropdown-menu').appendChild(
        parseHTML(html`
          <li selected>
            <a
              is="nav-to"
              to=${`/virtual/edition/acronym/${edition}`}
              id=${edition.toLowerCase()}
              >${edition}</a
            >
          </li>
        `)
      );
    });
  }

  removeSelectedEditions() {
    if (this.id !== 'editions') return;
    this.querySelectorAll('li[selected]').forEach((node) => node.remove());
  }

  setDropDownMenuName() {
    const anchor = this.querySelector('li.dropdown>a');
    const span = anchor.querySelector('span');
    anchor.textContent = this.menuName;
    anchor.appendChild(span);
  }

  setAnchorItemsNames() {
    this.items?.forEach(({ id, name }) => {
      this.querySelectorAll(`li>a#${id}`).forEach(
        (anchor) => (anchor.textContent = name)
      );
    });
  }

  getDrops() {
    return this.getRootNode().querySelectorAll('.dropdown');
  }
}

customElements.define('dropdown-menu', DropdownMenu, { extends: 'li' });
