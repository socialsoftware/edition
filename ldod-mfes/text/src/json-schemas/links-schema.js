/** @format */

export default {
	type: 'object',
	required: ['links'],
	properties: {
		links: {
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
		},
	},
};
