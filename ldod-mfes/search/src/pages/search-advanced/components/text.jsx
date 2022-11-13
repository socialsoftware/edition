export default ({ root, form }) => {
  return (
    <div class="form-floating">
      <input
        name="text"
        type="search"
        class="form-control"
        placeholder="keyword"
        required
      />
      <label data-search-key="keyword">{root.getConstants('keyword')}</label>
    </div>
  );
};
