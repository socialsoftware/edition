import { useEffect } from 'react';
import { Link as link, } from 'react-router-dom';
import parseHTML from 'html-react-parser';
import '../resources/css/bootstrap-table.min.css';

export default ({ data, headers }) => (
  <div className="fixed-table-body">
    <table
      id="tablecitations"
      data-pagination="false"
      data-search="true"
      className="table table-hover"
    >
      <thead>
        <tr>
          {Object.keys(headers ?? {}).map((header, index) => (
            <th key={index}>
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
              <td key={ind}>{parseHTML(entry[header] ?? '')}</td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  </div>
);
