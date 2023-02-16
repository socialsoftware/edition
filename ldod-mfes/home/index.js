/** @format */

import './src/navbar/ldod-navbar';
import './src/home/home';

document
	.querySelector('ldod-navbar')
	.shadowRoot.querySelectorAll("li[is='lang-menu'] > a")
	.forEach(a => {
		a.addEventListener('click', () =>
			document
				.querySelectorAll('[language]')
				.forEach(ele => ele.setAttribute('language', a.id))
		);
	});
