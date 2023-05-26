/** @format */

import { undo } from './utils.js';
import { writeFileSync } from 'node:fs';

export default name => {
	try {
		writeFileSync(`${name}/package.json`, JSON.stringify(getPackageJSON(name)));
		console.log('installing dependencies');
	} catch (error) {
		console.error(error.toString());
		undo(name);
	}
};

function getPackageJSON(name) {
	return {
		name,
		version: '1.0.0',
		license: 'MIT',
		private: true,
		type: 'module',
		entry: 'index.js',
		config: {
			docker: 'http://localhost:8080/ldod-mfes',
			dev: 'http://localhost:9000/ldod-mfes',
		},
		scripts: {
			dev: 'vite',
			'build:manifest': 'node ./scripts/manifest-deps.js',
			build: 'vite build && yarn build:manifest',
			pack: `yarn build && ./scripts/pack.sh ${name}`,
			'publish-dev': `yarn run pack && ./scripts/publish.sh ${name} ${name}.js @./dist/${name}.tgz $npm_package_config_dev`,
			'unpublish-dev': `./scripts/unpublish.sh ${name} $npm_package_config_dev`,
			publish: `yarn run pack && ./scripts/publish.sh ${name} ${name}.js @./dist/${name}.tgz $npm_package_config_docker`,
			unpublish: `./scripts/unpublish.sh ${name} $npm_package_config_docker`,
		},
		devDependencies: {
			vite: 'latest',
			'@rollup/plugin-terser': 'latest',
			'@core': 'file:../shell/shared/dist',
		},
		dependencies: {},
		externalDependencies: {},
		importmaps: {},
	};
}
