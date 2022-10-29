import { isDev } from '../../../../../virtualRouter';
import { DeleteButton, MergeButton } from './MergeDeleteButtons';
import TaxonomyTable from './TaxonomyTable';

const onPublicContent = (target, node) => {
  const popover = node.querySelector('ldod-popover');
  popover.element = () => (
    <>
      <ul class="drop-menu" style={{ minWidth: `${target.clientWidth}px` }}>
        <li>
          <a
            target="_blank"
            class="drop-item"
            is="nav-to"
            to={`${isDev() ? '' : '/ldod-mfes'}/virtual/edition/acronym/${
              node.taxonomy.veAcronym
            }`}>
            {node.taxonomy.veAcronym}
          </a>
        </li>
      </ul>
    </>
  );

  popover.target = target;
  popover.toggleAttribute('show');
};

const onUsedId = (target, node) => {
  const popover = node.querySelector('ldod-popover');
  popover.element = () => (
    <>
      <ul class="drop-menu" style={{ minWidth: `${target.clientWidth}px` }}>
        {node.taxonomy.usedIn.map((ed) => (
          <li>
            <a
              target="_blank"
              class="drop-item"
              is="nav-to"
              to={`${
                isDev() ? '' : '/ldod-mfes'
              }/virtual/edition/acronym/${ed}`}>
              {ed}
            </a>
          </li>
        ))}
      </ul>
    </>
  );

  popover.target = target;
  popover.toggleAttribute('show');
};

export default ({ node }) => {
  return (
    <div style={{ padding: '20px' }} id="taxonomyComponent">
      <div class="flex-center" style={{ marginBottom: '10px' }}>
        <button
          style={{ width: '200px' }}
          id="publicContent"
          class="btn btn-sm btn-secondary"
          type="button"
          open-popover
          onClick={({ target }) => onPublicContent(target, node)}>
          {node.getConstants('publicContent')}
        </button>
        {node.taxonomy.usedIn.length && (
          <button
            style={{ width: '200px' }}
            id="usedIn"
            class="btn btn-sm btn-secondary"
            type="button"
            open-popover
            onClick={({ target }) => onUsedId(target, node)}>
            {node.getConstants('usedIn')}
          </button>
        )}
      </div>
      <div id="virtual-categoryActionsContainer" class="flex-center">
        <form onSubmit={node.onAddCategory}>
          <div id="catInputName" class="input-group">
            <input
              style={{ width: '200px' }}
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
              style={{ width: '200px' }}
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
          style={{ width: '200px' }}
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
