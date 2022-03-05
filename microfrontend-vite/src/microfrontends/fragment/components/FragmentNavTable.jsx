import { Link } from 'react-router-dom';

export default ({ colsWidth, data, tbWidth, author }) => {
  return (
    <table width={tbWidth}>
      {author && (
        <caption className="text-center">
          <Link to={`/edition/acronym/${author?.acronym}`}>
            {author?.editor}
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
