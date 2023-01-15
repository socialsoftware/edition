export function load() {
	import('about')
		.then(({ loadConductCode }) => loadConductCode())
		.catch(e => console.error(e));
}
