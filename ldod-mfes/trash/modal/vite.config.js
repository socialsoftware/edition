/** @format */

import { defineConfig } from 'vite';
import terser from '@rollup/plugin-terser';
export default defineConfig({
	build: {
		target: 'esnext',
		outDir: '../dist',
		emptyOutDir: false,
		sourcemap: true,
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
			{
				find: '@vendor/',
				replacement: '../../vendor/node_modules/',
			},
		],
	},
});
