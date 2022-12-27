import style from './style.css?inline';
import { createPopper } from '@src/popper.js';
import { annotationsList } from '../../ldod-annotations';
import { Annotation } from '../../annotation';
import { describeTextPosition } from '@apache-annotator/dom_0.3.0-dev.23';
import { fromNode } from 'simple-xpath-position';

const getBoundingClientRect = (x = 0, y = 0) => ({
  width: 0,
  height: 0,
  top: y + 15,
  left: x,
});

export class NewAnnPopover extends HTMLElement {
  constructor(refNode, annotationNode, interId) {
    super();
    this.hidden = true;
    this.refNode = refNode;
    this.ldodAnnotation = annotationNode;
    this.interId = interId;
  }

  static get observedAttributes() {
    return ['hidden'];
  }

  connectedCallback() {
    this.render();
    this.initPopper();
    this.addNewAnnListener();
  }

  initPopper = () => {
    this.virtualElement = {
      getBoundingClientRect: () => getBoundingClientRect(),
      contextElement: this.refNode,
    };
    this.popper = createPopper(this.virtualElement, this);
  };

  render() {
    this.appendChild(
      <div id="new-annotation-popover">
        <style>{style}</style>
        <button id="new-annotation-btn" onClick={this.onNew}>
          <span class="annotation-icon annotation-icon-comment"></span>
          <span>New</span>
        </button>
      </div>
    );
  }

  attributeChangedCallback(name, oldV, newV) {
    this.onChangeAttribute[name]();
  }

  onChangeAttribute = {
    hidden: () => {
      !this.hidden && this.addHiddenListener();
    },
  };

  hidePopover = () => {
    this.hidden = true;
  };

  updatePopper = (e) => {
    this.virtualElement.getBoundingClientRect = () =>
      getBoundingClientRect(e.clientX, e.clientY);
    this.popper.update();
    this.hidden = false;
  };

  addHiddenListener = () => {
    document.addEventListener('click', this.hidePopover, {
      once: true,
    });
  };

  removeHiddenListener = () => {
    document.removeEventListener('click', this.hidePopover, {
      once: true,
    });
  };

  addNewAnnListener = () => {
    this.refNode.addEventListener('click', this.handleNewAnnotation);
  };

  removeAnnListener = () => {
    this.refNode.removeEventListener('click', this.handleNewAnnotation);
  };

  handleNewAnnotation = async (e) => {
    e.stopPropagation();
    const selection = document.getSelection();

    if (selection.type !== 'Range' || selection.isCollapsed)
      return this.hidePopover();

    const currentQuote = selection.toString();

    if (currentQuote === this.quote) return this.hidePopover();
    this.quote = currentQuote;

    const range = selection.getRangeAt(0);
    let scope = range.commonAncestorContainer;

    while (!scope.tagName) scope = scope.parentElement;
    const selector = await describeTextPosition(range, scope);
    const start = fromNode(scope, this.refNode);
    const end = start;

    this.selectionXPath = {
      start,
      end,
      startOffset: selector.start,
      endOffset: selector.end,
    };

    this.updatePopper(e);
  };

  disconnectedCallback() {
    this.removeAnnListener();
    this.removeHiddenListener();
  }

  onNew = async (e) => {
    const id = crypto.randomUUID();
    const newAnn = await new Annotation(
      {
        quote: this.quote,
        ranges: [this.selectionXPath],
        externalId: id,
      },
      this.refNode
    )
      .highlight()
      .catch((error) => console.error(error));

    annotationsList.push(newAnn);

    this.virtualElement.getBoundingClientRect = () =>
      getBoundingClientRect(e.clientX, e.clientY);
    this.ldodAnnotation.showOnCreation(this.virtualElement, newAnn);
  };

  isSelectionPathEqual = (otherSelectionPath) => {
    if (!this.selectionXPath) return false;
    return Object.keys(this.selectionXPath).every(
      (key) => this.selectionXPath[key] === otherSelectionPath[key]
    );
  };
}

!customElements.get('new-ann-popover') &&
  customElements.define('new-ann-popover', NewAnnPopover);
