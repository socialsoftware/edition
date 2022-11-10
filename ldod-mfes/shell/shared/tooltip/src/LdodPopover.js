import { dom } from '../../dist/utils.js';
import popoverStyle from './popover.css?inline';
import { createPopper } from 'shared/popper.js';
window.html = String.raw;

export class LdodPopover extends HTMLElement {
  constructor() {
    super();
  }

  get show() {
    return this.hasAttribute('show');
  }

  get content() {
    return this.querySelector('[popover]');
  }

  static get observedAttributes() {
    return ['show'];
  }

  get options() {
    return {
      placement: 'bottom',
      modifiers: [
        {
          name: 'offset',
          options: {
            offset: [this.dataset.skidding || 0, this.dataset.distance || 8],
          },
        },
      ],
    };
  }

  getComponent = () => {
    const element = this.element();
    element.lastElementChild.toggleAttribute('popover');
    return element.children;
  };

  connectedCallback() {
    this.appendChild(getStyle());
    window.addEventListener('click', this.hidePopover);
  }

  hidePopover = ({ target }) => {
    if (target.hasAttribute('open-popover')) return;
    this.toggleAttribute('show', false);
  };

  attributeChangedCallback(name, oldV, newV) {
    this.handleChangeAttribute[name]();
  }

  handleChangeAttribute = {
    show: () => {
      if (!this.show) return this.content.remove();
      this.append(...this.getComponent());
      this.content.toggleAttribute('show');
      this.update();
    },
  };

  disconnectedCallback() {
    window.removeEventListener('click', this.hidePopover);
  }

  update = () => {
    this.target &&
      createPopper(this.target, this.content, this.options).update();
  };
}

!customElements.get('ldod-popover') &&
  customElements.define('ldod-popover', LdodPopover);

function getStyle() {
  return dom(
    html`<style>
      ${popoverStyle}
    </style>`
  );
}
