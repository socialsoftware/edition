import { ldodEventSubscriber } from '@shared/ldod-events.js';
import user from './src/user';
await user.mount('en', 'div#root');

ldodEventSubscriber('url-changed', ({ payload }) => {
	if (payload.path === '/') {
		history.pushState({}, undefined, '/');
		document.querySelector('ldod-outlet').childNodes.forEach(node => node.remove());
	}
});
document.querySelectorAll('button.lang').forEach(btn => {
	btn.addEventListener('click', () => {
		document.querySelectorAll('[language]').forEach(ele => {
			ele.setAttribute('language', btn.id);
		});
	});
});
