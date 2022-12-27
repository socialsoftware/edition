import { defineConfig, loadEnv } from 'vite';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  return {
    build: {
      target: 'es2022',
      outDir: 'build',
      lib: {
        entry: 'src/user.js',
        formats: ['es'],
        fileName: 'user',
      },
      rollupOptions: {
        external: [/^shared/],
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
          find: 'shared',
          replacement: `${env.VITE_NODE_HOST}/shared`,
        },
        {
          find: '@src',
          replacement: '/src',
        },
      ],
    },
  };
});
