import { defineConfig } from 'vite';
import terser from '@rollup/plugin-terser';

export default defineConfig({
	build: {
		target: 'es2022',
		outDir: '../dist',
		emptyOutDir: false,
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
