/** @format */

import { ldodEventSubscriber } from '@core';

const message = {
	pt: 'LdoD: Esta página não existe...',
	en: 'LdoD: Page not found...',
	es: 'LdoD: Esta página no existe',
};

const getNoPage = () => document.body.querySelector('div#no-page.container');

const NoPage = language =>
	/*html*/ `<div id="no-page" class="container">${message[language]}</div>`;

let langUnsub;

const mount = (language, ref) => {
	const container = document.body.querySelector(ref);
	container.innerHTML = NoPage(language);
	langUnsub = ldodEventSubscriber('language', handleLanguageEvent);
};
const unMount = () => {
	langUnsub();
	getNoPage().remove();
};

const handleLanguageEvent = ({ payload }) => (getNoPage().textContent = message[payload]);

export default () => ({
	path: '/not-found',
	mount,
	unMount,
});
