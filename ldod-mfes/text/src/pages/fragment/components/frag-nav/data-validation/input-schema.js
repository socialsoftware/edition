/** @format */

export default {
	type: 'object',
	properties: {
		type: {
			type: 'string',
		},
		tooltip: {
			type: 'string',
		},
		data: {
			type: 'array',
			items: {
				id: '/data',
				type: 'object',
				properties: {
					name: {
						type: 'string',
					},
					url: {
						type: 'string',
					},
					inters: {
						type: 'array',
						items: {
							id: '/inters',
							type: 'object',
							properties: {
								title: {
									type: 'string',
								},
								externalId: {
									type: 'string',
								},
								current: {
									id: '/inter',
									type: 'object',
									properties: {
										xmlId: {
											type: 'string',
										},
										urlId: {
											type: 'string',
										},
										name: {
											type: 'string',
										},
									},
									required: ['xmlId', 'urlId'],
								},
								checked: {
									type: 'boolean',
								},
							},
							required: ['externalId', 'current', 'checked'],
						},
					},
				},
			},
		},
	},
	required: ['type'],
};
