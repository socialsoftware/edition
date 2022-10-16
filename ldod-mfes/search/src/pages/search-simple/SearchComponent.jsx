export default ({ node }) => {
  return (
    <form onSubmit={node.searchRequest}>
      <div style={{ display: 'flex', justifyContent: 'center', gap: '20px' }}>
        <input
          id="term"
          type="text"
          name="searchTerm"
          placeholder="searchFor"
          required
        />
        <select name="searchType" id="type">
          <option value="" selected>
            completeSearch
          </option>
          <option value="title">titleSearch</option>
        </select>
        <select name="searchSource" id="source">
          <option value="">All</option>
          <option value="Coelho">jpc</option>
          <option value="Cunha">tsc</option>
          <option value="Pizarro">jp</option>
          <option value="Zenith">rz</option>
          <option value="BNP">witnesses</option>
        </select>
        <button type="submit">search</button>
      </div>
    </form>
  );
};
