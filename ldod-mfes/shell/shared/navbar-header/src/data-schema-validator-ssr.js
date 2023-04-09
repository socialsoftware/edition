/** @format */

import headerSchema from './header-data-schema.json';
import { ldodValidator } from '../../dist/ldod-events';

export const headerDataSchemaValidator = data => {
	const errors = ldodValidator.validate(data, headerSchema).errors;
	if (errors.length) {
		errors.forEach(error => console.error(error));
		return false;
	}
	return true;
};
