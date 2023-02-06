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
		sourcemap: true,
		lib: {
			entry: ['src/notifications.js'],
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
				find: '@src',
				replacement: path.resolve(__dirname, 'src'),
			},
			{
				find: '@shared/',
				replacement: '../../dist/',
			},
		],
	},
});
