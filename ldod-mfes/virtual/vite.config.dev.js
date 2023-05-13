/** @format */

import { defineConfig, loadEnv } from 'vite';

export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, process.cwd(), '');
	return {
		build: {
			target: 'esnext',
			outDir: 'build',
			sourcemap: true,
			lib: {
				entry: 'src/virtual.js',
				formats: ['es'],
				fileName: 'virtual-dev',
			},
			emptyOutDir: false,
		},
		rollupOptions: {
			external: ['search', 'annotations', 'text'],
		},
		esbuild: {
			jsxFactory: 'createElement',
			jsxFragment: 'createFragment',
			jsxInject: "import {createElement, createFragment} from 'core'",
		},
		resolve: {
			alias: [
				{
					find: '@vendor',
					replacement: '/node_modules',
				},
				{
					find: '@core',
					replacement: '/node_modules/shared/dist/core/ldod-core.js',
				},
				{
					find: '@src/',
					replacement: '/src/',
				},
				{
					find: 'search',
					replacement: `${env.VITE_NODE_HOST}/search/search.js`,
				},
				{
					find: 'annotations',
					replacement: `${env.VITE_NODE_HOST}/annotations/annotations.js`,
				},
				{
					find: 'text',
					replacement: `${env.VITE_NODE_HOST}/text/text.js`,
				},

				{
					find: 'annotations.dev',
					replacement: `${env.VITE_NODE_HOST}/annotations/annotations.dev.js`,
				},
			],
		},
	};
});
