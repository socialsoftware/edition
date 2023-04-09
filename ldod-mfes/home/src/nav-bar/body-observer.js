/** @format */

const observable = document.body;

customElements.whenDefined('nav-bar').then(({ instance }) => {
	const navElement = instance.shadowRoot.firstElementChild;
	new MutationObserver(mutations => {
		mutations.forEach(mutation => {
			if (observable.scrollHeight <= window.innerHeight) return;
			if (mutation.target.classList.contains('modal-open'))
				navElement.style.paddingRight = '15px';
			else navElement.style.paddingRight = '0px';
		});
	}).observe(observable, {
		attributes: true,
		attributeFilter: ['class'],
	});
});
