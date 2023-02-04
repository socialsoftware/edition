self.onmessage = async ({ data: { type, url } }) => {
	(await fetch(url)).arrayBuffer().then(res => postMessage({ type, url, res }));
};
