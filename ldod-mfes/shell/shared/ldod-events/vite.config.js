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
			entry: 'src/ldod-events.js',
			formats: ['es'],
			fileName: 'ldod-events',
		},
		rollupOptions: {
			output: {
				plugins: [terser()],
			},
		},
	},
});
