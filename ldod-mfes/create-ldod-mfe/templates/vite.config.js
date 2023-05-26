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
					find: '@core',
					replacement: '/node_modules/@core/core/ldod-core.js',
				},
			],
		},
	};
});
