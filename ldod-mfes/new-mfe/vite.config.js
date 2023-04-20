/** @format */

import {defineConfig, loadEnv} from 'vite';
import {terser} from 'rollup-plugin-terser';

export default defineConfig(({mode}) => {
    const env = loadEnv(mode, process.cwd(), '');
    return {
        build: {
            target: 'es2022',
            outDir: 'build',
            sourcemap: true,
            lib: {
                entry: 'src/new-mfe.js',
                formats: ['es'],
                fileName: 'new-mfe',
            },

            rollupOptions: {
                output: {
                    plugins: [terser()],
                },
                external: [/^@shared/, 'text', /^@vendor/],
            },
        },

        resolve: {
            alias: [
                {
                    find: '@shared',
                    replacement: `${env.VITE_NODE_HOST}/shared`,
                },
                {
                    find: 'home',
                    replacement: `${env.VITE_NODE_HOST}/home/home.js`,
                },

                {
                    find: '@src/',
                    replacement: '/src/',
                },
            ],
        },
    };
});
