import { selectedInters } from '../../virtual';

export default ({ node }) => {
  return (
    <>
      <div
        id="virtual-virtualNavContainer"
        style={{ display: 'flex', justifyContent: 'center' }}>
        <h5 data-virtual-key="virtualEditions" class="text-center">
          {node.getConstants('virtualEditions')}
        </h5>
        <span id="virtualNavigationTooltip" class="icon-flex icon-info"></span>
        <ldod-tooltip
          data-ref="div#virtual-virtualNavContainer span#virtualNavigationTooltip"
          data-virtual-tooltip-key="virtualEditionsInfo"
          placement="bottom"
          light
          width="200px"
          content={node.getConstants('virtualEditionsInfo')}></ldod-tooltip>
      </div>
      {selectedInters.map((acrn) => {
        return (
          <div class="text-center">
            {
              <table
                width="100%"
                style={{
                  borderSpacing: '0',
                  borderCollapse: 'collapse',
                }}>
                <caption class="text-center">
                  <a is="nav-to" to={`/virtual/edition/acronym/${acrn}`}>
                    {acrn}
                  </a>
                </caption>
                <thead>
                  <tr>
                    {['10%', '10%', '25%', '10%', '25%', '20%'].map(
                      (width, i) => (
                        <th key={i} style={{ width }}></th>
                      )
                    )}
                  </tr>
                </thead>
                <tbody>
                  {node.inters[acrn]?.map((edition, i) => (
                    <tr key={i}>
                      <td></td>
                      <td>
                        <input
                          name={edition.urlId}
                          id={edition.externalId}
                          type="checkbox"
                          onChange={node.onCheckboxChange}
                          checked={node.selectedInters.includes(
                            edition.externalId
                          )}
                        />
                      </td>
                      <td>
                        <a
                          is="nav-to"
                          to={`/text/fragment/${edition.prevXmlId}/inter/${edition.prevUrlId}`}>
                          <span class="icon icon-chevron-left"></span>
                        </a>
                      </td>
                      <td>
                        <a
                          is="nav-to"
                          to={`/text/fragment/${edition.xmlId}/inter/${edition.urlId}`}>
                          {edition.number || '0'}
                        </a>
                      </td>
                      <td>
                        <a
                          is="nav-to"
                          to={`/text/fragment/${edition.nextXmlId}/inter/${edition.nextUrlId}`}>
                          <span class="icon icon-chevron-right"></span>
                        </a>
                      </td>
                      <td></td>
                    </tr>
                  ))}
                </tbody>
              </table>
            }
          </div>
        );
      })}
    </>
  );
};
