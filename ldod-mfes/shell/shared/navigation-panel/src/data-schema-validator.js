import { ldodValidator } from '@shared/ldod-events.js';
import gridSchema from './grids-data-schema.json';
import langSchema from './lang-data-schema.json';
import schema from './navigation-data-schema.json';

ldodValidator.addSchema(gridSchema, '/grids');
ldodValidator.addSchema(langSchema, '/lang');

export { ldodValidator, schema };
