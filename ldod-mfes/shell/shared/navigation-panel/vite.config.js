import { defineConfig } from 'vite';
import terser from '@rollup/plugin-terser';
import { resolve } from 'path';

export default defineConfig({
	build: {
		target: 'es2022',
		outDir: '../dist',
		emptyOutDir: false,
		lib: {
			entry: 'src/navigation-panel.js',
			formats: ['es'],
			fileName: 'navigation-panel',
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
	resolve: {
		alias: [
			{
				find: '@shared',
				replacement: resolve('../dist'),
			},
			{
				find: '@vendor',
				replacement: resolve('../dist/node_modules'),
			},
		],
	},
});
