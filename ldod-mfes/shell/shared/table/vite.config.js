import { defineConfig, loadEnv } from 'vite';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  return {
    build: {
      target: 'es2022',
      outDir: '../dist',
      lib: {
        entry: 'table.js',
        formats: ['es'],
        fileName: 'table',
      },
      emptyOutDir: false,
      sourcemap: true,
      rollupOptions: {
        external: ['shared/vanilla-jsx.js'],
      },
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
          replacement: `${env.VITE_NODE_HOST}/shared/`,
        },
      ],
    },
  };
});
