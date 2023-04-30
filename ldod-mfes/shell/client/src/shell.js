/** @format */

import './sw/service-worker-reg';
import './body-observer.js';
import './events-module.js';
import './shell-router.js';
import '@shared/router.js';
import './worker-loader.js';

globalThis?.addEventListener('pointermove', loadLazyModules, { once: true });

function loadLazyModules() {
	import('@shared/notifications.js');
	import('@shared/ldod-icons.js');
	import('./components/loading-spinner/loading-spinner.js');
	import('./components/scroll-btn/scroll-btn.js');
}
