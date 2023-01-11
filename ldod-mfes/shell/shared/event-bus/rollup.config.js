import { nodeResolve } from '@rollup/plugin-node-resolve';
import nodePolyfills from 'rollup-plugin-node-polyfills';
import commonjs from '@rollup/plugin-commonjs';
import typescript from 'rollup-plugin-typescript2';
import { terser } from 'rollup-plugin-terser';
import pkg from './package.json';

export default [
  {
    input: 'src/index.ts',
    output: [
      {
        name: 'EventBus',
        file: pkg.browser,
        format: 'umd',
        plugins: [terser()],
        sourcemap: true,
      },
      {
        name: 'EventBus',
        file: `${pkg.browser.replace(/\.min\.js$/, '.js')}`,
        format: 'umd',
      },
    ],
    plugins: [
      commonjs(),
      nodePolyfills(),
      nodeResolve({ browser: true, preferBuiltins: false }),
      typescript({
        typescript: require('typescript'),
      }),
    ],
  },
];
