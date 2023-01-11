(() => {
	const URL = '/ldod-mfes/style/fonts';
	const fonts = [
		{
			family: 'Work-Sans',
			src: `url('${URL}/WorkSans-VariableFont_wght.ttf') format('truetype')`,
			style: 'normal',
			weight: '200 800',
			display: 'optional',
		},

		{
			family: 'Space-Mono',
			src: `url('${URL}/space-mono/SpaceMono-Bold.ttf') format('truetype')`,
			style: 'normal',
			weight: 700,
			display: 'swap',
		},
		{
			family: 'Ultra',
			src: `url('${URL}/ultra/Ultra-Regular.ttf') format('truetype')`,
			style: 'normal',
			weight: 400,
			display: 'swap',
		},
		{
			family: 'League-Gothic',
			src: `url('${URL}/league-gothic/LeagueGothic-Regular.otf') format('truetype')`,
			style: 'normal',
			weight: 400,
			display: 'swap',
		},
		{
			family: 'League-Gothic-Condensed',
			src: `url('${URL}/league-gothic/LeagueGothic-CondensedRegular.otf') format('truetype')`,
			style: 'normal',
			weight: 400,
			display: 'swap',
		},

		{
			family: 'Space-Mono',
			src: `url('${URL}/space-mono/SpaceMono-Regular.ttf') format('truetype')`,
			style: 'normal',
			weight: 400,
			display: 'swap',
		},
	];
	const loader = () => {
		fonts.forEach(({ family, src, style, weight, display }) => {
			let font = new FontFace(family, src, {
				style,
				weight,
				display,
			});
			font.load()
				.then(loaded => document.fonts.add(loaded))
				.catch(e => console.log(e));
		});
		window.dispatchEvent(new Event('fonts-loaded'));
	};

	window.addEventListener('pointermove', loader);
	window.addEventListener('fonts-loaded', () => this.removeEventListener('pointermove', loader));
})();
