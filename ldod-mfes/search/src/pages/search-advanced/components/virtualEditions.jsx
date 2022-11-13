export default ({ root, form }) => {
  return (
    <div id="wrapper">
      <div class="form-floating">
        <select name="veInclusion" class="form-select">
          <option value="includedIn" data-search-key="includedIn">
            {root.getConstants('includedIn')}
          </option>
          <option value="excludedFrom" data-search-key="excludedFrom">
            {root.getConstants('excludedFrom')}
          </option>
        </select>
        <label data-search-key="veInclusion">
          {root.getConstants('veInclusion')}
        </label>
      </div>
      <div class="form-floating">
        <select name="virtualEdition" class="form-select">
          <option value="all" data-search-key="all">
            {root.getConstants('all')}
          </option>
          {root.data.virtualEditions.map((ve) => (
            <option value={ve.acronym}>{ve.title}</option>
          ))}
        </select>
        <label data-search-key="virtualEdition">
          {root.getConstants('virtualEdition')}
        </label>
      </div>
    </div>
  );
};
