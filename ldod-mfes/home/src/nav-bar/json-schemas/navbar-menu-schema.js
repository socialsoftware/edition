/** @format */
import constants from './constants-schema';
export default {
	type: 'object',
	required: ['name', 'data'],
	properties: {
		name: {
			type: 'string',
		},
		data: {
			type: 'object',
			required: ['name', 'links'],
			properties: {
				name: { type: 'string' },
				links: {
					type: 'array',
					items: {
						type: 'object',
						required: ['key'],
						properties: {
							key: {
								type: 'string',
							},
							route: {
								type: 'string',
							},
							link: {
								type: 'string',
							},
						},
					},
				},
			},
		},
		replace: { type: 'boolean' },
		constants,
	},
};
