import { useState, useEffect } from 'react';
import { getReadingExperts } from '../api/reading';
import { setExperts, getExperts } from '../readingStore';
import ReadingColumn from '../components/ReadingColumn';
import Recomendation from '../components/Recomendation';
import ReadingText from '../components/ReadingText';



export default ({messages}) => {

  useEffect(() => {
    getReadingExperts().then(({ data }) => setExperts(data));
  }, []);

  return (
    <>
      <ReadingText title={(() => messages && messages['book_disquiet'])()} />
      {getExperts() &&
        getExperts().map((expert, index) => (
          <ReadingColumn key={index} expert={expert} />
        ))}
      <Recomendation messages={messages} />
    </>
  );
};
