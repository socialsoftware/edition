// rollup.config.js
import { terser } from 'rollup-plugin-terser';
import sourcemaps from 'rollup-plugin-sourcemaps';
import alias from '@rollup/plugin-alias';
import commonjs from '@rollup/plugin-commonjs';

export default {
  input: './src/ldod-events.js',
  plugins: [sourcemaps(), commonjs(), alias({
    entries: {
      "@trutoo": "./node_modules/@trutoo/event-bus/dist/index.umd.min.js",
      "jsonschema": "./node_modules/jsonschema/lib/index.js"
    }
  })],
  output: [
    {
      sourcemap: true,
      dir: '../dist',
      format: 'es',
      plugins: [terser()],
    },
  ],
};
