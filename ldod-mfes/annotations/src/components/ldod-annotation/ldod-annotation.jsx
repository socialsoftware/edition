/** @format */

import { createAnnotation, deleteAnnotation, updateAnnotation } from '../../api-requests';
import { errorPublisher } from '../../events-module';
import { annotationsList, mutateAnnotationsList } from '../../ldod-annotations';
import annotationHtml from './annotation-html';
import { annotationCats, multipleSelectHTML } from './multiple-select-html';
import style from './style.css?inline';
import snowStyle from 'quill_1.3.7/dist/quill.snow.css?inline';

let createPopper;
let quill;
let multipleSelect;

const loadModules = async () => {
	if (!multipleSelect) multipleSelect = (await import('@src/select-pure')).selectPure;
	if (!createPopper) createPopper = (await import('@src/popper.js')).createPopper;
	if (!quill) quill = (await import('quill_1.3.7/dist/quill.min.js')).default;
};

export class LdodAnnotation extends HTMLElement {
	constructor(interId, categories, openVocab) {
		super();
		this.interId = interId;
		this.categories = categories;
		this.openVocab = openVocab;
	}

	get isReadable() {
		return this.annotation?.data.canBeRead;
	}

	get isEditable() {
		return this.annotation?.data.canBeUpdated;
	}

	get canBeDeleted() {
		return this.annotation?.data.canBeDeleted;
	}

	get contentEditor() {
		return this.querySelector('div#content');
	}

	get id() {
		return this.annotation?.id;
	}

	get tagMultipleSelect() {
		return this.querySelector('select-pure');
	}

	static get observedAttributes() {
		return ['updated'];
	}

	clearTimeout = () => {
		window.clearTimeout(this.timeout);
		this.timeout = null;
	};

	connectedCallback() {
		this.hidden = true;
		this.innerHTML = /*html*/ `
			<style>
				${style}
				${snowStyle}
			</style>`;
	}

	attributeChangedCallback(name) {
		this.onChangedAtttribute[name]();
	}

	onChangedAtttribute = {
		updated: () => this.onHoverAnnotation(),
	};

	handleHover(ref, annotation) {
		this.ref = ref;
		this.annotation = annotation;
		this.toggleAttribute('updated', true);
	}

	onHoverAnnotation = async () => {
		await loadModules();
		if (!this.hasAttribute('updated')) return;
		this.toggleAttribute('updated', false);
		if (this.annotation.data.human && !this.isReadable) return;
		this.render();
		this.annotation.data.human && this.initEditor();
		this.initPopper();
	};

	render() {
		this.querySelector('div.comment')?.remove();
		this.appendChild(annotationHtml(this));
	}

	show = () => {
		this.addPopperEvents();
		this.hidden = false;
		this.popper.update();
		this.clearTimeout();
	};

	hide = () => {
		this.timeout = setTimeout(() => {
			this.timeout && this.hideNow();
		}, 2000);
	};

	hideNow = () => {
		this.removeAnnotationEvents();
		this.on = false;
		this.hidden = true;
	};

	onClose = e => {
		e.stopPropagation();
		this.hideNow();
	};

	initPopper = (ref = this.ref) => {
		this.hideNow();
		this.popper && this.popper.destroy();
		this.popper = createPopper(ref, this);
		this.show();
	};

	addPopperEvents = () => {
		this.addAnnotationEvents();
		this.addAnnotationRefEvents();
	};

	addAnnotationEvents = () => {
		this.addEventListener('pointerenter', this.onPointerInteraction);
		this.contentEditor.addEventListener('focusin', this.onKeyboardInteraction);
		this.addEventListener('pointerleave', this.onPointerInteraction);
		this.contentEditor.addEventListener('focusout', this.onKeyboardInteraction);
	};

	removeAnnotationEvents = () => {
		this.removeEventListener('pointerenter', this.onPointerInteraction);
		this.contentEditor.removeEventListener('focusin', this.onKeyboardInteraction);
		this.removeEventListener('pointerleave', this.onPointerInteraction);
		this.contentEditor.removeEventListener('focusout', this.onKeyboardInteraction);
	};

	addAnnotationRefEvents = () => {
		this.ref?.addEventListener('pointerleave', this.onPointerInteraction);
	};

	removeAnnotationRefEvents = () => {
		this.ref?.removeEventListener('pointerleave', this.onPointerInteraction);
	};

	onPointerInteraction = e => {
		this.hasPointerInteraction = e.type === 'pointerenter';
		this.handleInteraction();
	};

	onKeyboardInteraction = e => {
		this.hasKeyboardInteraction = e.type === 'focusin';
		this.handleInteraction();
	};

	handleInteraction = () => {
		this[this.hasKeyboardInteraction || this.hasPointerInteraction ? 'show' : 'hide']();
	};

	initEditor = () => {
		this.editor = new quill(this.contentEditor, {
			theme: 'snow',
			modules: { toolbar: [['bold', 'italic', 'underline', 'link', 'clean']] },
			placeholder: 'Notes ...',
		});
		let contents = this.annotation?.data.contents;

		contents
			? this.editor.setContents(JSON.parse(decodeURI(contents)))
			: this.editor.setText(this.annotation?.data.text || '');
		if (!this.isEditable) {
			this.editor.disable();
			this.annotation?.data.tagList.length &&
				this.contentEditor.appendChild(annotationCats(this));
			return;
		}

		this.contentEditor.appendChild(multipleSelectHTML(this));

		this.addEditorEvents();
		this.addTagSelectEvent();
	};

	addEditorEvents = () => {
		this.editor.root.onblur = () => {
			this.annotation.data.text = this.editor.getText();
			this.annotation.data.contents = encodeURI(JSON.stringify(this.editor.getContents()));
		};
	};

	addTagSelectEvent = () => {
		this.tagMultipleSelect.addEventListener('change', () => {
			const selected = Array.from(
				this.tagMultipleSelect.querySelectorAll('option-pure[selected]')
			).map(opt => opt.getAttribute('value'));
			this.annotation.data.tagList = selected;
		});
	};

	showOnCreation = async (virtualElement, ann) => {
		await loadModules();
		this.annotation = ann;
		this.render();
		this.initEditor();
		this.initPopper(virtualElement);
		this.removeAnnotationEvents();
		this.hidden = false;
	};

	// API requests

	onSuccess = data => {
		this.dispatchAnnotationEvent();
	};

	onError = error => {
		errorPublisher(error.message);
		console.error(error);
	};

	onSave = async () => {
		if (this.annotation.data.uri) await this.updateAnnotation(this.annotation);
		else
			await createAnnotation(this.interId, this.annotation.data)
				.then(this.onSuccess)
				.catch(this.onError);
	};

	updateAnnotation = async annotation => {
		await updateAnnotation(annotation.id, annotation.data)
			.then(this.onSuccess)
			.catch(this.onError);
	};

	onRemove = () => {
		if (!this.annotation.data.uri) {
			this.removeNotPersisted();
			return;
		}
		deleteAnnotation(this.interId, this.annotation.id).then(this.onSuccess).catch(this.onError);
	};

	removeNotPersisted() {
		this.annotation.destroy();
		this.hideNow();
		mutateAnnotationsList(annotationsList.filter(ann => ann.id !== this.annotation.id));
	}

	dispatchAnnotationEvent = () => {
		this.dispatchEvent(
			new CustomEvent('annotator:annotation-update', {
				bubbles: true,
				composed: true,
			})
		);
	};
}

!customElements.get('ldod-annotation') && customElements.define('ldod-annotation', LdodAnnotation);
