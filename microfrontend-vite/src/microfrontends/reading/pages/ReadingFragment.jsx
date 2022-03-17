import { useParams } from 'react-router-dom';
import { useEffect } from 'react';
import ReadingColumn from '../components/ReadingColumn';
import ReadingText from '../components/ReadingText';
import Recomendation from '../components/Recomendation';
import { getCurrentReadingFragment, getReadingExperts } from '../api/reading';
import { getRecommendation, setFragment } from '../readingStore';

export default ({ messages, experts, fragment }) => {
  const { xmlid, urlid } = useParams();

  useEffect(() => {
    !experts && getReadingExperts();
    !fragment && getCurrentReadingFragment(xmlid, urlid, getRecommendation());
    return () =>  setFragment()
  }, []);

  return (
    <>
      {fragment  &&  (
        <>
          {experts?.map((expert, index) => {
              let isOpen = () =>
                expert?.acronym === fragment.expertEditionInterDto?.acronym;

              return (
                <div
                  key={index}
                  className={`${isOpen() ? 'block-column' : 'thin-column'}`}
                >
                  <ReadingColumn expert={expert} fragment={fragment} />
                  {isOpen() && (
                    <ReadingText
                      title={fragment.fragment.title}
                      text={fragment.transcript}
                    />
                  )}
                </div>
              );
            })}
          <Recomendation messages={messages} fragment={fragment} />
        </>
      )}
    </>
  );
};
