/** @format */

import { defineConfig, loadEnv } from 'vite';
import terser from '@rollup/plugin-terser';

export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, process.cwd(), '');
	return {
		build: {
			target: 'esnext',
			outDir: '../dist',
			sourcemap: true,
			lib: {
				entry: 'table.js',
				formats: ['es'],
				fileName: 'table',
			},
			emptyOutDir: false,
			rollupOptions: {
				output: {
					plugins: [terser()],
				},
				external: [/^@shared/],
			},
		},

		resolve: {
			alias: [
				{
					find: '@shared/',
					replacement: `${env.VITE_NODE_HOST}/shared/`,
				},
			],
		},
	};
});
