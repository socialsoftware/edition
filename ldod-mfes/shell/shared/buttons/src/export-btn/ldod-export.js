import { parseHTML } from 'shared/utils.js';
import { html } from '../utils';

export default ({ node }) => {
  const btn = parseHTML(html`
    <button class="btn btn-primary ellipsis" type="button" id="exportBtn">
      <span class="icon icon-export"></span>
      <span label>${node.title}</span>
    </button>
  `);
  if (node.width) btn.style.width = node.width;
  btn.onclick = node.handleSubmit;
  return btn;
};
