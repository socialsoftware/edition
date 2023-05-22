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
		links: [
			{ key: 'lit', route: headers.lit() },
			{ key: 'lit-ts', route: headers.litTs() },
			{ key: 'pure', route: headers.pure() },
		],
	},
	constants: {
		pt: {},
		en: {},
		es: {},
	},
};

export { headerData, headers as default };
