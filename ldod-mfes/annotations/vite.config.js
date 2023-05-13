/** @format */

import { defineConfig, loadEnv } from 'vite';
import terser from '@rollup/plugin-terser';
import { resolve } from 'path';

export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, process.cwd(), '');
	return {
		build: {
			target: 'esnext',
			outDir: 'build',
			sourcemap: true,
			manifest: true,
			lib: {
				entry: 'src/annotations.js',
				formats: ['es'],
				fileName: 'annotations',
			},
			rollupOptions: {
				output: {
					plugins: [terser()],
				},
				external: [/^@core/, /^@ui/, /^@vendor/],
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
					find: 'vendor',
					replacement: resolve(process.cwd(), 'node_modules'),
				},
				{
					find: '@src/',
					replacement: '/src/',
				},
			],
		},
	};
});
