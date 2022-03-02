import React from 'react';
import '../../../resources/css/bootstrap-table.min.css';

export default React.memo(({ data, headers, classes }) => (

  <div className="fixed-table-body">
    <table
      id="tablecitations"
      data-pagination="false"
      data-search="true"
      className={classes}
    >
      <thead>
        <tr>
          {Object.keys(headers ?? {}).map((header, index) => (
            <th key={index} className={`tb-header-${header}`}>
              <div className="th-inner">{headers[header]}</div>
              <div className="fht-cell"></div>
            </th>
          ))}
        </tr>
      </thead>
      <tbody>
        {data?.map((entry, index) => (
          <tr key={index} data-index={index}>
            {Object.keys(headers ?? {}).map((header, ind) => (
              <td className={`tb-data-${header}`}  key={ind}>{entry[header]}</td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  </div>
));
