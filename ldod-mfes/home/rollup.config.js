// rollup.config.js

import { terser } from 'rollup-plugin-terser';
import sourcemaps from 'rollup-plugin-sourcemaps';
import { importAssertions } from 'acorn-import-assertions';
import { importAssertionsPlugin } from 'rollup-plugin-import-assert';
import dynamicImportVars from '@rollup/plugin-dynamic-import-vars';

export default {
  input: './src/home.js',
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
  ],
};
