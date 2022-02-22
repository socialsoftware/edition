import { useLocation, useParams } from 'react-router-dom';
import { getExperts, readingStore } from '../readingStore';
import ReadingColumn from '../components/ReadingColumn';
import ReadingText from '../components/ReadingText';
import Recomendation from '../components/Recomendation';

export default ({ messages }) => {
  const { state } = useLocation();
  const currentExpert = state.expertEditionInterDto;

  return (
    <>
      {getExperts() &&
        getExperts().map((expert, index) => (
          <div key={index}>
            <ReadingColumn expert={expert} state={state} key={index} />
            {expert.acronym === currentExpert.acronym && (
              <ReadingText
                title={state.fragment.title}
                text={state.transcript}
              />
            )}
          </div>
        ))}
      <Recomendation messages={messages} state={state} />
    </>
  );
};
