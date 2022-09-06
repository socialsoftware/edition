import { parseHTML } from '@dist/utils.js';
window.html = String.raw;

export default ({ node, isMultiple }) => {
  const form = parseHTML(html`
    <form enctype="multipart/form-data">
      <div class="input-group">
        <input
          ${isMultiple ? 'multiple' : ''}
          required
          type="file"
          name=${isMultiple ? 'files' : 'file'}
          class="form-control"
          id="inputFile"
          accept=".xml"
        />
        <button class="btn btn-primary" type="submit" id="loadBtn" disabled>
          <span label>${node.title}</span>
          <span class="icon icon-upload"></span>
        </button>
      </div>
    </form>
  `);

  form.onsubmit = node.handleSubmit;
  form.oninput = node.handleInput;

  return form;
};
