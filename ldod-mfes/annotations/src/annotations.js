/** @format */

const isBrowserEnv =
	typeof window !== 'undefined' &&
	typeof document !== 'undefined' &&
	typeof navigator !== 'undefined';

if (isBrowserEnv) window.onpointermove = async () => import('./annotator-service');

export default {
	path: '',
	mount: () => {},
	unMount: () => {},
	annotator: async ({ id, element }) => {
		new (await customElements.whenDefined('annotator-service'))().annotate({ id, element });
	},
};
