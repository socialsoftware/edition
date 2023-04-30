/** @format */

export default ({ root, clazz, key }) => {
	return /*html*/ `
		<div class="well authorialStyle" id="transcriptionContainer">
			${
				clazz
					? /*html*/ `<p class="${clazz}">${root.data.transcriptions[key]}</p>`
					: root.data.transcriptions[0]
			}
		</div>
	`;
};
