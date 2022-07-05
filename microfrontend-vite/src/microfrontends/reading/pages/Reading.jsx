import { useEffect } from 'react';
import { getReadingExperts } from '../api/reading';
import ReadingColumn from '../components/ReadingColumn';
import Recomendation from '../components/Recomendation';
import ReadingText from '../components/ReadingText';

export default ({ messages, experts }) => {
  
  useEffect( () => {
    !experts &&  getReadingExperts();
  }, []);

  return (
    <>
      {experts && (
        <>
          <div style={{ width: '58.3331%' }}>
            <ReadingText title={messages['book_disquiet']} />
          </div>
          {experts.map((expert, index) => (
            <div key={index} style={{ width: '8.333%' }}>
              <ReadingColumn expert={expert} />
            </div>
          ))}
          <Recomendation messages={messages} />
        </>
      )}
    </>
  );
};
