import { useNavigate, useParams } from 'react-router-dom';
import {
  getCurrentReadingFragment,
  getPrevNextReadingFragment,
  getStartReadingFragment,
} from '../api/reading';
import { rightArrowUrl, leftArrowUrl } from '../pages/ReadingMain';
import { getRecommendation, setRecommendation } from '../readingStore';

export const xmlId = (data) => data.fragment.fragmentXmlId;
export const urlId = (data) => data.expertEditionInterDto.urlId;

export default ({ expert, state }) => {
  const { xmlid, urlid } = useParams();

  const getExpertFrag = () =>
    state.expertEditionDtoList.find(
      ([exp]) => exp && exp.acronym && exp.acronym === expert.acronym
    );

  const isOpen = () =>
    state && state.expertEditionInterDto.acronym === expert.acronym;

  const navigate = useNavigate();

  const fetchFragment = () => {
    getStartReadingFragment(expert.acronym, getRecommendation()).then(
      ({ data }) => {
        setRecommendation(data.readingRecommendation);
        navigate(`/reading/fragment/${xmlId(data)}/inter/${urlId(data)}`, {
          state: data,
        });
      }
    );
  };

  const fetchPrevNextFragment = (type) => {
    getPrevNextReadingFragment(xmlid, urlid, getRecommendation(), type).then(
      ({ data }) => {
        setRecommendation(data.readingRecommendation);
        navigate(`/reading/fragment/${xmlId(data)}/inter/${urlId(data)}`, {
          state: data,
        });
      }
    );
  };

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

  return (
    <div
      className={`reading__column${
        isOpen() ? '--open' : ''
      } col-xs-12 col-sm-1 no-pad`}
    >
      <h4>
        <a onClick={fetchFragment}>{expert.editor}</a>
      </h4>
      {!state ? (
        <a onClick={fetchFragment}>
          <img src={rightArrowUrl} />
        </a>
      ) : (
        getExpertFrag() &&
        getExpertFrag().map(({ number, urlId, fragmentXmlId }, index) => {
          return (
            <div key={index}>
              <div className="hidden-xs" style={{ marginBottom: '25px' }}>
                <a onClick={() => fetchNumberFragment(fragmentXmlId, urlId)}>
                  <h2>{number}</h2>
                </a>
                <div className="arrows">
                  <div>
                    <a onClick={() => fetchPrevNextFragment('prev')}>
                      <img src={leftArrowUrl} />
                    </a>
                  </div>
                  <div>
                    <a onClick={() => fetchPrevNextFragment('next')}>
                      <img src={rightArrowUrl} />
                    </a>
                  </div>
                </div>
              </div>
              <div className="visible-xs-block">
                <div>
                  <h2>{number}</h2>
                  <a onClick={() => fetchPrevNextFragment('next')}>
                    <img src={rightArrowUrl} />
                  </a>
                  <a onClick={() => fetchPrevNextFragment('prev')}>
                    <img src={leftArrowUrl} />
                  </a>
                </div>
              </div>
            </div>
          );
        })
      )}
    </div>
  );
};
