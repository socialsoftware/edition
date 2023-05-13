/** @format */

export const loadImages = target => {
	target.querySelectorAll('img').forEach(img => {
		img.onload = () => {
			img.style.opacity = '1';
			const prev = img.previousElementSibling;
			if (prev instanceof HTMLImageElement) return;
			prev.remove();
		};
		img.src = getUrl(img.id);
	});
};
function getUrl(path) {
	const url = `${import.meta.env.VITE_BASE}resources/${path}`;
	return new URL(url, import.meta.url).href;
}
