#### data schema example

```js
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
const data = {
	pt: {
		title: 'Titulo',
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
					'<a>edition link</a>',
					iconRight.cloneNode(true),
					'',
				],
			},
		],
	},
	es: {
		title: 'Titulo',
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
```
