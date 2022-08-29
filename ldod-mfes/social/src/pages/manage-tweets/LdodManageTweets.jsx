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

  getConstant(key, ...args) {
    const constant = constants[this.language][key];
    return args.length ? constant(...args) : constant;
  }
  static get observedAttributes() {
    return ['data', 'language'];
  }

  connectedCallback() {
    this.appendChild(<div id="manageTweetsWrapper"></div>);
    this.render();
  }

  attributeChangedCallback(name, oldV, newV) {
    this.handleChangeAttribute[name](oldV, newV);
  }

  handleChangeAttribute = {
    data: () => {
      this.render();
    },
    language: (oldV, newV) => {
      if (oldV && oldV !== newV) {
        this.querySelectorAll('[data-key]').forEach(
          (node) =>
            (node.firstChild.textContent = node.dataset.args
              ? this.getConstant(
                  node.dataset.key,
                  JSON.parse(node.dataset.args)
                )
              : this.getConstant(node.dataset.key))
        );
      }
    },
  };

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
          title={this.getConstant('title', this.numberOfCitations)}
          numberOfCitations={this.numberOfCitations}
        />
        <Buttons
          generate={this.getConstant('generate')}
          remove={this.getConstant('remove', this.numberOfTweets)}
          node={this}
        />

        <TweetsTable
          node={this}
          tableInfo={this.getConstant('tableInfo', this.getTableInfoArgs())}
          constants={constants}
        />
      </>
    );
  }
}
!customElements.get('ldod-manage-tweets') &&
  customElements.define('ldod-manage-tweets', LdodManageTweets);
