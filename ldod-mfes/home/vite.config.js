/** @format */

import { defineConfig } from 'vite';
import terser from '@rollup/plugin-terser';

export default defineConfig({
	build: {
		target: 'esnext',
		outDir: 'build',
		lib: {
			entry: ['src/home.js'],
			formats: ['es'],
			fileName: (_, entry) => entry + '.js',
		},
		sourcemap: true,
		rollupOptions: {
			output: {
				plugins: [
					terser({
						ecma: '2016',
					}),
				],
			},
			external: [/^@ui/, /^@vendor/, 'user'],
		},
	},

	resolve: {
		alias: [
			{
				find: '@src/',
				replacement: '/src/',
			},
			{
				find: '@ui',
				replacement: '/node_modules/shared/dist/ui',
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
