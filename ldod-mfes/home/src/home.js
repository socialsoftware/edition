/** @format */

let home;
let ldodValidator;

if (typeof window !== 'undefined') {
	import('./navbar/ldod-navbar.js');
	import('./home/home-info.js');
}

export async function registerMFE(headerData) {
	if (!ldodValidator)
		ldodValidator = (await import('./navbar/header-menu/data-schema-validator.js'))
			.headerDataSchemaValidator;
	const { errors } = ldodValidator(headerData);
	if (errors.length) return errors;
	const { name, data, constants } = headerData;
	customElements.whenDefined('ldod-navbar').then(navbar => {
		navbar.instance.addHeaderMenu(name, data, constants);
	});
}

async function loadHome() {
	if (!home) home = await import('./home/home.js');
	return home;
}

export default {
	path: '/',
	mount: async (lang, ref) => (await loadHome()).mount(lang, ref),
	unMount: async () => (await loadHome()).unMount(),
};
