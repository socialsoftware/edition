import { useLocation } from 'react-router-dom';
import { getExperts, getRecommendation, setRecommendation } from '../readingStore';
import ReadingColumn from '../components/ReadingColumn';
import ReadingText from '../components/ReadingText';
import Recomendation from '../components/Recomendation';
import { urlId, xmlId } from './ReadingMain';
import {getCurrentReadingFragment, getPrevRecom } from '../api/reading'

export default ({ messages }) => {
  const { state } = useLocation();
  const currentExpert = state.expertEditionInterDto;

  const fetchNumberFragment = (xmlid, urlid) => {
    getCurrentReadingFragment(xmlid, urlid, getRecommendation()).then(
      ({ data }) => {
        setRecommendation(data.readingRecommendation);
        navigate(`/reading/fragment/${xmlId(data)}/inter/${urlId(data)}`, {
          state: data,
        });
      }
    );
  };

  const fetchPrevRecom = () => {
    getPrevRecom(getRecommendation()).then(({ data }) => {
      setRecommendation(data.readingRecommendation);
      navigate(`/reading/fragment/${xmlId(data)}/inter/${urlId(data)}`, {
        state: data,
      });
    });
  };

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
