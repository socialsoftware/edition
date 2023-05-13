/** @format */

// rollup.config.js

import terser from '@rollup/plugin-terser';
import css from 'rollup-plugin-import-css';
import { resolve } from 'path';

export default {
	external: [/^@core/],
	input: [
		'src/shell.js',
		resolve('../shared/ldod-core-ui/src/scroll-btn/scroll-btn-ssr.js'),
		resolve('../shared/ldod-core-ui/src/loading-spinner/loading-spinner-ssr.js'),
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
