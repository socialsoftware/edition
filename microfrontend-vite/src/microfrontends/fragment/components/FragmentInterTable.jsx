import { Link } from 'react-router-dom';

export default ({ colsWidth, data, tbWidth, expert }) => {
  return (
    <table width={tbWidth}>
      {expert && (
        <caption className="text-center">
          <Link to={`/edition/acronym/${expert?.acronym}`}>
            {expert?.editor}
          </Link>
        </caption>
      )}
      <thead>
        <tr>
          {colsWidth?.map((pct, i) => (
            <th key={i} style={{ width: pct }}></th>
          ))}
        </tr>
      </thead>
      <tbody>
        <tr>
          {data?.map((row, i) => (
            <td key={i}>{row}</td>
          ))}
        </tr>
      </tbody>
    </table>
  );
};
