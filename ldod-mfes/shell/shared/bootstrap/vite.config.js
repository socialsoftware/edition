import { defineConfig } from 'vite';
import path from 'path';

export default defineConfig({
  build: {
    target: 'es2022',
    outDir: '../dist',
    emptyOutDir: false,
    lib: {
      entry: 'src/index.js',
      formats: ['es'],
      fileName: 'bootstrap',
    },
    rollupOptions: {
      external: ['@popperjs/core'],
      output: {
        assetFileNames: (asset) => {
          if (asset.name == 'style.css') return 'bootstrap.css';
          return asset.name;
        },
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
