import './src/navigation-panel';

document.querySelectorAll('button[lang]').forEach(btnLang => {
	btnLang.onclick = () =>
		document.querySelectorAll('[language]').forEach(ele => {
			ele.setAttribute('language', btnLang.getAttribute('id'));
		});
});

const checkBox = document.createElement('input');
checkBox.type = 'checkbox';

const iconLeft = document.createElement('span', { is: 'ldod-span-icon' });
iconLeft.setAttribute('is', 'ldod-span-icon');
iconLeft.setAttribute('icon', 'chevron-left');
iconLeft.setAttribute('size', '15px');

const iconRight = document.createElement('span', { is: 'ldod-span-icon' });
iconRight.setAttribute('is', 'ldod-span-icon');
iconRight.setAttribute('icon', 'chevron-right');
iconRight.setAttribute('size', '15px');

const ele = document.createElement('ldod-navigation-panel');
ele.setAttribute('language', 'pt');
ele.setAttribute('cells', '6');
ele.data = {
	pt: {
		title: 'Titulo',
		tooltipContent: 'Conteúdo',
		grids: [
			{
				gridTitle: 'Titulo da edição',
				gridData: [
					'',
					checkBox.cloneNode(true),
					iconLeft.cloneNode(true),
					'<a>Link da edição</a>',
					iconRight.cloneNode(true),
					'',
				],
			},
			{
				gridTitle: 'Titulo da edição',
				gridData: [
					'',
					checkBox.cloneNode(true),
					iconLeft.cloneNode(true),
					'<a>Link da edição</a>',
					iconRight.cloneNode(true),
					'',
				],
			},
		],
	},
	en: {
		title: 'Title',
		tooltipContent: 'Content',
		grids: [
			{
				gridTitle: 'Edition title',
				gridData: [
					'',
					checkBox.cloneNode(true),
					iconLeft.cloneNode(true),
					'<a>Edition link</a>',
					iconRight.cloneNode(true),
					'',
				],
			},
			{
				gridTitle: 'Edition title',
				gridData: [
					'',
					checkBox.cloneNode(true),
					iconLeft.cloneNode(true),
					'<a>Edition link</a>',
					iconRight.cloneNode(true),
					'',
				],
			},
		],
	},
	es: {
		title: 'Titulo',
		tooltipContent: 'Conteúdo',
		grids: [
			{
				gridTitle: 'Titulo da edição',
				gridData: [
					'',
					checkBox.cloneNode(true),
					iconLeft.cloneNode(true),
					'<a>Link da edição</a>',
					iconRight.cloneNode(true),
					'',
				],
			},
			{
				gridTitle: 'Titulo da edição',
				gridData: [
					'',
					checkBox.cloneNode(true),
					iconLeft.cloneNode(true),
					'<a>Link da edição</a>',
					iconRight.cloneNode(true),
					'',
				],
			},
		],
	},
};

document.body.appendChild(ele);

const ele4 = document.createElement('ldod-navigation-panel');
ele4.setAttribute('language', 'pt');
ele4.setAttribute('cells', '4');
