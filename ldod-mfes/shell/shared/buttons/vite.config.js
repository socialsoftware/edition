import { defineConfig } from 'vite';

export default defineConfig({
  build: {
    target: 'es2022',
    outDir: '../dist',
    emptyOutDir: false,
    sourcemap: true,
    lib: {
      entry: 'buttons.js',
      formats: ['es'],
      fileName: 'buttons',

    },
    rollupOptions: {
      external: [
        'shared/fetcher.js',
        'shared/utils.js',
      ],
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
