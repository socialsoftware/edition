import { defineConfig } from 'vite';

export default defineConfig({
  build: {
    target: 'es2022',
    outDir: '../dist',
    lib: {
      entry: 'table.js',
      formats: ['es'],
      fileName: 'table-dev',
    },
    emptyOutDir: false,
  },
  esbuild: {
    jsxFactory: 'createElement',
    jsxFragment: 'createFragment',
    jsxInject:
      "import {createElement, createFragment} from 'shared/vanilla-jsx.js'",
  },
  resolve: {
    alias: [
      {
        find: 'shared/',
        replacement: '../dist/',
      },
    ],
  },
});
