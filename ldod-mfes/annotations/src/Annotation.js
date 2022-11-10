import { highlightText } from '@apache-annotator/dom';
import { toRange } from 'xpath-range';
import { ldodAnnotationComponent } from './annotator';

const defaultAnnotation = {
  human: true,
  canBeRead: true,
  canBeUpdated: true,
  text: '',
  contents: '',
  tagList: [],
};

export class Annotation {
  constructor(data, refNode) {
    this.data = { ...defaultAnnotation, ...data };
    this.id = data.externalId;
    this.xPathRange = data.ranges[0];
    this.refNode = refNode;
  }

  get markNodes() {
    return Array.from(
      this.refNode.querySelectorAll(`mark[id="${this.id}"]`)
    ).filter((node) => node.innerHTML.trim());
  }

  toRange() {
    this.range = toRange(
      this.xPathRange.start,
      this.xPathRange.startOffset,
      this.xPathRange.end,
      this.xPathRange.endOffset,
      this.refNode
    );
  }

  highlight = () => {
    return new Promise((resolve) => {
      this.toRange();
      this.destroy = highlightText(this.range, 'mark', {
        id: this.id,
      });
      this.setMarkHandlers();
      return resolve(this);
    });
  };

  destroyAndnormalizeCommonAncestor() {
    this.destroy();
    this.range.commonAncestorContainer.normalize();
  }

  setMarkHandlers() {
    this.addHoverListener();
  }

  addHoverListener() {
    this.markNodes.forEach((node) => {
      node.addEventListener('pointerenter', this.addHoverHandler);
    });
  }

  addHoverHandler = ({ target }) => {
    ldodAnnotationComponent.handleHover(target, this);
  };
}
