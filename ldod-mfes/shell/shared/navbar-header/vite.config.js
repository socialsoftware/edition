/** @format */

import { defineConfig } from 'vite';
import { terser } from 'rollup-plugin-terser';
import { resolve } from 'path';

export default defineConfig({
	build: {
		target: 'esnext',
		outDir: '../dist/dropdown',
		sourcemap: true,
		emptyOutDir: false,
		lib: {
			entry: ['src/li-dropdown.js', 'src/navbar-header-ssr.js'],
			formats: ['es'],
		},
		rollupOptions: {
			output: {
				plugins: [terser()],
			},

			external: [/^@shared/],
		},
	},
	resolve: {
		alias: [
			{
				find: '@shared',
				replacement: resolve('../dist'),
			},
		],
	},
});
