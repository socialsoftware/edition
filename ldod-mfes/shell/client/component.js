class HtmlComponent extends HTMLElement {
	constructor({ onConnected, render, onMount }) {
		super();
	}

	connectedCallnack(onConnected) {
		onConnected();
		this.render();
	}

	render(render) {
		render();
		this.componentDidMount();
	}

	componentDidMount(onComponentDidMount) {
		onComponentDidMount();
	}
}
