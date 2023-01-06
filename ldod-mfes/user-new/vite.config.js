import { defineConfig } from 'vite';
import terser from '@rollup/plugin-terser';
import { resolve } from 'path';

export default defineConfig({
	build: {
		target: 'esnext',
		outDir: 'build',
		lib: {
			entry: 'src/user.js',
			formats: ['es'],
			fileName: 'user',
		},
		rollupOptions: {
			output: {
				plugins: [terser()],
			},
			external: [/^@shared/],
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
				find: '@src',
				replacement: '/src',
			},
		],
	},
});
