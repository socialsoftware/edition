/** @format */

// rollup.config.js
import terser from '@rollup/plugin-terser';

export default {
	input: ['src/fetcher.js', 'src/request-proxy.js'],
	external: /^@shared/,
	output: {
		dir: '../dist',
		format: 'es',
		sourcemap: true,
		plugins: [terser()],
	},
};
