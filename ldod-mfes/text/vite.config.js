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
			lib: {
				entry: 'src/text.js',
				formats: ['es'],
				fileName: 'text',
			},
			rollupOptions: {
				output: {
					plugins: [terser({ ecma: '2016' })],
				},
				external: [/^@core/, /^@ui/, 'reading', 'virtual'],
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
					find: '@ui',
					replacement: '/node_modules/shared/dist/ui',
				},
				{
					find: 'reading',
					replacement: `${env.VITE_NODE_HOST}/reading`,
				},
				{
					find: 'virtual',
					replacement: `${env.VITE_NODE_HOST}/virtual`,
				},
				{
					find: '@src/',
					replacement: '/src/',
				},
			],
		},
	};
});
