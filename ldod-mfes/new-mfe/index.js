/** @format */

import('./src/new-mfe').then(api => {
	api.default.mount('en', 'div#app');
});
