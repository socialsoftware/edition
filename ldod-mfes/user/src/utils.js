/** @format */

export function setInvalidFor(input, message) {
	const formFloating = input.parentElement;
	const small = formFloating.querySelector('small');
	formFloating.className = 'form-floating invalid';
	small.innerText = message;
}

export function setValidFor(input) {
	const formFloating = input.parentElement;
	formFloating.className = 'form-floating valid';
}

export function capitalizeFirstLetter(word) {
	return word.slice(0, 1).toUpperCase().concat(word.slice(1, word.length));
}

export const loadConstants = async lang =>
	(await import(`./resources/messages-${lang}.js`)).default;

export const isDev = () => import.meta.env.DEV;
