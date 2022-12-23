// rollup.config.js
import { terser } from 'rollup-plugin-terser';
import alias from '@rollup/plugin-alias';
import commonjs from '@rollup/plugin-commonjs';
import json from '@rollup/plugin-json';

export default {
  input: './src/ldod-events.js',
  external: [/^vendor/],
  plugins: [
    json(),
    commonjs(),
    alias({
      entries: {
        vendor: '../node_modules',
      },
    }),
  ],

  output: [
    {
      sourcemap: true,
      dir: '../dist',
      format: 'es',
      plugins: [terser()],
    },
  ],
};
