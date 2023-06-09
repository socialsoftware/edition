/** @format */

export default {
	type: 'array',
	items: {
		type: 'object',
		required: ['key', 'route'],
		properties: {
			key: {
				type: 'string',
			},
			route: {
				type: 'string',
			},
		},
	},
};
