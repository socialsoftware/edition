import { parseHTML } from 'shared/utils.js';
import { virtualReferences } from '../../external-deps.js';
import dropdownMenu from './dropdown-menu.js';

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
    !this.hasChildNodes && this.render();
  }
  attributeChangedCallback(name, oldValue, newValue) {
    if (oldValue === newValue || !oldValue) return;
    this.setDropDownMenuName();
    this.setAnchorItemsNames();
  }

  render() {
    this.innerHTML = dropdownMenu(this.menuName, this.items);
  }

  addSelectedEditions(editions) {
    editions.forEach((edition) => {
      this.querySelector('ul.dropdown-menu').appendChild(
        parseHTML(html`
          <li selected>
            <a
              is="nav-to"
              to="${virtualReferences?.virtualEdition?.(edition) || ''}"
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
