/** @format */

import { defineConfig } from 'vite';
import terser from '@rollup/plugin-terser';
import path from 'path';

export default defineConfig({
	build: {
		target: 'es2022',
		cssCodeSplit: true,
		outDir: '../dist',
		emptyOutDir: false,

		lib: {
			entry: [
				'src/scss/utilities.scss',
				'src/scss/root.scss',
				'src/buttons-css.js',
				'src/root-css.js',
				'src/forms-css.js',
				'src/modal-css.js',
				'src/bootstrap-css.js',
				'src/modal.js',
			],
			formats: ['es'],
			fileName: (_, entry) => `bootstrap/${entry}.js`,
		},

		rollupOptions: {
			output: {
				plugins: [terser()],
				assetFileNames: 'bootstrap/[name].[ext]',
			},
		},
	},
	resolve: {
		alias: [
			{
				find: '@bootstrap',
				replacement: path.resolve(__dirname, 'node_modules/bootstrap'),
			},
			{
				find: '@src',
				replacement: path.resolve(__dirname, 'src'),
			},
			{
				find: '@popperjs/core',
				replacement: '../../vendor/node_modules/@popperjs/core_2.11.6',
			},
		],
	},
});
