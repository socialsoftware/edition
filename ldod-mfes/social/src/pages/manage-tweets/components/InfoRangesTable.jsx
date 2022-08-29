export default ({ headers, data }) => {
  return (
    <table class="table table-stripped table-condensed table-bordered">
      <thead>
        <tr>
          {headers.map((header) => (
            <td style={{ textTransform: 'capitalize' }}>{header}</td>
          ))}
        </tr>
      </thead>
      <tbody>
        {data.reverse().map((row) => (
          <tr>
            {headers.map((header) => {
              return <td>{row[header]}</td>;
            })}
          </tr>
        ))}
      </tbody>
    </table>
  );
};
