self.addEventListener('activate', event => {});

self.addEventListener('install', event => {
	event.waitUntil(
		caches.open('v1').then(cache => {
			cache.add('/ldod-mfes/');
		})
	);
});

self.addEventListener('fetch', event => {});
