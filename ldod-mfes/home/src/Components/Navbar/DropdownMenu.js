class DropdownMenu extends HTMLLIElement {
  static get observedAttributes() {
    return ['language'];
  }

  get menuItems() {
    return this.items;
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
        ${this.menuItems.reduce(
          (prev, { id, route, link, name }) =>
            prev.concat(html`
              <li>
                ${route
                  ? html`<a is="nav-to" to=${route} id=${id}>${name}</a>`
                  : html`<a href=${link} target="_blank" id=${id}>${name}</a>`}
              </li>
            `),
          ''
        )}
      </ul>
    `;
  }

  setDropDownMenuName() {
    const anchor = this.querySelector('li.dropdown>a');
    const span = anchor.querySelector('span');
    anchor.textContent = this.menuName;
    anchor.appendChild(span);
  }

  setAnchorItemsNames() {
    this.menuItems.forEach(({ id, name }) =>
      this.querySelectorAll(`li>a#${id}`).forEach(
        (anchor) => (anchor.textContent = name)
      )
    );
  }

  getDrops() {
    return this.getRootNode().querySelectorAll('.dropdown');
  }
}

customElements.define('dropdown-menu', DropdownMenu, { extends: 'li' });
