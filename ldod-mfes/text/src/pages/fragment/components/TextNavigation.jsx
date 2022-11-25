import { textReferences } from '@src/text';
import TextNavigationTable from './TextNavigationTable';
window.html = String.raw;

const getSourceTableRow = (node, { externalId, xmlId, urlId, shortName }) => {
  return [
    [
      '',
      <input
        name={shortName}
        id={`checkbox-${externalId}`}
        type="checkbox"
        onChange={() => node.handleInterCheckboxChange(externalId, urlId)}
        checked={node.inters.has(externalId)}
      />,
      <a is="nav-to" to={textReferences.fragmentInter(xmlId, urlId)}>
        {shortName}
      </a>,
      '',
      ,
    ],
  ];
};

const getExpertTableRow = (node, expert) => {
  return [
    '',
    <input
      name={expert.urldId}
      id={`checkbox-${expert.externalId}`}
      type="checkbox"
      onChange={() =>
        node.handleInterCheckboxChange(expert.externalId, expert.urlId)
      }
      checked={node.inters.has(expert.externalId)}
    />,
    <a
      is="nav-to"
      to={textReferences.fragmentInter(expert.prevXmlId, expert.prevUrlId)}>
      <span class="icon icon-chevron-left"></span>
    </a>,
    <a
      is="nav-to"
      to={textReferences.fragmentInter(expert.xmlId, expert.urlId)}>
      {expert.number}
    </a>,
    <a
      is="nav-to"
      to={textReferences.fragmentInter(expert.nextXmlId, expert.nextUrlId)}>
      <span class="icon icon-chevron-right"></span>
    </a>,
    '',
    ,
  ];
};

export default ({ node }) => {
  return (
    <div id="text-navigation">
      <h5 data-key="witnesses" class="text-center">
        {node.getConstants('witnesses')}
      </h5>
      <div class="text-center" style={{ paddingTop: '8px' }}>
        {node.data.sortedSourceInterList?.map((source) => {
          return (
            <TextNavigationTable
              data={getSourceTableRow(node, source)}
              tbWidth="100%"
              colsWidth={['10%', '10%', '60%', '20%']}
            />
          );
        })}
      </div>
      <br />
      <div
        id="text-expertEditionNavContainer"
        style={{ display: 'flex', justifyContent: 'center' }}>
        <h5 data-key="expertEditions" class="text-center">
          {node.getConstants('expertEditions')}
        </h5>
        <span id="titleTooltip" class="icon-flex icon-info"></span>
        <ldod-tooltip
          data-ref="div#text-expertEditionNavContainer span#titleTooltip"
          data-tooltipkey="expertEditionNav"
          placement="bottom"
          light
          width="200px"
          content={node.getConstants('expertEditionNav')}></ldod-tooltip>
      </div>

      {node.editorialInters.map((editorialInter) => (
        <div class="text-center">
          {editorialInter[0] && (
            <TextNavigationTable
              author={{
                editor: editorialInter[0].editor,
                acronym: editorialInter[0].acronym,
              }}
              tbWidth="100%"
              colsWidth={['10%', '10%', '25%', '10%', '25%', '20%']}
              data={editorialInter.map((inter) =>
                getExpertTableRow(node, inter)
              )}
            />
          )}
        </div>
      ))}
      <br />
    </div>
  );
};
