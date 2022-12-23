import { defineConfig, loadEnv } from 'vite';
import path from 'path';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  return {
    build: {
      target: 'es2022',
      outDir: 'build',
      sourcemap: true,
      emptyOutDir: false,
      lib: {
        entry: 'server-navbar.js',
        formats: ['es'],
        fileName: 'ssr-navbar',
      },
      rollupOptions: {
        external: [/^shared/],
      },
    },

    resolve: {
      alias: [
        {
          find: 'shared/',
          replacement: `${env.VITE_NODE_HOST}/shared/`,
        },
        {
          find: '@src',
          replacement: path.resolve(__dirname, 'src'),
        },
      ],
    },
  };
});
