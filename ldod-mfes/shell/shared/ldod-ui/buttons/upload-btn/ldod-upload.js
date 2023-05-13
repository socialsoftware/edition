export default ({ title, isMultiple }) => {
  return /*html*/ `
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
          <span label>${title}</span>
          <span class="icon icon-upload"></span>
        </button>
      </div>
    </form>
  `;
};
