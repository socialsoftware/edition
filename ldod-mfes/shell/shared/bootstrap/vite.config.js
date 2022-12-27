import { defineConfig } from 'vite';
import path from 'path';

export default defineConfig({
  build: {
    target: 'es2022',
    cssCodeSplit: true,
    outDir: '../dist',
    emptyOutDir: false,

    lib: {
      entry: [
        'src/root/root.js',
        'src/forms/forms.js',
        'src/buttons/buttons.js',
        'src/tables/tables.js',
      ],
      formats: ['es'],
      fileName: (_, entry) => `bootstrap/${entry}.js`,
    },

    rollupOptions: {
      output: {
        assetFileNames: 'bootstrap/[name].[ext]',
      },
    },
  },
  resolve: {
    alias: [
      {
        find: '@bootstrap',
        replacement: path.resolve(__dirname, 'node_modules/bootstrap'),
      },
      {
        find: '@src',
        replacement: path.resolve(__dirname, 'src'),
      },
    ],
  },
});
