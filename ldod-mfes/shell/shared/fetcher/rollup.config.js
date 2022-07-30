// rollup.config.js
import { terser } from 'rollup-plugin-terser';

export default {
  input: './src/fetcher.js',
  output: [
    {
      dir: '../dist',
      format: 'es',
      plugins: [terser()],
    },
  ],
};
