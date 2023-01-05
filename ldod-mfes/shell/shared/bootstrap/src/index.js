if (typeof window !== 'undefined') {
	window.addEventListener(
		'pointermove',
		() => {
			['root', 'buttons', 'forms'].forEach(href => {
				const link = document.createElement('link');
				link.rel = 'stylesheet';
				link.href = `dist/bootstrap/${href}.css`;
				document.head.appendChild(link);
			});
		},
		{ once: true }
	);
}

const form = document.querySelector('form');

/**
 *
 * @param {Event} e
 */

const onsubmit = e => {
	e.preventDefault();
	e.stopPropagation();
	const isValid = form.checkValidity();
	form.classList.add('was-validated');
	if (isValid) console.log(Object.fromEntries(new FormData(form)));
};

form.addEventListener('submit', onsubmit, false);
