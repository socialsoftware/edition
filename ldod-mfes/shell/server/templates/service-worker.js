/** @format */

let CACHE_VERSION = '6dd2649c-c81b-494b-b58e-001c54411077';

self.addEventListener('install', e => {
	self.skipWaiting();
});

self.addEventListener('activate', e => {
	e.waitUntil(enableNavigationPreload());
	self.skipWaiting();
});

self.addEventListener('activate', e => {
	e.waitUntil(
		deleteOldCaches().then(() => {
			self.clients.matchAll().then(clients => {
				clients.forEach(client =>
					client.postMessage({
						msg: 'Cache deleted',
					})
				);
			});
		})
	);
});

self.addEventListener('fetch', e => {
	e.respondWith(
		cacheFirst({
			request: e.request,
			preloadResponsePromise: e.preloadResponse,
			fallbackUrl: '/ldod-mfes/',
		})
	);
});

self.addEventListener('activate', event => {});

async function cacheFirst({ request, preloadResponsePromise, fallbackUrl }) {
	const cacheResponse = await caches.match(request);
	if (cacheResponse) return cacheResponse;

	const preloadResponse = await preloadResponsePromise;
	if (preloadResponse) {
		console.info('using preload response', preloadResponse);
		putInCache(request, preloadResponse.clone());
		return preloadResponse;
	}

	try {
		const networkResponse = await fetch(request);
		if (request.url.includes('/ldod-mfes/')) putInCache(request, networkResponse.clone());
		return networkResponse;
	} catch (error) {
		const fallbackResponse = await caches.match(fallbackUrl);
		if (fallbackResponse) return fallbackResponse;
		return new Response('Network error happened', {
			status: 408,
			headers: { 'Content-Type': 'text/plain' },
		});
	}
}

async function putInCache(req, res) {
	await caches.open(CACHE_VERSION).then(cache => cache.put(req, res));
}

function deleteCache(key) {
	console.info(`Deleting cache ${key}`);
	caches.delete(key);
}

async function deleteOldCaches() {
	return Promise.resolve(
		caches
			.keys()
			.then(keys => keys.filter(key => key !== `v${CACHE_VERSION}`).forEach(deleteCache))
	);
}

async function enableNavigationPreload() {
	if (self.registration.navigationPreload) await self.registration.navigationPreload.enable();
}
