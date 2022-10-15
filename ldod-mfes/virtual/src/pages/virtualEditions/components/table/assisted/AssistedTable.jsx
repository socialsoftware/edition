import { setLinearVE } from '../../../../../apiRequests';

const initialProperties = [
  { type: 'heteronym', weight: '0' },
  { type: 'date', weight: '0' },
  { type: 'text', weight: '0' },
  { type: 'taxonomy', weight: '0' },
];

let properties = initialProperties;

export const useProperties = () => {
  const getProperties = (acronym) =>
    (properties ?? initialProperties).map((prop) => ({ ...prop, acronym }));
  const setProps = (props = initialProperties) => (properties = props);
  return [getProperties, setProps];
};
const [getProperties, setProps] = useProperties();

const onSetAsInitial = async (node, inter) => {
  setProps(node.properties);
  const data = await setLinearVE(node.edition.externalId, {
    selected: inter.externalId,
    properties: getProperties(inter.shortName),
  });

  node.updateData(data);
};

const getTableData = (node) => {
  return node.inters.map((inter) => {
    const useInter = inter.usedList[0];
    return {
      externalId: inter.externalId,
      number: inter.number,
      title: inter.title,
      selected:
        node.selected !== inter.externalId ? (
          <button
            type="button"
            class="btn btn-outline-primary btn-sm"
            onClick={() => onSetAsInitial(node, inter)}>
            <span class="icon icon-check" style={{ marginRight: '8px' }}></span>
            {node.getConstants('defineFirst')}
          </button>
        ) : (
          ''
        ),
      useEdition: (
        <a
          target="_blank"
          is="nav-to"
          to={`/text/fragment/${useInter.xmlId}/inter/${useInter.urlId}`}>
          {`-> ${useInter.shortName}`}
        </a>
      ),
      search: JSON.stringify(inter),
    };
  });
};

export default ({ node }) => {
  return (
    <ldod-table
      language={node.language}
      id="virtual-assistedTable"
      classes="table  table-hover"
      headers={node.constants.assistedHeaders}
      data={getTableData(node)}
      constants={node.constants}
      data-searchkey="externalId"></ldod-table>
  );
};
