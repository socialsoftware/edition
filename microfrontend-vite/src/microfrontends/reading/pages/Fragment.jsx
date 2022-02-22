import { useLocation } from 'react-router-dom';
import { getExperts } from '../readingStore';
import ReadingColumn from '../components/ReadingColumn';
import ReadingText from '../components/ReadingText';
import Recomendation from '../components/Recomendation';

export default ({ messages,fetchNumberFragment, fetchPrevRecom }) => {
  const { state } = useLocation();
  const currentExpert = state.expertEditionInterDto;

  return (
    <>
      {getExperts() &&
        getExperts().map((expert, index) => (
          <div key={index}>
            <ReadingColumn
              expert={expert}
              state={state}
              key={index}
              fetchNumberFragment={fetchNumberFragment}
            />
            {expert.acronym === currentExpert.acronym && (
              <ReadingText
                title={state.fragment.title}
                text={state.transcript}
              />
            )}
          </div>
        ))}
      <Recomendation
        messages={messages}
        state={state}
        fetchPrevRecom={fetchPrevRecom}
        fetchNumberFragment={fetchNumberFragment}
      />
    </>
  );
};
