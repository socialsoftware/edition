/** @format */

// rollup.config.js
import terser from '@rollup/plugin-terser';

export default {
	input: './index.js',
	output: [
		{
			dir: 'dist',
			format: 'es',
			sourcemap: true,
			plugins: [terser()],
		},
	],
};
