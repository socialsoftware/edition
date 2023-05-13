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
				entry: 'src/about.js',
				formats: ['es'],
				fileName: 'about',
			},
			rollupOptions: {
				output: {
					plugins: [
						terser({
							ecma: '2016',
						}),
					],
				},
				external: [/^@core/],
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
					find: 'shared/',
					replacement: `${env.VITE_NODE_HOST}/shared/`,
				},
				{
					find: '@src/',
					replacement: '/src/',
				},
			],
		},
	};
});
