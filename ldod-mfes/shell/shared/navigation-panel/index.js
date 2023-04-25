/** @format */

import './src/frag-nav-panel';

document.querySelectorAll('button[lang]').forEach(btnLang => {
	btnLang.onclick = () =>
		document.querySelectorAll('[language]').forEach(ele => {
			ele.setAttribute('language', btnLang.getAttribute('id'));
		});
});

document.body.querySelector('frag-nav-panel#expert').setAttribute(
	'data-input',
	JSON.stringify({
		type: 'Edições dos Peritos',
		tooltip: 'conteudo',
		data: [
			{
				name: 'Jacinto Prado Coelho',
				acronym: 'JCP',
				inters: [
					{
						title: 'Edition title',
						externalId: 'someId',
						previous: { xmlId: 'Fr234', urlId: 'Frsdaasdgf' },
						current: { xmlId: 'Fr234', urlId: 'Frsdaasdgf', name: '325' },
						next: { xmlId: 'Fr234', urlId: 'Frsdaasdgf' },
						checked: true,
					},
					{
						title: 'Edition title',
						externalId: 'someId',
						previous: { xmlId: 'Fr234', urlId: 'Frsdaasdgf' },
						current: { xmlId: 'Fr234', urlId: 'Frsdaasdgf', name: '325' },
						next: { xmlId: 'Fr234', urlId: 'Frsdaasdgf' },
						checked: false,
					},
				],
			},
			{
				name: 'Jacinto Prado Coelho',
				acronym: 'JCP',
				inters: [
					{
						title: 'Edition title',
						externalId: 'someId',
						previous: { xmlId: 'Fr234', urlId: 'Frsdaasdgf' },
						current: { xmlId: 'Fr234', urlId: 'Frsdaasdgf', name: '325' },
						next: { xmlId: 'Fr234', urlId: 'Frsdaasdgf' },
						checked: false,
					},
					{
						title: 'Edition title',
						externalId: 'someId',
						previous: { xmlId: 'Fr234', urlId: 'Frsdaasdgf' },
						current: { xmlId: 'Fr234', urlId: 'Frsdaasdgf', name: '325' },
						next: { xmlId: 'Fr234', urlId: 'Frsdaasdgf' },
						checked: true,
					},
				],
			},
		],
	})
);

document.body.querySelector('frag-nav-panel').setAttribute(
	'data-input',
	JSON.stringify({
		type: 'Testemunhos',
		data: [
			{
				inters: [
					{
						externalId: 'someId',
						current: { xmlId: 'Fr234', urlId: 'Frsdaasdgf', name: '325' },
						checked: true,
					},
					{
						externalId: 'someId',
						current: { xmlId: 'Fr234', urlId: 'Frsdaasdgf', name: '325' },
						checked: false,
					},
				],
			},
		],
	})
);
