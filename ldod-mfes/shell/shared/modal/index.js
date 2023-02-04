/** @format */

import './src/ldod-bs-modal/modal-bs';
import './src/ldod-modal/modal';

document
	.getElementById('toggle')
	.addEventListener('click', e =>
		document.querySelector('ldod-bs-modal').toggleAttribute('show')
	);
