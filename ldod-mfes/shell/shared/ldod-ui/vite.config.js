/** @format */

import { defineConfig } from 'vite';
import terser from '@rollup/plugin-terser';

export default defineConfig({
	build: {
		target: 'esnext',
		outDir: '../dist/ui',
		emptyOutDir: true,
		sourcemap: true,
		lib: {
			entry: ['index.js', 'nav-dropdown/nav-dropdown-ssr.js', 'buttons/buttons.js'],
			formats: ['es'],
		},
		rollupOptions: {
			output: {
				assetFileNames: '[name].[ext]',
				chunkFileNames: '[name].js',
				plugins: [terser()],
			},
			external: [/^@ui/, /node_modules/, /@vendor/, /^@core/],
		},
	},
	resolve: {
		alias: [
			{
				find: '@vendor/',
				replacement: '../../vendor/node_modules/',
			},
		],
	},
});
