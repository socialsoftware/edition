import Heteronym from './heteronymWebComp';
import DateSelect from './dateWebComp';

const onChangeEdition = (target, form) => {
  form.querySelectorAll('heteronym-select, date-select').forEach((node) => {
    node.setAttribute('edition', target.value);
  });
};

export default ({ root, form }) => {
  const editions = root.data.editions;

  return (
    <>
      <div class="form-floating">
        <select name="inclusion" class="form-select">
          <option value="in" data-search-key="includedIn">
            {root.getConstants('includedIn')}
          </option>
          <option value="out" data-search-key="excludedFrom">
            {root.getConstants('excludedFrom')}
          </option>
        </select>
        <label data-search-key="editionInclusion">
          {root.getConstants('editionInclusion')}
        </label>
      </div>
      <div class="form-floating">
        <select
          name="edition"
          class="form-select"
          onChange={({ target }) => onChangeEdition(target, form)}>
          <option value="all" data-search-key="all">
            {root.getConstants('all')}
          </option>
          {editions.map((ed) => (
            <option value={ed.acronym}>{ed.editor}</option>
          ))}
        </select>
        <label data-search-key="editions">
          {root.getConstants('editions')}
        </label>
      </div>
      {new Heteronym(root, form)}
      {new DateSelect(root, form)}
    </>
  );
};
