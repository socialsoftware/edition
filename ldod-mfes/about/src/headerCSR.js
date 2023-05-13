/** @format */

import { headerData } from './header-data';

if (typeof window !== 'undefined') {
	customElements.whenDefined('nav-bar').then(({ instance }) => {
		if (!instance.getHeader('about')) {
			import('@core').then(({ ldodEventBus }) =>
				ldodEventBus.publish('ldod:header', headerData)
			);
		}
	});
}
