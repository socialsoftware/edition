import { defineConfig } from 'vite';

export default defineConfig({
  build: {
    target: 'es2022',
    outDir: '../dist',
    emptyOutDir: false,
    lib: {
      entry: 'index.js',
      formats: ['es'],
      fileName: 'popper',
    },
  },
});
