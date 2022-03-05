import { useLocation } from 'react-router-dom';
import ReadingColumn from '../components/ReadingColumn';
import ReadingText from '../components/ReadingText';
import Recomendation from '../components/Recomendation';


export default ({ messages, fetchNumberFragment, fetchPrevRecom, language, experts }) => {
  const { state } = useLocation();
  const currentExpert = state?.expertEditionInterDto;

  return (
    <>
      {experts?.map((expert, index) => {
        let isOpen = () => expert?.acronym === currentExpert?.acronym;

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
        language={language}
        state={state}
        fetchPrevRecom={fetchPrevRecom}
        fetchNumberFragment={fetchNumberFragment}
      />
    </>
  );
};
