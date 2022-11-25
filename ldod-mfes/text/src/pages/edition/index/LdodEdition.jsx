import constants, { editions } from '../resources/constants.js';
import style from '../style.css?inline';

export class LdodEdition extends HTMLElement {
  constructor() {
    super();
  }

  get language() {
    return this.getAttribute('language');
  }
  get wrapper() {
    return this.querySelector('div#ldodEditionWrapper');
  }

  static get observedAttributes() {
    return ['language'];
  }

  getConstants(key) {
    return constants[this.language][key];
  }

  connectedCallback() {
    this.appendChild(<style>{style}</style>);
    this.appendChild(<div id="ldodEditionWrapper"></div>);
    this.render();
  }

  getImageUrl = (filename) => {
    const url = new URL(
      `../resources/images/${filename}.webp`,
      import.meta.url
    );
    return url.href;
  };

  render() {
    this.wrapper.innerHTML = '';
    this.wrapper.appendChild(
      <>
        <h3 class="text-center" data-key="editions">
          {this.getConstants('editions')}
        </h3>
        <p>&nbsp;</p>
        {editions.map(({ padding, filename, path }) => (
          <a is="nav-to" to={path} class="ldod-default">
            <div class="div-link">
              <img
                src={this.getImageUrl(filename)}
                width="100%"
                style={{ paddingBottom: padding }}
              />
              <img
                src={this.getImageUrl(`${filename}H`)}
                width="100%"
                style={{ paddingBottom: padding }}
              />
            </div>
          </a>
        ))}
      </>
    );
  }

  attributeChangedCallback(name, oldV, newV) {
    this.handeChangedAttribute[name](oldV, newV);
  }

  handeChangedAttribute = {
    language: (oldV, newV) => {
      if (oldV && oldV !== newV) this.handleChangedLanguage();
    },
  };

  handleChangedLanguage = () => {
    this.querySelectorAll('[data-key]').forEach((node) => {
      return (node.firstChild.textContent = this.getConstants(
        node.dataset.key
      ));
    });
  };
}
!customElements.get('ldod-edition') &&
  customElements.define('ldod-edition', LdodEdition);
