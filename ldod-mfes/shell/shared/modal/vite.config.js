/** @format */

import { defineConfig } from 'vite';
import terser from '@rollup/plugin-terser';
export default defineConfig({
	build: {
		target: 'es2022',
		outDir: '../dist',
		emptyOutDir: false,
		lib: {
			entry: ['src/ldod-modal/modal.js', 'src/ldod-bs-modal/modal-bs.js'],
			formats: ['es'],
			fileName: (_, entry) => entry + '.js',
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
				replacement: '../../../dist/',
			},
		],
	},
});
