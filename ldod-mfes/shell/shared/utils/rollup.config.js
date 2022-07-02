// rollup.config.js
import { terser } from 'rollup-plugin-terser';

export default {
  input: './src/utils.js',
  output: [
    {
      dir: '../dist',
      format: 'es',
      plugins: [terser()],
    },
  ],
};
