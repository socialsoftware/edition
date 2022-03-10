import HTMLReactParser from 'html-react-parser';

export default ({ messages, variations, headers }) => {
  return (
    <div>
      <h4>
        {messages.variations} ({variations.length})
      </h4>
      <table className="table table-condensed">
        <thead>
          <tr>
            {headers.map(({ shortName, title }, index) => (
              <th key={index}>
                {shortName}
                <br />
                {title}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {variations.map((row, index) => (
            <tr key={index}>
              {row?.map((variation, index) => (
                <td key={index}>{HTMLReactParser(variation)}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
