export const selectPure = import.meta.env.DEV
	? import('select-pure_2.1.4/dist/index.js')
	: import('@vendor/select-pure_2.1.4/dist/index.js');
