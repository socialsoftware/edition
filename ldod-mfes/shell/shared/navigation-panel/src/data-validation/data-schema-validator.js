/** @format */

import { ldodValidator } from '@shared/ldod-events.js';
import inputSchema from './input-schema.json';
import dataSchema from './data-schema.json';
import intersSchema from './inters-data-schema.json';
import interSchema from './inter-data-schema.json';

ldodValidator.addSchema(dataSchema, '/data');
ldodValidator.addSchema(intersSchema, '/inters');
ldodValidator.addSchema(interSchema, '/inter');

export { ldodValidator, inputSchema };
