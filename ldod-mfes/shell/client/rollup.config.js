// rollup.config.js

import sourcemaps from 'rollup-plugin-sourcemaps';
import terser from '@rollup/plugin-terser';
import { importAssertionsPlugin } from 'rollup-plugin-import-assert';
import { importAssertions } from 'acorn-import-assertions';

export default {
  input: 'src/shell.js',
  output: [
    {
      dir: 'dist',
      format: 'es',
      plugins: [terser()],
      sourcemap: true,
    },
  ],
  acornInjectPlugins: [importAssertions],
  plugins: [sourcemaps(), importAssertionsPlugin(),
  ]
};
