import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  base: '/microfrontend-vite/',
  build: {
    outDir: '../edition-ldod/src/main/webapp/microfrontend-vite/',
    emptyOutDir: true,
    assetsDir: './static',
    manifest: true,
  },
});
