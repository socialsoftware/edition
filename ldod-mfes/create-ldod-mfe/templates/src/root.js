/** @format */

import helloWorld from './page/hello-world';

let container;

function mount(lang, ref) {
	container = document.querySelector(ref).appendChild(helloWorld(lang));
}
function unMount() {
	container?.remove();
}

export { mount, unMount };
