// rollup.config.js

import { terser } from 'rollup-plugin-terser';
import { importAssertions } from 'acorn-import-assertions';
import { importAssertionsPlugin } from 'rollup-plugin-import-assert';
import sourcemaps from 'rollup-plugin-sourcemaps';

export default {
  input: './src/shell.js',
  plugins: [sourcemaps()],
  output: [
    {
      sourcemap: true,
      dir: 'dist',
      format: 'es',
      plugins: [terser()],
    },
  ],
  acornInjectPlugins: [importAssertions],
  plugins: [importAssertionsPlugin()],
};
