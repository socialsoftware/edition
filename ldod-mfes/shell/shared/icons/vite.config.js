/** @format */

import { defineConfig } from 'vite';
import svgLoader from 'vite-svg-loader';
import terser from '@rollup/plugin-terser';

export default defineConfig({
	build: {
		target: 'esnext',
		outDir: '../dist',
		dynamicImportVarsOptions: {
			exclude: ['./src/helpers.js'],
		},
		sourcemap: true,
		emptyOutDir: false,
		lib: {
			entry: 'index.js',
			formats: ['es'],
			fileName: 'ldod-icons',
		},
		rollupOptions: {
			output: {
				assetFileNames: '[name].[ext]',
				chunkFileNames: '[name].js',
				plugins: [terser()],
			},

			external: [/^@shared/],
		},
	},
	plugins: [svgLoader()],
});
