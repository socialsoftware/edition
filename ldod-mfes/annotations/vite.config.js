import { defineConfig, loadEnv } from 'vite';
import { terser } from 'rollup-plugin-terser';
import { resolve } from 'path';

export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, process.cwd(), '');
	return {
		build: {
			target: 'es2022',
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
				external: [/^@shared/, /^@vendor/],
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
					find: 'shared',
					replacement: `${env.VITE_NODE_HOST}/shared`,
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
