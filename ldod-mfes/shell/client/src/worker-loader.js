/** @format */

const worker = new Worker('/ldod-mfes/shell/worker.js', { type: 'module' });

const FONTS_URL = '/ldod-mfes/shell/style/fonts';
const ROOT_CSS_URL = '/ldod-mfes/shared/ui/bootstrap/root.css';
const fonts = [
	{
		family: 'Work-Sans',
		src: `${FONTS_URL}/WorkSans-VariableFont_wght.ttf`,
		style: 'normal',
		weight: '200 800',
		display: 'optional',
	},

	{
		family: 'Space-Mono',
		src: `${FONTS_URL}/space-mono/SpaceMono-Bold.ttf`,
		style: 'normal',
		weight: 700,
		display: 'swap',
	},
	{
		family: 'Ultra',
		src: `${FONTS_URL}/ultra/Ultra-Regular.ttf`,
		style: 'normal',
		weight: 400,
		display: 'swap',
	},
	{
		family: 'League-Gothic',
		src: `${FONTS_URL}/league-gothic/LeagueGothic-Regular.otf`,
		style: 'normal',
		weight: 400,
		display: 'swap',
	},
	{
		family: 'League-Gothic-Condensed',
		src: `${FONTS_URL}/league-gothic/LeagueGothic-CondensedRegular.otf`,
		style: 'normal',
		weight: 400,
		display: 'swap',
	},

	{
		family: 'Space-Mono',
		src: `${FONTS_URL}/space-mono/SpaceMono-Regular.ttf`,
		style: 'normal',
		weight: 400,
		display: 'swap',
	},
];

fonts.forEach(font => {
	worker.postMessage({ type: 'font', url: font.src });
});
worker.postMessage({ type: 'css', url: ROOT_CSS_URL });

//worker.postMessage({ type: 'script', url: '/ldod-mfes/shared/notifications.js' });
//worker.postMessage({ type: 'script', url: '/ldod-mfes/shared/ldod-icons.js' });
//worker.postMessage({ type: 'script', url: '/ldod-mfes/references.js' });

const workerHandler = {
	font: (res, url) => onFont(res, url),
	css: (res, url) => onCss(res),
	script: (res, url) => onScript(res),
};

worker.onmessage = ({ data: { type, url, res } }) => workerHandler[type](res, url);

function onFont(res, url) {
	const { family, display, style, weight } = fonts.find(font => font.src === url);
	document.fonts.add(
		new FontFace(family, res, {
			style,
			weight,
			display,
		})
	);
}
function onCss(res) {
	const style = document.createElement('style');
	style.innerHTML = new TextDecoder().decode(res);
	document.head.appendChild(style);
}

function onScript(res) {
	const script = document.createElement('script');
	script.type = 'module';
	script.innerHTML = new TextDecoder().decode(res);
	document.body.appendChild(script);
}
