import tooltipStyle from '@src/tooltip.css?inline';
import tooltipHtml from './tooltip-html';
import { createPopper } from './popper.js';

export class LdodDynamicTooltip extends HTMLElement {
  constructor() {
    super();
    this.attachShadow({ mode: 'open' });
    const sheet = new CSSStyleSheet();
    sheet.replaceSync(tooltipStyle);
    this.shadowRoot.adoptedStyleSheets = [sheet];
    this.shadowRoot.innerHTML = tooltipHtml;
  }

  get ref() {
    return this.getAttribute('ref');
  }

  get element() {
    return this.getRootNode().querySelector(this.ref);
  }

  get tooltip() {
    return this.shadowRoot.querySelector('#tooltip');
  }

  get placement() {
    return this.getAttribute('placement') ?? 'bottom';
  }

  isLightTheme() {
    return this.hasAttribute('light');
  }

  setLightTheme() {
    this.tooltip.toggleAttribute('dark', false);
    this.tooltip.toggleAttribute('light', true);
  }

  get options() {
    return {
      placement: this.placement,
      modifiers: [
        {
          name: 'offset',
          options: {
            offset: [0, 8],
          },
        },
      ],
    };
  }

  get popper() {
    return createPopper(this.element, this.tooltip, this.options);
  }

  static get observedAttributes() {
    return ['ref'];
  }

  connectedCallback() {}

  render() {
    this.isLightTheme() && this.setLightTheme();
    this.tooltip.style.maxWidth = this.getAttribute('width');
    this.tooltip.querySelector('#content').textContent = this.dataset.content;
    this.element?.setAttribute('aria-describedby', 'tooltip');
    this.show();
  }

  attributeChangedCallback(name, oldV, newV) {
    this.changedAttribute[name](newV);
  }

  changedAttribute = {
    ref: (newV) => newV && this.onHoverRef(),
  };

  onHoverRef = () => {
    this.instance = this.popper;
    this.element?.addEventListener('pointerleave', this.hide);
    this.render();
  };

  show = () => {
    this.instance.update();
    this.tooltip.toggleAttribute('data-show', true);
  };

  hide = () => {
    this.tooltip.toggleAttribute('data-show', false);
    this.reset();
  };

  reset = () => {
    this.instance?.destroy();
    this.instance = null;
  };
}

!customElements.get('ldod-dynamic-tooltip') &&
  customElements.define('ldod-dynamic-tooltip', LdodDynamicTooltip);
