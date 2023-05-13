import constants from '../../constants';
export default ({ node }) => {
  return (
    <form onSubmit={node.searchRequest}>
      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          gap: '20px',
          flexWrap: 'wrap',
        }}>
        <div class="form-floating">
          <input
            id="searchTerm"
            class="form-control"
            type="search"
            name="searchTerm"
            required
            placeholder="searchFor"
          />
          <label data-search-key="searchFor">
            {node.getConstants('searchFor')}
          </label>
        </div>
        <div class="form-floating">
          <select name="searchType" class="form-select" id="searchType">
            <option data-search-key="completeSearch" value="" selected>
              {node.getConstants('completeSearch')}
            </option>
            <option data-search-key="titleSearch" value="title">
              {node.getConstants('titleSearch')}
            </option>
          </select>
          <label data-search-key="searchType">
            {node.getConstants('searchType')}
          </label>
        </div>

        <div class="form-floating">
          <select name="searchSource" class="form-select" id="source">
            <option value="" data-search-key="all">
              All
            </option>
            <option value="Coelho">{constants().jpc}</option>
            <option value="Cunha">{constants().tsc}</option>
            <option value="Pizarro">{constants().jp}</option>
            <option value="Zenith">{constants().rz}</option>
            <option value="BNP" data-search-key="witnesses">
              {node.getConstants('witnesses')}
            </option>
          </select>
          <label data-search-key="searchSource">
            {node.getConstants('searchSource')}
          </label>
        </div>

        <button type="submit" class="btn btn-outline-secondary">
          <span class="icon icon-magnifying-glass"></span>
          search
        </button>
      </div>
    </form>
  );
};
