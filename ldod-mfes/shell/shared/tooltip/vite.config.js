import { defineConfig } from 'vite';
import terser from '@rollup/plugin-terser';
export default defineConfig({
	build: {
		target: 'es2022',
		outDir: '../dist',
		emptyOutDir: false,
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
