/** @format */

import { defineConfig } from 'vite';
import terser from '@rollup/plugin-terser';
export default defineConfig({
	build: {
		target: 'esnext',
		outDir: '../dist',
		emptyOutDir: false,
		sourcemap: true,
		lib: {
			entry: 'tooltip.js',
			formats: ['es'],
			fileName: 'tooltip',
		},

		rollupOptions: {
			output: {
				plugins: [terser()],
			},
			external: [/node_modules/, /@vendor/],
		},
	},
});
