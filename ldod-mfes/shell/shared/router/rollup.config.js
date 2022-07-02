// rollup.config.js
import { terser } from 'rollup-plugin-terser';

export default {
  input: './src/router.js',
  output: [
    {
      dir: '../dist',
      format: 'es',
      plugins: [terser()],
    },
  ],
};
