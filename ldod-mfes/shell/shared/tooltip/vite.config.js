import { defineConfig } from 'vite';

export default defineConfig({
  build: {
    target: 'es2022',
    outDir: '../dist',
    emptyOutDir: false,
    sourcemap: true,
    lib: {
      entry: 'tooltip.js',
      formats: ['es'],
      fileName: 'tooltip',
    },

    rollupOptions: {
      external: [/^vendor/],
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
