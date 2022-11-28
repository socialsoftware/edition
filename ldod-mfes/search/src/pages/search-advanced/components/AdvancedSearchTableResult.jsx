import { text } from '../../../externalDeps';
import constants from '../constants';

import.meta.env.DEV
  ? await import('shared/table-dev.js')
  : await import('shared/table.js');

const getTableData = (root) => {
  return root.inters.map((inter) => {
    const criterias = root.criteriaNumber.reduce((prev, curr, index) => {
      let c = ++index;
      prev[`C${c}`] = inter.options.map((i) => ++i).includes(c) ? 'X' : '';
      return prev;
    }, {});

    return {
      externalId: inter.externalId,
      data: () => ({
        fragments: (
          <a is="nav-to" to={text.fragment?.(inter.xmlId)}>
            {inter.fragTitle}
          </a>
        ),
        interpretations: (
          <a is="nav-to" to={text.fragmentInter(inter.xmlId, inter.urlId)}>
            {inter.title}
          </a>
        ),
        ...criterias,
        sourceTypeHeader: inter.sourceType,
        sourcesHeader: (
          <span data-search-key={inter.sources}>
            {root.getConstants(inter.sources)}
          </span>
        ),
        hasLdoDMarkHeader: inter.hasLdoDMark
          ? root.getConstants('yes')
          : root.getConstants('no'),
        dateHeader: inter.date,
        editionHeader: inter.editor,
        heteronymHeader:
          inter.heteronym === 'não atribuído'
            ? root.getConstants(inter.heteronym)
            : inter.heteronym,
      }),
      search: JSON.stringify(Object.values(inter).join(', ')),
    };
  });
};

export default ({ root }) => {
  const headers = [
    'fragments',
    'interpretations',
    ...root.criteriaNumber.map((i) => `C${++i}`),
    'sourceTypeHeader',
    'sourcesHeader',
    'hasLdoDMarkHeader',
    'editionHeader',
    'heteronymHeader',
    'dateHeader',
  ];
  return (
    <ldod-table
      id="search-advanced"
      classes="table table-bordered table-hover"
      headers={headers}
      data={getTableData(root)}
      constants={constants(root.fragsNumber, root.intersNumber)[root.language]}
      data-searchkey="externalId"></ldod-table>
  );
};