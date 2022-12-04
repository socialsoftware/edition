import { reading } from '@src/externalDeps';
import textReferences from '@src/references';

export const getTableData = (node) => {
  return node.data?.map((frag) => {
    return {
      externalId: frag.externalId,
      data: () => ({
        number: +frag.number,
        title: (
          <a is="nav-to" to={textReferences.fragment(frag.xmlId)}>
            {frag.title}
          </a>
        ),
        reading: (
          <a
            is="nav-to"
            to={reading.editionInterPath?.(frag.xmlId, frag.urlId)}>
            <span class="icon icon-eye"></span>
          </a>
        ),
        volume: frag.volume,
        page: frag.startPage,
        date: frag.date,
        heteronym: frag.heteronym || (
          <span data-key="notAssigned">{node.getConstants('notAssigned')}</span>
        ),
      }),
      search: JSON.stringify(frag),
    };
  });
};
