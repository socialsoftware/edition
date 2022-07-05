import { Link } from 'react-router-dom';

export const isExpertEdition = (edition) => edition?.type === 'EXPERT';

const dropEmptyCols = (data) => {
  let filteredHeaders = Object.keys(data?.[0] ?? {}).filter((header) =>
    data.some((entry) => entry[header])
  );
  return data?.map((row) =>
    Object.keys(row)
      .filter((header) => filteredHeaders.includes(header))
      .reduce(
        (prev, curr) => ({
          ...prev,
          [curr]: row[curr],
        }),
        {}
      )
  );
};

function ExpertEditionEntry(entry) {
  const xmlid = entry?.xmlId;
  const urlid = entry?.urlId;

  const result = {
    number: entry?.number === 0 ? '' : entry?.number,
    title: (
      <Link
        to={xmlid && urlid && `/fragments/fragment/${xmlid}/inter/${urlid}`}
      >
        {entry?.title}
      </Link>
    ),
    read: (
      <Link to={xmlid && urlid && `/reading/fragment/${xmlid}/inter/${urlid}`}>
        <span className="glyphicon glyphicon-eye-open"></span>
      </Link>
    ),
    volume: entry?.volume,
    page: entry?.startPage,
    date: entry?.date,
    heteronym: entry?.heteronym,
  };
  return { ...result, searchData: JSON.stringify(result) };
}

export function ExpertEditionModel(data) {
  return {
    ...data,
    type: 'EXPERT',
    tableData: dropEmptyCols(
      data?.sortedInterpsList?.map((entry) => ExpertEditionEntry(entry))
    ),
  };
}
