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
				entry: 'src/search.js',
				formats: ['es'],
				fileName: 'search',
			},
			rollupOptions: {
				output: {
					plugins: [terser()],
				},
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
					find: '@src',
					replacement: '/src',
				},
			],
		},
	};
});
