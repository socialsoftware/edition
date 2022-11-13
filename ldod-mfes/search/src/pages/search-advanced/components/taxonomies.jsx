export default ({ root, form }) => {
  return (
    <div class="form-floating">
      <input
        name="taxonomies"
        type="search"
        class="form-control"
        placeholder="taxonomies"
        required
      />
      <label data-search-key="taxonomies">
        {root.getConstants('taxonomies')}
      </label>
    </div>
  );
};
