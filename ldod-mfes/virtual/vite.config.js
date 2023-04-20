/** @format */

import { defineConfig, loadEnv } from 'vite';
import terser from '@rollup/plugin-terser';

export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, process.cwd(), '');
	return {
		build: {
			target: 'es2022',
			outDir: 'build',
			sourcemap: true,
			manifest: true,
			lib: {
				entry: 'src/virtual.js',
				formats: ['es'],
				fileName: 'virtual',
			},

			rollupOptions: {
				output: {
					plugins: [terser({ ecma: '2016' })],
				},
				external: [/^@shared/, 'text', /^@vendor/],
			},
		},
		esbuild: {
			jsxFactory: 'createElement',
			jsxFragment: 'createFragment',
			jsxInject: "import {createElement, createFragment} from '@shared/vanilla-jsx.js'",
		},
		resolve: {
			alias: [
				{
					find: '@shared',
					replacement: `${env.VITE_NODE_HOST}/shared`,
				},
				{
					find: 'annotations',
					replacement: `${env.VITE_NODE_HOST}/annotations/annotations.js`,
				},
				{
					find: 'annotations.dev',
					replacement: `${env.VITE_NODE_HOST}/annotations/annotations.dev.js`,
				},
				{
					find: '@src/',
					replacement: '/src/',
				},
				{
					find: 'search',
					replacement: `${env.VITE_NODE_HOST}/search/search.js`,
				},
			],
		},
	};
});
