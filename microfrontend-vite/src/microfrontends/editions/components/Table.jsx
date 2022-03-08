import React from 'react';
import ReactTooltip from 'react-tooltip';
import '../../../resources/css/bootstrap-table.min.css';

export default React.memo(({ headers, data, classes, messages }) => {

  const dropEmptyCols = (headers, data) =>
    Object.keys(headers).filter((header) => data?.some((row) => row[header]));
    
  return (
    <div className="fixed-table-body">
      <table
        id="tableEditions"
        data-pagination="false"
        data-search="true"
        className={classes}
      >
        <thead>
          <tr>
            {dropEmptyCols(headers, data).map((header, index) => (
              <th
                key={index}
                className={
                  header !== 'title' && header !== 'heteronym'
                    ? `text-center`
                    : ''
                }
              >
                <ReactTooltip
                  id={`${header}-info`}
                  type="dark"
                  place="bottom"
                  effect="solid"
                  className="edition-table-tooltip"
                  getContent={() => messages?.[`${header}_info`]}
                />
                <span data-tip="" data-for={`${header}-info`}>
                  <div className="th-inner">{headers?.[header]}</div>
                </span>
                <div className="fht-cell"></div>
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data?.map((entry, index) => (
            <tr key={index} data-index={index}>
              {dropEmptyCols(headers, data).map((header, ind) => (
                <td
                  className={
                    header !== 'title' && header !== 'heteronym'
                      ? `text-center`
                      : ''
                  }
                  key={ind}
                >
                  {entry[header]}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
});
