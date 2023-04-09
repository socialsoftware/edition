/** @format */

import { defineConfig, loadEnv } from 'vite';
import { terser } from 'rollup-plugin-terser';

export default defineConfig({
	build: {
		target: 'es2022',
		outDir: 'build',
		lib: {
			entry: ['src/home.js'],
			formats: ['es'],
			fileName: (_, entry) => entry + '.js',
		},
		rollupOptions: {
			output: {
				plugins: [terser()],
			},
			external: [/^@shared/, /^@vendor/, 'user'],
		},
	},

	resolve: {
		alias: [
			{
				find: '@src/',
				replacement: '/src/',
			},
			{
				find: '@shared',
				replacement: '/node_modules/shared/dist',
			},
			{
				find: '@vendor/',
				replacement: '/node_modules/',
			},
			{
				find: 'user',
				replacement: 'http://localhost:9000/ldod-mfes/user/user.js',
			},
			{
				find: 'shared',
				replacement: '/node_modules/shared/dist',
			},
		],
	},
});
