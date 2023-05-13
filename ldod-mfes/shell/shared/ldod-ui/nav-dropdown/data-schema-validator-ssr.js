/** @format */

import headerSchema from './data-schema.json';
import '../../event-bus/dist/index.umd.min.js';
export const headerDataSchemaValidator = data => {
	const errors = globalThis.validate.validate(data, headerSchema).errors;
	if (errors.length) {
		errors.forEach(error => console.error(error));
		return false;
	}
	return true;
};
