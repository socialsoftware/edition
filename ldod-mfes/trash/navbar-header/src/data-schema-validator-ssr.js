/** @format */

import headerSchema from './header-data-schema.json';
import * as pkg from '../../event-bus/dist/index.umd.min';

const validator = pkg.default.Validator();

export const headerDataSchemaValidator = data => {
	const errors = validator.validate(data, headerSchema).errors;
	if (errors.length) {
		errors.forEach(error => console.error(error));
		return false;
	}
	return true;
};
