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
			lib: {
				entry: 'src/search.js',
				formats: ['es'],
				fileName: 'search',
			},
			rollupOptions: {
				output: {
					plugins: [terser()],
				},
				external: [/^@core/, /^@ui/, 'text'],
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
					find: '@src',
					replacement: '/src',
				},
			],
		},
	};
});
