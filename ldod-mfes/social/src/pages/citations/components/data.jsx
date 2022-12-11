import { textFrag } from '@src/external-deps';

const getStringDate = (value) => (value < 10 ? `0${value}` : value);

export const getTableData = (citations) => {
  return citations?.map((row) => {
    return {
      tweetId: row.sourceLink.split('/')[5],
      date: `${getStringDate(row.formattedDate[2])}-${getStringDate(
        row.formattedDate[1]
      )}-${row.formattedDate[0]} ${getStringDate(
        row.formattedDate[3]
      )}:${getStringDate(row.formattedDate[4])}`,
      fragment: (
        <a is="nav-to" to={textFrag(row.xmlId)}>
          {row.title}
        </a>
      ),
      sourceLink: (
        <a href={`${row.sourceLink}`} target="_blank">
          Tweet
        </a>
      ),
      tweetText: row.tweetText,
      location: row.location,
      country: row.country,
      username: (
        <a href={`https://twitter.com/${row.username}`} target="_blank">
          {row.username}
        </a>
      ),
      search: JSON.stringify(row),
    };
  });
};
