export default ({ title }) => {
  return /*html*/ `
    <button class="btn btn-primary ellipsis" type="button" id="exportBtn">
      <span class="icon icon-export"></span>
      <span label>${title}</span>
    </button>
  `;
};
