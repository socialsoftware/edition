/** @format */

const observable = document.body;
let navElement;
const observer = new MutationObserver(mutations => {
	mutations.forEach(mutation => {
		if (!navElement)
			navElement = document.body.querySelector('ldod-navbar').shadowRoot.firstElementChild;
		if (observable.scrollHeight <= window.innerHeight) return;
		if (mutation.target.classList.contains('modal-open'))
			navElement.style.paddingRight = '15px';
		else navElement.style.paddingRight = '0px';
	});
});
observer.observe(observable, {
	attributes: true,
	attributeFilter: ['class'],
});
