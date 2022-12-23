// rollup.config.js
import { terser } from 'rollup-plugin-terser';

export default {
  input: './src/router.js',
  external: [/^shared/],
  output: [
    {
      sourcemap: true,
      dir: '../dist',
      format: 'es',
      plugins: [terser()],
    },
  ],
};
