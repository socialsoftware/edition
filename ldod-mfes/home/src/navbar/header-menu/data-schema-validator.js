/** @format */

import { ldodValidator } from '@shared/ldod-events.js';
import headerSchema from './header-menu-schema.json';
import dataSchema from './data-schema.json';
import constantsSchema from './constants-schema.json';

ldodValidator.addSchema(dataSchema, '/data');
ldodValidator.addSchema(constantsSchema, '/constants');

export const headerDataSchemaValidator = data => ldodValidator.validate(data, headerSchema);
