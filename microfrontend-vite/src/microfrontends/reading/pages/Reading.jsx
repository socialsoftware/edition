import { useEffect } from 'react';
import { getReadingExperts } from '../api/reading';
import { setExperts, getExperts } from '../readingStore';
import ReadingColumn from '../components/ReadingColumn';
import Recomendation from '../components/Recomendation';
import ReadingText from '../components/ReadingText';
import {  useParams } from 'react-router-dom';

export default ({ messages, fetchNumberFragment, fetchPrevRecom }) => {
  const params = useParams();

  useEffect(() => {
    getReadingExperts().then(({ data }) => setExperts(data));
    params?.xmlid && params?.urlid && fetchNumberFragment(params.xmlid, params.urlid);
  }, []);

  return (
    <>
      <div style={{ width: '58.3331%' }}>
        <ReadingText title={(() => messages?.['book_disquiet'])()} />
      </div>
      {getExperts()?.map((expert, index) => (
        <div key={index} style={{ width: '8.333%' }}>
          <ReadingColumn
            expert={expert}
            fetchNumberFragment={fetchNumberFragment}
          />
        </div>
      ))}
      <Recomendation
        messages={messages}
        fetchNumberFragment={fetchNumberFragment}
        fetchPrevRecom={fetchPrevRecom}
      />
    </>
  );
};
