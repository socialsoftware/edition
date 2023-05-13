/** @format */

import './sw/service-worker-reg';
import './body-observer.js';
import '@core';
import './events-module.js';
import './shell-router.js';
import './worker-loader.js';

const loadCoreUi = () => import('@core-ui');
globalThis?.addEventListener('pointermove', loadCoreUi, { once: true });
