/** @format */

import headerSchema from './data-schema.json';

export const headerDataSchemaValidator = data => {
	if (!globalThis.validator) return;
	const errors = validator.validate(data, headerSchema).errors;
	if (errors.length) {
		errors.forEach(error => console.error(error));
		return false;
	}
	return true;
};
