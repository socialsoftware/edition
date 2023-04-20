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
				external: [/^@shared/, 'text'],
				output: {
					plugins: [terser({ ecma: '2016' })],
				},
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
					find: '@src',
					replacement: '/src',
				},
			],
		},
	};
});
