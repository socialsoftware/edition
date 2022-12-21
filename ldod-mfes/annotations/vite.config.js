import { defineConfig, loadEnv } from 'vite';
import pkg from './package.json';
import { resolve } from 'path';
import transformImports from './rollup-plugin-transform-imports';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  return {
    build: {
      target: 'es2022',
      outDir: 'build',
      sourcemap: true,
      manifest: true,
      lib: {
        entry: 'src/annotations.js',
        formats: ['es'],
        fileName: 'annotations',
      },
      rollupOptions: {
        external: [
          /^shared/,
          ...Object.keys(pkg.externalDependencies).map(
            (dep) => new RegExp(`^${dep}`)
          ),
        ],
        plugins: [transformImports()],

        /* output: {
          chunkFileNames: '[name].js',
          manualChunks: (id) => {
            if (!Object.keys(pkg.dependencies).some((dep) => id.includes(dep)))
              return;
            const dep = Object.keys(pkg.dependencies).find((dep) =>
              id.includes(dep)
            );
            return `shared/${dep}${pkg.dependencies[dep]}`;
          },
          plugins: [transformImports()],
        },
      },*/
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
          find: 'vendor',
          replacement: resolve(process.cwd(), 'node_modules'),
        },
        {
          find: '@src/',
          replacement: '/src/',
        },
      ],
    },
  };
});
