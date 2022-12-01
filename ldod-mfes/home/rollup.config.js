// rollup.config.js

import { terser } from 'rollup-plugin-terser';
import sourcemaps from 'rollup-plugin-sourcemaps';
import { importAssertions } from 'acorn-import-assertions';
import { importAssertionsPlugin } from 'rollup-plugin-import-assert';
import dynamicImportVars from '@rollup/plugin-dynamic-import-vars';
import alias from '@rollup/plugin-alias';

export default {
  input: './src/home.js',
  external: ['shared/utils.js'],
  output: [
    {
      dir: 'build',
      format: 'es',
      sourcemap: true,
      plugins: [terser()],
    },
  ],
  acornInjectPlugins: [importAssertions],
  plugins: [
    sourcemaps(),
    importAssertionsPlugin(),
    dynamicImportVars({ exclude: ['./src/externalDeps.js'] }),
    alias({
      entries: [
        {
          find: 'shared',
          replacement: './node_modules/shared/dist',
        },
      ],
    }),
  ],
};
