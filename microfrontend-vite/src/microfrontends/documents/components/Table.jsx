import parseHTML from 'html-react-parser';
import '../../../resources/css/bootstrap-table.min.css';

export default ({ data, headers, classes }) => {
  return (
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
                <td className={`tb-data-${header}`} key={ind}>
                  {parseHTML(entry?.[header] ?? '')}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
