import Buttons from './components/Buttons.jsx';
import Title from './components/Title.jsx';
import TweetsTable from './components/TweetsTable.jsx';
import constants from './constants.js';
import.meta.env.DEV
  ? await import('shared/table-dev.js')
  : await import('shared/table.js');

export class LdodManageTweets extends HTMLElement {
  constructor() {
    super();
  }

  get language() {
    return this.getAttribute('language');
  }

  get numberOfCitations() {
    return this.tweets?.twitterCitations.length;
  }

  get numberOfCitationsWithInfoRanges() {
    return this.tweets?.numberOfCitationsWithInfoRanges;
  }

  get numberOfTweets() {
    return this.tweets?.tweetsSize;
  }

  get wrapper() {
    return this.querySelector('div#manageTweetsWrapper');
  }

  getConstants(key, ...args) {
    const constant = constants[this.language][key];
    return args.length ? constant(...args) : constant;
  }
  static get observedAttributes() {
    return ['data', 'language'];
  }

  connectedCallback() {
    this.appendChild(<div id="manageTweetsWrapper"></div>);
    this.render();
    this.addEventListeners();
  }

  attributeChangedCallback(name, oldV, newV) {
    this.handleChangeAttribute[name](oldV, newV);
  }

  handleChangeAttribute = {
    data: () => this.render(),
    language: (oldV, newV) =>
      oldV && oldV !== newV && this.handleChangedLanguage(),
  };

  addEventListeners = () => {
    this.addEventListener('ldod-table-searched', this.updateTitle);
  };

  updateTitle = ({ detail }) => {
    this.querySelector('h3#title').firstChild.textContent = this.getConstants(
      'title',
      detail.size
    );
  };

  handleChangedLanguage() {
    this.querySelectorAll('[data-key]').forEach(
      (node) =>
        (node.firstChild.textContent = node.dataset.args
          ? this.getConstants(node.dataset.key, JSON.parse(node.dataset.args))
          : this.getConstants(node.dataset.key))
    );
  }

  getTableInfoArgs() {
    const cits = this.numberOfCitations;
    const ranges = this.numberOfCitationsWithInfoRanges;
    return { cits, ranges };
  }

  render() {
    if (!this.tweets) return;
    this.wrapper.innerHTML = '';
    this.wrapper.appendChild(
      <>
        <Title
          title={this.getConstants('title', this.numberOfCitations)}
          numberOfCitations={this.numberOfCitations}
        />
        <Buttons
          generate={this.getConstants('generate')}
          remove={this.getConstants('remove', this.numberOfTweets)}
          node={this}
        />

        <TweetsTable
          node={this}
          tableInfo={this.getConstants('tableInfo', this.getTableInfoArgs())}
          constants={constants}
        />
      </>
    );
  }
}
!customElements.get('ldod-manage-tweets') &&
  customElements.define('ldod-manage-tweets', LdodManageTweets);
