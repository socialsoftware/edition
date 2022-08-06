import tableStyle from './table.css?inline';

export const Table = ({ id, headers, data, constants, classes }) => {
  return (
    <>
      <style>{tableStyle}</style>

      <table id={id} class={classes}>
        <thead>
          <tr>
            {headers.map((key) => (
              <td data-key={key}>{constants(key)}</td>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((entry) => (
            <tr>
              {headers.map((key) => (
                <td>
                  {typeof entry[key] === 'function' ? entry[key]() : entry[key]}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </>
  );
};
