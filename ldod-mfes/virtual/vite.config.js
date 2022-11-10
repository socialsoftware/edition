import { defineConfig, loadEnv } from 'vite';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  return {
    build: {
      target: 'es2022',
      outDir: 'build',
      sourcemap: true,
      lib: {
        entry: 'src/virtual.js',
        formats: ['es'],
        fileName: 'virtual',
      },

      rollupOptions: {
        external: [
          'shared/vanilla-jsx.js',
          'shared/router.js',
          'shared/table.js',
          'shared/fetcher.js',
          'shared/utils.js',
          'shared/tooltip.js',
          'shared/popper.js',
          'shared/select-pure.js',
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
          find: 'annotations',
          replacement: `${env.VITE_NODE_HOST}/annotations/annotations.js`,
        },
        {
          find: 'annotations.dev',
          replacement: `${env.VITE_NODE_HOST}/annotations/annotations.dev.js`,
        },
        {
          find: '@src/',
          replacement: '/src/',
        },
        {
          find: 'search',
          replacement: `${env.VITE_NODE_HOST}/search/search.js`,
        },
      ],
    },
  };
});
