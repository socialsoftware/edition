// rollup.config.js
import terser from '@rollup/plugin-terser';

export default {
	input: 'vanilla-jsx.js',
	output: [
		{
			dir: '../dist',
			format: 'es',
			plugins: [terser()],
		},
	],
};
