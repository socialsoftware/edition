import constants from '../../constants';
import RecommendationModal from '../components/recommendation-modal/RecommendationModal';
import style from '../style.css?inline';
import { navigateTo } from 'shared/router.js';
import { readingReferences } from '../../reading';

const loadPopper = () =>
  import.meta.env.DEV
    ? import('shared/tooltip.dev.js')
    : import('shared/tooltip.js');

export class LdodReading extends HTMLElement {
  constructor() {
    super();
    this.constants = constants;
  }

  get language() {
    return this.getAttribute('language');
  }

  get wrapper() {
    return this.querySelector('#reading-wrapper');
  }

  get recommendationModal() {
    return this.querySelector('ldod-modal#reading-recommendationModal');
  }

  static get observedAttributes() {
    return ['language'];
  }

  getConstants(key) {
    return this.constants[this.language][key];
  }

  connectedCallback() {
    this.appendChild(<style>{style}</style>);
    this.appendChild(<div id="reading-wrapper"></div>);
    if (this.expertEditions) this.render();
    this.addEventListeners();
  }

  render() {
    this.wrapper.innerHTML = '';
    this.wrapper.appendChild(
      <>
        <div class="reading-grid">
          <div
            class="reading-text reading-book-title"
            style={{ gridColumn: '1/8' }}>
            <h1 data-reading-key="title">{this.getConstants('title')}</h1>
          </div>
          {this.expertEditions.map((edition) => {
            return (
              <div class="reading-column">
                <h4>
                  <a
                    is="nav-to"
                    to={readingReferences.editionInterPath(
                      edition.xmlId,
                      edition.urlId
                    )}>
                    {edition.editor}
                  </a>
                </h4>
                <span
                  class="icon icon-arrow-right"
                  onClick={() =>
                    navigateTo(
                      readingReferences.editionInterPath(
                        edition.xmlId,
                        edition.urlId
                      ),
                      this
                    )
                  }></span>
              </div>
            );
          })}
          <div class="reading-column">
            <span
              id="reading-recommendation"
              data-reading-key="recommendation"
              onClick={this.onRecommendation}>
              {this.getConstants('recommendation')}
            </span>
            <span
              id="reading-recommendationTooltip"
              class="icon icon-info"></span>
          </div>
        </div>

        <RecommendationModal root={this} />
      </>
    );
  }

  updateData(data) {
    this.expertEditions = data;
    this.render();
  }

  attributeChangedCallback(name, oldV, newV) {
    this.onChangeAttribute[name]();
  }

  onChangeAttribute = {
    language: () => this.onlanguageChange(),
  };

  onlanguageChange = () => {
    this.querySelectorAll('[data-reading-key]').forEach((element) => {
      element.firstChild.textContent = this.getConstants(
        element.dataset.readingKey
      );
    });
    this.querySelectorAll('[data-reading-tooltip-key]').forEach((tooltip) => {
      tooltip.setAttribute(
        'content',
        this.getConstants(tooltip.dataset.readingTooltipKey)
      );
    });
  };

  onRecommendation = async () => {
    this.recommendationModal.toggleAttribute('show', true);
  };

  onRecommendationSubmit = () => {
    this.recommendationModal.toggleAttribute('show');
  };

  addEventListeners = () => {
    this.wrapper.addEventListener('pointerenter', loadPopper);
  };
}
!customElements.get('ldod-reading') &&
  customElements.define('ldod-reading', LdodReading);
