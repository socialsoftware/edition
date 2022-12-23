import textReferences from '@src/references';
import { getExpertEdition } from './expert-edition';
import { getSourceInter } from './source-inter.jsx';

export const getTableData = (node) => {
  return node.fragments?.map((frag, index) => {
    return {
      externalId: frag.externalId,
      data: () => ({
        title: (
          <a is="nav-to" to={textReferences.fragment(frag.xmlId)}>
            {frag.title}
          </a>
        ),
        jpc: getExpertEdition(frag.expertsInterMap.JPC, node),
        tsc: getExpertEdition(frag.expertsInterMap.TSC, node),
        rz: getExpertEdition(frag.expertsInterMap.RZ, node),
        jp: getExpertEdition(frag.expertsInterMap.JP, node),
        wit1: getSourceInter(
          frag.sortedSourceInterList[0] ?? {},
          node,
          `wit-1${index}`
        ),
        wit2: getSourceInter(
          frag.sortedSourceInterList[1] ?? {},
          node,
          `wit-2${index}`
        ),
        wit3: getSourceInter(
          frag.sortedSourceInterList[2] ?? {},
          node,
          `wit-3${index}`
        ),
      }),
      search: JSON.stringify(frag),
    };
  });
};
