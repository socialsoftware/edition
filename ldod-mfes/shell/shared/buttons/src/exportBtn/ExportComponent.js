import { parseHTML } from '@dist/utils.js';
window.html = String.raw;

export default ({ node }) => {
  const btn = parseHTML(html`
    <button class="btn btn-primary" type="button" id="exportBtn">
      <span label>${node.title}</span>
      <span class="icon icon-export"></span>
    </button>
  `);
  btn.onclick = node.handleSubmit;
  return btn;
};
