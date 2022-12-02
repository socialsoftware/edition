import { text } from '@src/externalDeps';
import { isDev } from '@src/utils';
import AddFragments from './AddFragments';

let searchLoaded;
const loadSearch = () => {
  if (searchLoaded) return;
  import('search').then(({ loadSearchService }) => {
    loadSearchService();
    searchLoaded = true;
  });
};

const getCurrIndex = (node, id) =>
  node.inters.map(({ externalId }) => externalId).indexOf(id);

const getNewIndex = (node, id, up) => {
  const currIndex = getCurrIndex(node, id);
  return up
    ? currIndex > node.getFirstVisibleRowIndex()
      ? node.getPrevVisibleRowIndex(id)
      : currIndex
    : currIndex < node.getLastVisibleRowIndex()
    ? node.getNextVisibleRowIndex(id)
    : currIndex;
};

const onTop = (node, externalId, reverse = false) => {
  const oldIndex = getCurrIndex(node, externalId);
  const newIndex = reverse
    ? node.getLastVisibleRowIndex()
    : node.getFirstVisibleRowIndex();
  if (oldIndex === newIndex) return;
  node.rowUpdate({ externalId, newIndex, oldIndex, changed: true });
};

const onRemove = (node, externalId) => {
  node.rowHide({ externalId, hidden: true });
};

export const addTableRow = (node, inter, index) => {
  const useInter = inter.usedList[0];
  const id = inter.externalId;
  return {
    externalId: id,
    newNumber: ++index,
    number: inter.number,
    title: inter.title,
    actions: (
      <div
        style={{
          display: 'flex',
          justifyContent: 'center',
          gap: '8px',
        }}>
        <span
          class="icon top-icon"
          title="Move to top"
          onClick={() => onTop(node, id)}></span>
        <span
          class="icon bottom-icon"
          title="Move to bottom"
          onClick={() => onTop(node, id, true)}></span>
        <span
          class="icon up-icon"
          title="Move up"
          onClick={() =>
            node.changePosition(
              id,
              getCurrIndex(node, id),
              getNewIndex(node, id, true)
            )
          }></span>
        <span
          class="icon down-icon"
          title="Move down"
          onClick={() =>
            node.changePosition(
              id,
              getCurrIndex(node, id),
              getNewIndex(node, id, false)
            )
          }></span>
        <span
          class="icon icon-trash"
          title="Remove fragment"
          onClick={() => onRemove(node, id)}></span>
      </div>
    ),
    useEdition: (
      <a
        target="_blank"
        is="nav-to"
        to={`${isDev() ? '' : '/ldod-mfes'}${text.fragmentInter(
          useInter.xmlId,
          useInter.urlId
        )}`}>
        {`-> ${useInter.shortName}`}
      </a>
    ),
    search: JSON.stringify(inter),
  };
};

const getTableData = (node) => {
  return node.inters.map((inter, index) => addTableRow(node, inter, index));
};

const onAddFragments = (node) => {
  loadSearch();
  node
    .querySelector('ldod-modal#virtual-addFragmentsModal')
    .toggleAttribute('show');
};

export default ({ node }) => {
  return (
    <div>
      <div class="button-container">
        <button class="btn btn-primary" onClick={() => onAddFragments(node)}>
          {node.getConstants('addFrags')}
        </button>
      </div>

      <ldod-table
        id="virtual-manualTable"
        classes="table  table-hover"
        headers={node.constants.manualHeaders}
        data={getTableData(node)}
        data-rows={node.inters.length}
        constants={node.constants[node.language]}
        data-searchkey="externalId"></ldod-table>
      <AddFragments node={node} />
    </div>
  );
};
