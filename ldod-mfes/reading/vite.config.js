import { defineConfig, loadEnv } from 'vite';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  return {
    build: {
      target: 'es2022',
      outDir: 'build',
      sourcemap: 'true',
      lib: {
        entry: 'src/reading.js',
        formats: ['es'],
        fileName: 'reading',
      },
      rollupOptions: {
        external: [
          'shared/vanilla-jsx.js',
          'shared/router.js',
          'shared/popper.js',
          'text',
        ],
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
        {
          find: '@src/',
          replacement: '/src/',
        },
      ],
    },
  };
});
