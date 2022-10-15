export default () => {
  return (
    <div style={{ display: 'flex', justifyContent: 'center', gap: '20px' }}>
      <input id="term" type="text" name="searchTerm" placeholder="searchFor" />
      <select name="searchType" id="type">
        <option value="">completeSearch</option>
        <option value="">titleSearch</option>
      </select>
      <select name="searchSource" id="source">
        <option value="jpc">jpc</option>
        <option value="tsc">tsc</option>
        <option value="jp">jp</option>
        <option value="rz">rz</option>
        <option value="witnesses">witnesses</option>
      </select>
      <button type="button">search</button>
    </div>
  );
};
