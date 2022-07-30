import { defineConfig } from 'vite';

export default defineConfig({
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
        replacement: 'http://localhost:9000/ldod-mfes/shared/',
      },
    ],
  },
});
