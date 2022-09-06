import { defineConfig } from 'vite';

export default defineConfig({
  build: {
    target: 'es2022',
    outDir: '../dist',
    emptyOutDir: false,
    lib: {
      entry: 'buttons.js',
      formats: ['es'],
      fileName: 'buttons',
    },
  },

  resolve: {
    alias: [
      {
        find: '@src/',
        replacement: '/src/',
      },
      {
        find: '@dist/',
        replacement: '/../../shared/dist/',
      },
    ],
  },
});
