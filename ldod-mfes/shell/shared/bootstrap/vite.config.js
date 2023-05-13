/** @format */

import { defineConfig } from 'vite';
import terser from '@rollup/plugin-terser';
import path from 'path';

export default defineConfig({
	build: {
		target: 'esnext',
		cssCodeSplit: true,
		outDir: '../dist',
		emptyOutDir: false,

		lib: {
			entry: [
				'src/scss/root.scss',
				'src/buttons-css.js',
				'src/root-css.js',
				'src/forms-css.js',
				'src/modal-css.js',
				'src/bootstrap-css.js',
				'src/toast-css.js',
				'src/modal.js',
				'src/dropdown.js',
				'src/collapse.js',
				'src/navbar-css.js',
				'src/nav-css.js',
				'src/dropdown-css.js',
				'src/reboot-css.js',
				'src/transitions-css.js',
			],
			formats: ['es'],
			fileName: (_, entry) => `ui/bootstrap/${entry}.js`,
		},

		rollupOptions: {
			output: {
				plugins: [terser()],
				assetFileNames: 'ui/bootstrap/[name].[ext]',
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
				replacement:
					'../../vendor/node_modules/@popperjs/core_2.11.6/dist/umd/popper.min.js',
			},
		],
	},
});
