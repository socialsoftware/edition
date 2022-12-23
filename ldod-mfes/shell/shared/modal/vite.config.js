import { defineConfig } from 'vite';

export default defineConfig({
  build: {
    target: 'es2022',
    outDir: '../dist',
    emptyOutDir: false,
    lib: {
      entry: 'modal.js',
      formats: ['es'],
      fileName: 'modal',
    },
    rollupOptions: {
      external: [/^shared/],
    },
  },

  resolve: {
    alias: [
      {
        find: '@src/',
        replacement: '/src/',
      },
    ],
  },
});
