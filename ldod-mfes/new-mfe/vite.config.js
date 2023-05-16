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
				entry: `src/${env.VITE_MFE_NAME}.js`,
				formats: ['es'],
				fileName: env.VITE_MFE_NAME,
			},
			rollupOptions: {
				output: {
					plugins: [terser()],
				},
				external: [/^@ui/, /^@core/, /^@lit-bundle/, /^@vendor/],
			},
		},
		esbuild: {
			jsxFactory: 'createElement',
			jsxFragment: 'createFragment',
			jsxInject: "import {createElement, createFragment} from 'shared/vanilla-jsx.js'",
		},
		resolve: {
			alias: [
				{
					find: '@vendor/',
					replacement: '/node_modules/',
				},
				{
					find: '@core',
					replacement: './node_modules/shared/dist/core/ldod-core.js',
				},
				{
					find: '@shared/',
					replacement: `/node_modules/shared/dist/`,
				},
				{
					find: 'shared/',
					replacement: `/node_modules/shared/dist/`,
				},
				{
					find: '@src/',
					replacement: '/src/',
				},
				{
					find: '@lit-bundle',
					replacement: 'https://cdn.jsdelivr.net/gh/lit/dist@2.7.4/all/lit-all.min.js',
				},
			],
		},
	};
});
