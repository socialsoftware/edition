/** @format */

const parser = new DOMParser();
export const getIconSVG = iconRaw => parser.parseFromString(iconRaw, 'image/svg+xml');

export const iconSVGLoader = async iconName => {
	const iconRaw = window.LDOD_PRODUCTION
		? (await import(/* @vite-ignore */ `@ui/icons/${iconName}.js`)).default
		: (await import(/* @vite-ignore */ `./icons/${iconName}.js`)).default;
	return getIconSVG(iconRaw).firstChild;
};
