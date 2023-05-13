/** @format */

import { defineConfig, loadEnv } from 'vite';
import terser from '@rollup/plugin-terser';

export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, process.cwd(), '');
	return {
		build: {
			target: 'esnext',
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
					plugins: [terser()],
				},
				external: [/^@ui/, 'text', /^@vendor/, /^@core/],
			},
		},
		esbuild: {
			jsxFactory: 'createElement',
			jsxFragment: 'createFragment',
			jsxInject: "import {createElement, createFragment} from '@core'",
		},
		resolve: {
			alias: [
				{
					find: '@core',
					replacement: '/node_modules/shared/dist/core/ldod-core.js',
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
