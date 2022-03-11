import { Link } from 'react-router-dom';

export const isExpertEdition = (edition) => edition?.type === "EXPERT";

export function ExpertEditionEntry(entry) {

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
      <Link to={xmlid && urlid && `/reading/${xmlid}/${urlid}`}>
        <span className="glyphicon glyphicon-eye-open"></span>
      </Link>
    ),
    volume: entry?.volume,
    page: entry?.startPage,
    date: entry?.date,
    heteronym: entry?.heteronym,
  };
  return {...result, searchData: JSON.stringify(result)}
}
