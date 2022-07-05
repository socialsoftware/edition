import { Link } from 'react-router-dom';

export default ({ colsWidth, data, tbWidth, author }) => {
  return (
    <table width={tbWidth}>
      {author && (
        <caption className="text-center">
          <Link to={`/edition/acronym/${author.acronym}`}>
            {author.editor}
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
        {data?.map((edition, i) => (
          <tr key={i}>
            {edition.map((row, index) => (
              <td key={index}>{row}</td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
};
