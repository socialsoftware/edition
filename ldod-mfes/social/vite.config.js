import { defineConfig, loadEnv } from 'vite';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  return {
    build: {
      target: 'es2022',
      outDir: 'build',
      lib: {
        entry: 'src/social.js',
        formats: ['es'],
        fileName: 'social',
      },
      rollupOptions: {
        external: [
          'shared/vanilla-jsx.js',
          'shared/router.js',
          'shared/table.js',
          'shared/fetcher.js',
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
