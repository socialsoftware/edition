/** @format */

import { hideHomeInfo, showHomeInfo } from '@src/home-info';
const title = {
	en: 'Contact Us',
	es: 'Contacto',
	pt: 'Contacto',
};

const emailAnchor = <a href="mailto:arquivo.ldod@fl.uc.pt"> arquivo.ldod@fl.uc.pt</a>;

const component = {
	en: 'If you have any questions about the LdoD Archive, please contact us',
	es: 'Si tiene alguna pregunta acerca del Archivo LdoD, por favor, contacte connosotros',
	pt: 'Para qualquer assunto relativamente ao Arquivo LdoD contacte-nos para',
};

export class LdodContact extends HTMLElement {
	constructor() {
		super();
	}

	get language() {
		return this.getAttribute('language');
	}

	static get observedAttributes() {
		return ['language'];
	}

	connectedCallback() {
		this.appendChild(this.wrapper());
		this.render();
		showHomeInfo();
	}

	attributeChangedCallback(name, oldV, newV) {
		this.handlers[name](oldV, newV);
	}

	disconnectedCallback() {
		hideHomeInfo();
	}

	handlers = {
		language: (oldV, newV) => {
			if (!oldV || oldV === newV) return;
			this.render();
		},
	};

	getTitle() {
		return (
			<>
				<h1 class="text-center">{title[this.language]}</h1>
				<p>&nbsp;</p>
			</>
		);
	}

	getComponent() {
		return (
			<>
				<p>
					{component[this.language]}
					{emailAnchor}
				</p>
				<strong>&nbsp;</strong>
			</>
		);
	}

	wrapper() {
		return <div id="about-wrapper" class="ldod-about"></div>;
	}

	render() {
		const wrapper = this.querySelector('#about-wrapper');
		wrapper.appendChild(
			<div>
				{this.getTitle()}
				{this.getComponent(this.language)}
			</div>
		);
		wrapper.childNodes.length > 1 && wrapper.firstChild.remove();
	}
}
!customElements.get('ldod-contact') && customElements.define('ldod-contact', LdodContact);
