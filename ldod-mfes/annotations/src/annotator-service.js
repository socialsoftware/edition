/** @format */

let instance;

const service = (await import('./annotator')).annotatorService;

class AnnotatorService extends HTMLElement {
	static instance;
	constructor() {
		super();
		if (!AnnotatorService.instance) {
			AnnotatorService.instance = this;
			document.body.appendChild(this);
		}
		return AnnotatorService.instance;
	}

	static observedAttributes = ['data-id', 'data-selector'];

	attributeChangedCallback(name) {
		console.log(name);
		this.annotate();
	}

	annotate({ id, selector } = { id: this.id, selector: this.selector }) {
		service({ id, element: document.querySelector(selector) });
	}

	get id() {
		return this.dataset.id;
	}
	get selector() {
		return this.dataset.selector;
	}
}
customElements.define('annotator-service', AnnotatorService);
