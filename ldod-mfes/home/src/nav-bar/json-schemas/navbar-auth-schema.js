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
			required: ['links'],
			properties: {
				links: {
					type: 'array',
					items: {
						type: 'object',
						required: ['key', 'path'],
						properties: {
							key: {
								type: 'string',
							},
							path: {
								type: 'string',
							},
						},
					},
				},
				constants,
			},
		},
		isAuth: { type: 'boolean' },
	},
};
