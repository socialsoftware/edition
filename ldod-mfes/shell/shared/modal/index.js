/** @format */

import './src/ldod-bs-modal/modal-bs';
import './src/ldod-modal/modal';
import '../dist/tooltip.js';

document
	.getElementById('toggle')
	.addEventListener('click', e =>
		document.querySelector('ldod-bs-modal').toggleAttribute('show')
	);

console.log(document.querySelector('ldod-tooltip').element);
