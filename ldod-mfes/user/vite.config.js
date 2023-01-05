import { defineConfig, loadEnv } from 'vite';
import { terser } from 'rollup-plugin-terser';
import { resolve } from 'path';

export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, process.cwd(), '');
	return {
		build: {
			target: 'esnext',
			outDir: 'build',
			sourcemap: true,
			lib: {
				entry: 'src/user.js',
				formats: ['es'],
				fileName: 'user',
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
					find: '@shared',
					replacement: resolve('node_modules', 'shared/dist'),
				},
				{
					find: '@vendor',
					replacement: resolve('node_modules', 'shared/dist/node_modules'),
				},

				{
					find: '@src',
					replacement: '/src',
				},
			],
		},
	};
});
