import { useLocation } from 'react-router-dom';
import { getExperts } from '../readingStore';
import ReadingColumn from '../components/ReadingColumn';
import ReadingText from '../components/ReadingText';
import Recomendation from '../components/Recomendation';

export default ({ messages, fetchNumberFragment, fetchPrevRecom }) => {
  const { state } = useLocation();
  const currentExpert = state?.expertEditionInterDto;

  return (
    <>
      {getExperts()?.map((expert, index) => {
        let isOpen = () => expert.acronym === currentExpert.acronym;

        return (
          <div key={index} className={`${isOpen() ? "block-column" : "thin-column"}`}>
            <ReadingColumn
              expert={expert}
              state={state}
              key={index}
              fetchNumberFragment={fetchNumberFragment}
            />
            {isOpen() && (
              <ReadingText
                title={state.fragment.title}
                text={state.transcript}
              />
            )}
          </div>
        );
      })}
      <Recomendation
        messages={messages}
        state={state}
        fetchPrevRecom={fetchPrevRecom}
        fetchNumberFragment={fetchNumberFragment}
      />
    </>
  );
};
