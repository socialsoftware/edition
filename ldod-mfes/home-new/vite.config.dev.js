import { defineConfig, loadEnv } from 'vite';
import path from 'path';

export default defineConfig(({ command, mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  return {
    build: {
      target: 'es2022',
      outDir: 'build.dev',
      sourcemap: true,
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
          replacement: path.resolve(__dirname, 'node_modules/shared/dist'),
        },
        {
          find: '@src',
          replacement: path.resolve(__dirname, 'src'),
        },
        {
          find: 'user',
          replacement: `${env.VITE_NODE_HOST}/user/user.js`,
        },
      ],
    },
  };
});
