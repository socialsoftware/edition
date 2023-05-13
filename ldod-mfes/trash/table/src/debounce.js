let timeoutId;

function clear() {
	clearTimeout(timeoutId);
	timeoutId = undefined;
}

export function debounce(delay, fn) {
	if (timeoutId) clear();
	timeoutId = setTimeout(() => {
		fn();
		clear();
	}, delay);
}
