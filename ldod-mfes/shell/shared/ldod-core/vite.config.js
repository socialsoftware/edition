/** @format */

import { defineConfig } from 'vite';
import terser from '@rollup/plugin-terser';

export default defineConfig({
	build: {
		target: 'esnext',
		outDir: '../dist/core',
		emptyOutDir: true,
		sourcemap: true,
		lib: {
			entry: 'index.js',
			formats: ['es'],
			fileName: 'ldod-core',
		},
		rollupOptions: {
			output: {
				plugins: [terser()],
			},
		},
	},
});
