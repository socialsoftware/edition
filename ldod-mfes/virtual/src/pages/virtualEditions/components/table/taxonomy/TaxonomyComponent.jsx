import { isDev } from '../../../../../virtualRouter';
import { DeleteButton, MergeButton } from './MergeDeleteButtons';
import TaxonomyTable from './TaxonomyTable';

const onPublicContent = (target, node) => {
  const popover = node.querySelector('ldod-popover');
  popover.element = () => (
    <div id="test">
      <ul class="drop-menu" style={{ minWidth: `${target.clientWidth}px` }}>
        <li>
          <a
            target="_blank"
            class="drop-item"
            is="nav-to"
            to={`${isDev() ? '' : '/ldod-mfes'}/virtual/edition/acronym/${
              node.taxonomy.veAcronym
            }`}>
            {node.getConstants('edition')}
          </a>
        </li>
      </ul>
    </div>
  );

  popover.target = target;
  popover.toggleAttribute('show');
};

export default ({ node }) => {
  return (
    <div style={{ padding: '20px' }} id="taxonomyComponent">
      <div id="virtual-categoryActionsContainer" class="flex-center">
        <form onSubmit={node.onAddCategory}>
          <div id="catInputName" class="input-group">
            <input
              required
              id="addCategoryInput"
              name="name"
              type="text"
              class="form-control"
              placeholder={node.getConstants('categoryName')}
              aria-label="New category's name"
              aria-describedby="category-add-button"
            />
            <button
              id="addCategory"
              type="submit"
              class="btn btn-sm btn-primary">
              <span class="icon icon-plus"></span>
              <span data-virtual-key="addCategory">
                {node.getConstants('addCategory')}
              </span>
            </button>
          </div>
        </form>
        <MergeButton node={node} />
        <button
          id="generateTopics"
          type="button"
          class="btn btn-sm btn-primary"
          onClick={node.onGenerateTopics}>
          <span class="icon icon-plus"></span>
          <span data-virtual-key="generateTopics">
            {node.getConstants('generateTopics')}
          </span>
        </button>
        <DeleteButton node={node} />
        <button
          id="publicContent"
          class="btn btn-sm btn-secondary"
          type="button"
          open-popover
          onClick={({ target }) => onPublicContent(target, node)}>
          Public content
        </button>
      </div>
      {['addCategory', 'generateTopics'].map((id) => (
        <ldod-tooltip
          data-ref={`button#${id}`}
          data-virtual-tooltip-key={`${id}Info`}
          placement="top"
          width="300px"
          content={node.getConstants(`${id}Info`)}></ldod-tooltip>
      ))}
      <ldod-popover data-distance="0"></ldod-popover>
      <TaxonomyTable node={node} />
    </div>
  );
};
