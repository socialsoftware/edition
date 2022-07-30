import style from './app.css?inline';
import jsLogo from '../assets/javascript.svg';
import wcLogo from '../assets/web-components.svg';

export class AppJS extends HTMLElement {
  constructor() {
    super();
    this.counter = 0;
    const shadow = this.attachShadow({ mode: 'open' });
    shadow.appendChild(<style>{style}</style>);
  }

  get counter() {
    return this.getAttribute('counter');
  }

  set counter(val) {
    this.setAttribute('counter', val);
  }

  static get observedAttributes() {
    return ['counter'];
  }
  connectedCallback() {
    this.shadowRoot.appendChild(this.getComponent());
  }

  getComponent = () => (
    <div>
      <a
        href="https://developer.mozilla.org/en-US/docs/Web/Web_Components"
        target="_blank">
        <img
          src={wcLogo}
          class="logo web-components"
          alt="web-components logo"
        />
      </a>
      <a
        href="https://developer.mozilla.org/en-US/docs/Web/JavaScript"
        target="_blank">
        <img src={jsLogo} class="logo vanilla" alt="JavaScript logo" />
      </a>
      <h1>Hello Web Components + Vanilla JS!</h1>
      <div class="card">
        <button id="counter" type="button" onClick={this.increaseCounter}>
          {this.counter}
        </button>
      </div>
    </div>
  );

  attributeChangedCallback(name, oldV, newV) {
    if (name === 'counter' && oldV !== newV && oldV)
      this.shadowRoot.querySelector('button#counter').textContent =
        this.counter;
  }

  increaseCounter = () => this.counter++;

  disconnectedCallback() {}
}
!customElements.get('app-js') && customElements.define('app-js', AppJS);
