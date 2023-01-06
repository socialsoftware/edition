const parser = new DOMParser();
export const getIconSVG = iconRaw => parser.parseFromString(iconRaw, 'image/svg+xml');

export const iconSVGLoader = async iconName => {
	const iconRaw =
		typeof window !== 'undefined' && window.LDOD_PRODUCTION
			? (await import(/* @vite-ignore */ `@shared/icons/${iconName}.js`)).default
			: (await import(/* @vite-ignore */ `./icons/${iconName}.js`)).default;
	return getIconSVG(iconRaw).firstChild;
};
