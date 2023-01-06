// rollup.config.js
import terse from '@rollup/plugin-terser';
import { terser } from 'rollup-plugin-terser';
export default {
	input: './src/router.js',
	external: [/^@shared/],
	output: [
		{
			dir: '../dist',
			format: 'es',
			sourcemap: true,
			plugins: [terser()],
		},
	],
};
