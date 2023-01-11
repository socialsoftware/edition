const addResourcesToCache = async resources => {
	const cache = await caches.open('v1');
	await cache.addAll(resources);
};

self.addEventListener('install', event => {
	event.waitUntil(
		addResourcesToCache([
			'/ldod-mfes/shared/bootstrap/root.css',
			'/ldod-mfes/style/fonts/WorkSans-VariableFont_wght.ttf',
		])
	);
});

self.addEventListener('fetch', event => {
	event.respondWith(caches.match(event.request));
});

console.log(await caches.keys());
