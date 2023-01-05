import { defineConfig, loadEnv } from 'vite';
import { terser } from 'rollup-plugin-terser';

export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, process.cwd(), '');
	return {
		build: {
			target: 'es2022',
			outDir: 'build',
			sourcemap: true,
			lib: {
				entry: 'src/text.js',
				formats: ['es'],
				fileName: 'text',
			},
			rollupOptions: {
				output: {
					plugins: [terser()],
				},
				external: [/^@shared/, 'reading', 'virtual'],
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
					find: 'reading',
					replacement: `${env.VITE_NODE_HOST}/reading`,
				},
				{
					find: 'virtual',
					replacement: `${env.VITE_NODE_HOST}/virtual`,
				},
				{
					find: '@src/',
					replacement: '/src/',
				},
			],
		},
	};
});
