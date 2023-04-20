/** @format */

import { defineConfig, loadEnv } from 'vite';
import terser from '@rollup/plugin-terser';
export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, process.cwd(), '');
	return {
		build: {
			target: 'es2022',
			outDir: 'build',
			lib: {
				entry: 'src/social.js',
				formats: ['es'],
				fileName: 'social',
			},
			sourcemap: true,
			rollupOptions: {
				output: { plugins: [terser({ ecma: '2016' })] },
				external: [/^@shared/, 'text'],
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
					find: '@src/',
					replacement: '/src/',
				},
			],
		},
	};
});
