/** @format */

import { defineConfig } from 'vite';
import terser from '@rollup/plugin-terser';
export default defineConfig({
	build: {
		target: 'es2022',
		outDir: '../dist',
		emptyOutDir: false,
		lib: {
			entry: 'buttons.js',
			formats: ['es'],
			fileName: 'buttons',
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
				find: '@src/',
				replacement: '/src/',
			},
			{
				find: '@shared/',
				replacement: '/../../shared/dist/',
			},
		],
	},
});
