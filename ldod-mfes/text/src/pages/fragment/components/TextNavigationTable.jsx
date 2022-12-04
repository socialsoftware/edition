import textReferences from '@src/references';

export default ({ colsWidth, data, tbWidth, author }) => {
  return (
    <table
      width={tbWidth}
      style={{ borderSpacing: '0', borderCollapse: 'collapse' }}>
      {author && (
        <caption class="text-center">
          <a is="nav-to" to={textReferences.edition(author.acronym)}>
            {author.editor}
          </a>
        </caption>
      )}
      <thead>
        <tr>
          {colsWidth?.map((width, i) => (
            <th key={i} style={{ width }}></th>
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
