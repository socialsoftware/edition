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
				entry: 'src/reading.js',
				formats: ['es'],
				fileName: 'reading',
			},
			rollupOptions: {
				external: [/^@core/, /^@ui/, 'text'],
				output: {
					plugins: [terser()],
				},
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
					replacement: `${env.VITE_NODE_HOST}/shared`,
				},
				{
					find: '@ui',
					replacement: `/node_modules/shared/dist/ui`,
				},
				{
					find: '@src',
					replacement: '/src',
				},
			],
		},
	};
});
