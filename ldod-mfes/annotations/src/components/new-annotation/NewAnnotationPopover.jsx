import style from './style.css?inline';
import { createPopper } from 'shared/popper.js';
import { fromRange } from 'xpath-range';
import { annotationsList, processExistingAnnotations } from '../../annotator';
import { Annotation } from '../../Annotation';

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

    // normalize parent node
    annotationsList.forEach((ann) => ann.destroy());
    range.commonAncestorContainer.parentNode.normalize();
    this.selectionXPath = fromRange(range, this.refNode);

    // rerender annotations
    processExistingAnnotations(annotationsList);

    this.updatePopper(e);
  };

  disconnectedCallback() {
    this.removeAnnListener();
    this.removeHiddenListener();
  }

  removeTextFromXPath = () => {
    const splitStart = this.selectionXPath.start.split('/');
    if (!splitStart.at(splitStart.length - 1).startsWith('text')) return;
    this.selectionXPath.start = splitStart
      .splice(0, splitStart.length - 1)
      .join('/');

    const endSplit = this.selectionXPath.end.split('/');
    if (!endSplit.at(endSplit.length - 1).startsWith('text')) return;
    this.selectionXPath.end = endSplit.splice(0, endSplit.length - 1).join('/');
  };

  onNew = async (e) => {
    const id = Date.now().toString();
    this.removeTextFromXPath();
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
