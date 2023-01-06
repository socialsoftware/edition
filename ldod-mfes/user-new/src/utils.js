export function capitalizeFirstLetter(word) {
	return word.slice(0, 1).toUpperCase().concat(word.slice(1, word.length));
}

export const isDev = () => import.meta.env.DEV;
