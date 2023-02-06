// vite.config.js
import { defineConfig } from "file:///home/raimundo/Documents/thesis/edition/ldod-mfes/shell/shared/node_modules/vite/dist/node/index.js";
import terser from "file:///home/raimundo/Documents/thesis/edition/ldod-mfes/shell/shared/node_modules/@rollup/plugin-terser/dist/es/index.js";
import path from "path";
var __vite_injected_original_dirname = "/home/raimundo/Documents/thesis/edition/ldod-mfes/shell/shared/bootstrap";
var vite_config_default = defineConfig({
  build: {
    target: "es2022",
    cssCodeSplit: true,
    outDir: "../dist",
    emptyOutDir: false,
    lib: {
      entry: [
        "src/scss/utilities.scss",
        "src/scss/root.scss",
        "src/utilities-css.js",
        "src/buttons-css.js",
        "src/root-css.js",
        "src/forms-css.js",
        "src/modal-css.js",
        "src/bootstrap-css.js",
        "src/toast-css.js",
        "src/modal.js"
      ],
      formats: ["es"],
      fileName: (_, entry) => `bootstrap/${entry}.js`
    },
    rollupOptions: {
      output: {
        plugins: [terser()],
        assetFileNames: "bootstrap/[name].[ext]"
      }
    }
  },
  resolve: {
    alias: [
      {
        find: "@bootstrap",
        replacement: path.resolve(__vite_injected_original_dirname, "node_modules/bootstrap")
      },
      {
        find: "@src",
        replacement: path.resolve(__vite_injected_original_dirname, "src")
      },
      {
        find: "@popperjs/core",
        replacement: "../../vendor/node_modules/@popperjs/core_2.11.6"
      }
    ]
  }
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcuanMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCIvaG9tZS9yYWltdW5kby9Eb2N1bWVudHMvdGhlc2lzL2VkaXRpb24vbGRvZC1tZmVzL3NoZWxsL3NoYXJlZC9ib290c3RyYXBcIjtjb25zdCBfX3ZpdGVfaW5qZWN0ZWRfb3JpZ2luYWxfZmlsZW5hbWUgPSBcIi9ob21lL3JhaW11bmRvL0RvY3VtZW50cy90aGVzaXMvZWRpdGlvbi9sZG9kLW1mZXMvc2hlbGwvc2hhcmVkL2Jvb3RzdHJhcC92aXRlLmNvbmZpZy5qc1wiO2NvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9pbXBvcnRfbWV0YV91cmwgPSBcImZpbGU6Ly8vaG9tZS9yYWltdW5kby9Eb2N1bWVudHMvdGhlc2lzL2VkaXRpb24vbGRvZC1tZmVzL3NoZWxsL3NoYXJlZC9ib290c3RyYXAvdml0ZS5jb25maWcuanNcIjsvKiogQGZvcm1hdCAqL1xuXG5pbXBvcnQgeyBkZWZpbmVDb25maWcgfSBmcm9tICd2aXRlJztcbmltcG9ydCB0ZXJzZXIgZnJvbSAnQHJvbGx1cC9wbHVnaW4tdGVyc2VyJztcbmltcG9ydCBwYXRoIGZyb20gJ3BhdGgnO1xuXG5leHBvcnQgZGVmYXVsdCBkZWZpbmVDb25maWcoe1xuXHRidWlsZDoge1xuXHRcdHRhcmdldDogJ2VzMjAyMicsXG5cdFx0Y3NzQ29kZVNwbGl0OiB0cnVlLFxuXHRcdG91dERpcjogJy4uL2Rpc3QnLFxuXHRcdGVtcHR5T3V0RGlyOiBmYWxzZSxcblxuXHRcdGxpYjoge1xuXHRcdFx0ZW50cnk6IFtcblx0XHRcdFx0J3NyYy9zY3NzL3V0aWxpdGllcy5zY3NzJyxcblx0XHRcdFx0J3NyYy9zY3NzL3Jvb3Quc2NzcycsXG5cdFx0XHRcdCdzcmMvdXRpbGl0aWVzLWNzcy5qcycsXG5cdFx0XHRcdCdzcmMvYnV0dG9ucy1jc3MuanMnLFxuXHRcdFx0XHQnc3JjL3Jvb3QtY3NzLmpzJyxcblx0XHRcdFx0J3NyYy9mb3Jtcy1jc3MuanMnLFxuXHRcdFx0XHQnc3JjL21vZGFsLWNzcy5qcycsXG5cdFx0XHRcdCdzcmMvYm9vdHN0cmFwLWNzcy5qcycsXG5cdFx0XHRcdCdzcmMvdG9hc3QtY3NzLmpzJyxcblx0XHRcdFx0J3NyYy9tb2RhbC5qcycsXG5cdFx0XHRdLFxuXHRcdFx0Zm9ybWF0czogWydlcyddLFxuXHRcdFx0ZmlsZU5hbWU6IChfLCBlbnRyeSkgPT4gYGJvb3RzdHJhcC8ke2VudHJ5fS5qc2AsXG5cdFx0fSxcblxuXHRcdHJvbGx1cE9wdGlvbnM6IHtcblx0XHRcdG91dHB1dDoge1xuXHRcdFx0XHRwbHVnaW5zOiBbdGVyc2VyKCldLFxuXHRcdFx0XHRhc3NldEZpbGVOYW1lczogJ2Jvb3RzdHJhcC9bbmFtZV0uW2V4dF0nLFxuXHRcdFx0fSxcblx0XHR9LFxuXHR9LFxuXHRyZXNvbHZlOiB7XG5cdFx0YWxpYXM6IFtcblx0XHRcdHtcblx0XHRcdFx0ZmluZDogJ0Bib290c3RyYXAnLFxuXHRcdFx0XHRyZXBsYWNlbWVudDogcGF0aC5yZXNvbHZlKF9fZGlybmFtZSwgJ25vZGVfbW9kdWxlcy9ib290c3RyYXAnKSxcblx0XHRcdH0sXG5cdFx0XHR7XG5cdFx0XHRcdGZpbmQ6ICdAc3JjJyxcblx0XHRcdFx0cmVwbGFjZW1lbnQ6IHBhdGgucmVzb2x2ZShfX2Rpcm5hbWUsICdzcmMnKSxcblx0XHRcdH0sXG5cdFx0XHR7XG5cdFx0XHRcdGZpbmQ6ICdAcG9wcGVyanMvY29yZScsXG5cdFx0XHRcdHJlcGxhY2VtZW50OiAnLi4vLi4vdmVuZG9yL25vZGVfbW9kdWxlcy9AcG9wcGVyanMvY29yZV8yLjExLjYnLFxuXHRcdFx0fSxcblx0XHRdLFxuXHR9LFxufSk7XG4iXSwKICAibWFwcGluZ3MiOiAiO0FBRUEsU0FBUyxvQkFBb0I7QUFDN0IsT0FBTyxZQUFZO0FBQ25CLE9BQU8sVUFBVTtBQUpqQixJQUFNLG1DQUFtQztBQU16QyxJQUFPLHNCQUFRLGFBQWE7QUFBQSxFQUMzQixPQUFPO0FBQUEsSUFDTixRQUFRO0FBQUEsSUFDUixjQUFjO0FBQUEsSUFDZCxRQUFRO0FBQUEsSUFDUixhQUFhO0FBQUEsSUFFYixLQUFLO0FBQUEsTUFDSixPQUFPO0FBQUEsUUFDTjtBQUFBLFFBQ0E7QUFBQSxRQUNBO0FBQUEsUUFDQTtBQUFBLFFBQ0E7QUFBQSxRQUNBO0FBQUEsUUFDQTtBQUFBLFFBQ0E7QUFBQSxRQUNBO0FBQUEsUUFDQTtBQUFBLE1BQ0Q7QUFBQSxNQUNBLFNBQVMsQ0FBQyxJQUFJO0FBQUEsTUFDZCxVQUFVLENBQUMsR0FBRyxVQUFVLGFBQWE7QUFBQSxJQUN0QztBQUFBLElBRUEsZUFBZTtBQUFBLE1BQ2QsUUFBUTtBQUFBLFFBQ1AsU0FBUyxDQUFDLE9BQU8sQ0FBQztBQUFBLFFBQ2xCLGdCQUFnQjtBQUFBLE1BQ2pCO0FBQUEsSUFDRDtBQUFBLEVBQ0Q7QUFBQSxFQUNBLFNBQVM7QUFBQSxJQUNSLE9BQU87QUFBQSxNQUNOO0FBQUEsUUFDQyxNQUFNO0FBQUEsUUFDTixhQUFhLEtBQUssUUFBUSxrQ0FBVyx3QkFBd0I7QUFBQSxNQUM5RDtBQUFBLE1BQ0E7QUFBQSxRQUNDLE1BQU07QUFBQSxRQUNOLGFBQWEsS0FBSyxRQUFRLGtDQUFXLEtBQUs7QUFBQSxNQUMzQztBQUFBLE1BQ0E7QUFBQSxRQUNDLE1BQU07QUFBQSxRQUNOLGFBQWE7QUFBQSxNQUNkO0FBQUEsSUFDRDtBQUFBLEVBQ0Q7QUFDRCxDQUFDOyIsCiAgIm5hbWVzIjogW10KfQo=
