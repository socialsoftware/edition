/** @format */

import { defineConfig } from 'vite';
import terser from '@rollup/plugin-terser';
import path from 'path';
import svgLoader from 'vite-svg-loader';

export default defineConfig({
	build: {
		target: 'esnext',
		outDir: '../dist/core-ui',
		emptyOutDir: true,
		sourcemap: true,
		dynamicImportVarsOptions: {
			exclude: ['./src/icons/helpers.js'],
		},
		lib: {
			entry: 'index.js',
			formats: ['es'],
			fileName: 'ldod-core-ui',
		},
		rollupOptions: {
			output: {
				plugins: [terser()],
			},
			external: [/^@ui/],
		},
	},
	resolve: {
		alias: [
			{
				find: '@src',
				replacement: path.resolve(__dirname, 'src'),
			},
			{
				find: '@ui/',
				replacement: '../dist/ui/',
			},
		],
	},
	plugins: [svgLoader()],
});
