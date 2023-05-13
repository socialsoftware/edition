/** @format */

import { defineConfig, loadEnv } from 'vite';
import terser from '@rollup/plugin-terser';
export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, process.cwd(), '');
	return {
		build: {
			target: 'esnext',
			outDir: 'build',
			lib: {
				entry: 'src/social.js',
				formats: ['es'],
				fileName: 'social',
			},
			sourcemap: true,
			rollupOptions: {
				output: { plugins: [terser()] },
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
					find: '@ui/',
					replacement: '/node_modules/shared/dist/ui/',
				},
				{
					find: '@src/',
					replacement: '/src/',
				},
			],
		},
	};
});
