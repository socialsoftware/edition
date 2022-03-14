import { Link } from 'react-router-dom';

const imageUrl = (filename) =>
  new URL(`../resources/assets/${filename}.webp`, import.meta.url).href;
const editions = [
  {
    filename: 'JPC',
    acronym: 'JPC',
    padding: '5px',
  },
  {
    filename: 'TSC',
    acronym: 'TSC',
    padding: '5px',
  },
  {
    filename: 'RZ',
    acronym: 'RZ',
    padding: '5px',
  },
  {
    filename: 'JP',
    acronym: 'JP',
    padding: '10px',
  },
  {
    filename: 'ALdod',
    acronym: 'LdoD-Arquivo',
    padding: '5px',
  },
];

export default ({ messages }) => {
  console.log(editions);

  return (
    <div className="col-md-6 col-md-offset-3 intro-editions">
      <h2 className="text-center">{messages.editions}</h2>
      <p>&nbsp;</p>
      {editions.map(({ acronym, padding, filename }, index) => (
        <Link key={index} to={`/edition/acronym/${acronym}`} className="ldod-default">
          <div className="div-link">
            <img
              src={imageUrl(filename)}
              width="100%"
              style={{ paddingBottom: padding }}
            />
            <img
              src={imageUrl(`${filename}H`)}
              width="100%"
              style={{ paddingBottom: padding }}
            />
          </div>
        </Link>
      ))}
    </div>
  );
};
