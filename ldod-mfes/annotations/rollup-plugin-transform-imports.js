import pkg from './package.json';

export default function transformImports() {
  return {
    name: 'transform-imports',

    renderChunk(code) {
      Object.keys(pkg.externalDependencies).forEach((dep) => {
        code = code.replaceAll(`import '${dep}`, `import 'vendor/${dep}`);
        code = code.replaceAll(`import('${dep}`, `import('vendor/${dep}`);
      });
      return { code, map: null };
    },
  };
}
