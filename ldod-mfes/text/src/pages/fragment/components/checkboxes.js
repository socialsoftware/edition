/** @format */
const root = () => customElements.get('frag-inter').instance;
customElements.define(
	'transcription-cb',
	class TranscriptionCB extends HTMLInputElement {
		constructor() {
			super();
			this.type = 'checkbox';
			this.id = `checkboxTranscription-${this.name}`;
			this.addEventListener('change', root().handleTranscriptionCheckboxChange);
		}
	},
	{
		extends: 'input',
	}
);

export default ({ root, checkboxes }) => {
	return /*html*/ `
    <div id="text-checkBoxesContainer" class="interCheckboxes">
      ${Object.entries(root.transcriptionCheckboxes)
			.map(([cb, checked], index) => {
				if (checkboxes.includes(cb)) return getTranscriptionCBs(root, cb, checked, index);
			})
			.join('')}
    </div>
  `;
};

function getTranscriptionCBs(root, cb, checked, index) {
	return /*html*/ ` 
    <div id="${cb}" key="${index}">

      <input is="transcription-cb" name="${cb}" ${checked ? 'checked' : ''} />
      <label data-key="${cb}" for="${`checkboxTranscription-${cb}`}">
        ${root.getConstants(cb)}
      </label>
    </div>`;
}
