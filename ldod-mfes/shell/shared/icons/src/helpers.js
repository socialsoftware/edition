const parser = new DOMParser();
export const getIconSVG = iconRaw => parser.parseFromString(iconRaw, 'image/svg+xml');

export const iconSVGLoader = async iconName => {
	const iconRaw = import.meta.env.DEV
		? (await import(/* @vite-ignore */ `../src/js/${iconName}.js`)).default
		: (await import(/* @vite-ignore */ `@shared/icons/${iconName}.js`)).default;
	return getIconSVG(iconRaw).firstChild;
};
