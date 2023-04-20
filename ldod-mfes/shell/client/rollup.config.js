/** @format */

// rollup.config.js

import { terser } from 'rollup-plugin-terser';
import css from 'rollup-plugin-import-css';

export default {
	external: [/^@shared/],
	input: [
		'src/shell.js',
		'src/components/scroll-btn/scroll-btn-ssr.js',
		'src/components/loading-spinner/loading-spinner-ssr.js',
	],
	output: [
		{
			dir: 'dist',
			format: 'es',
			plugins: [terser()],
			sourcemap: true,
			entryFileNames: '[name].js',
			assetFileNames: '[name].js',
			chunkFileNames: '[name].js',
		},
	],
	plugins: [css()],
};
