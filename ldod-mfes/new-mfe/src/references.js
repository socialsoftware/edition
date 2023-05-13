/** @format */

const headers = {
	lit: () => '/new-mfe/lit',
	litTs: () => '/new-mfe/lit-ts',
	pure: () => '/new-mfe/pure',
};

const headerData = {
	name: 'new-mfe',
	data: {
		name: 'new-mfe',
		pages: [
			{ id: 'lit', route: headers.lit() },
			{ id: 'lit-ts', route: headers.litTs() },
			{ id: 'pure', route: headers.pure() },
		],
	},
	constants: {
		pt: {},
		en: {},
		es: {},
	},
};

export { headerData, headers as default };
