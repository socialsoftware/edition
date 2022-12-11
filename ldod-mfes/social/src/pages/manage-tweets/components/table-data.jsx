import { textFrag } from '../../../external-deps';
import InfoRangesTable from './info-ranges-table';

export const getTableData = (citations) => {
  const infoRangesHeaders = [
    'xmlId',
    'text',
    'quote',
    'start',
    'end',
    'startOffset',
    'endOffset',
  ];
  return citations?.map((row) => {
    return {
      tweetId: row.sourceLink.split('/')[5],
      date: row.data.replace('.', ''),
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
      infoRanges: (
        <InfoRangesTable headers={infoRangesHeaders} data={row.rangeList} />
      ),
      annotations: row.awareSetSize,
      retweets: `${row.numberOfRetweets}`,
      location: row.location,
      country: row.country,
      username: row.username,
      profile: row.userProfileURL,
      image: row.userImageURL,
      search: JSON.stringify(row),
    };
  });
};
