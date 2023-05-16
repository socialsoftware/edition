/** @format */

if (navigator.serviceWorker) register();

async function register() {
	const reg = await navigator.serviceWorker
		.register('/ldod-mfes/service-worker.js', {
			scope: '/ldod-mfes/',
			updateViaCache: 'none',
		})
		.catch(error => console.error(error));
	if (reg?.installing) console.log('Service worker installing');
	if (reg?.waiting) console.log('Service worker installed');
	if (reg?.active) console.log('Service worker active');
}

navigator.serviceWorker.addEventListener('message', event => {
	console.log('Main thread received a message:', event.data);
	location.reload();
});
