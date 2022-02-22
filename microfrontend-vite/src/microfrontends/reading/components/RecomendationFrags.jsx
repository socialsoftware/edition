import { useNavigate } from 'react-router-dom';
import { getCurrentReadingFragment, getPrevRecom } from '../api/reading';
import { rightArrowUrl } from '../pages/ReadingMain';
import { leftArrowUrl } from '../pages/ReadingMain';
import { getRecommendation, setRecommendation } from '../readingStore';
import { urlId, xmlId } from './ReadingColumn';

export default ({
  state: {
    prevCom,
    recommendations,
    expertEditionInterDto: { acronym, number },
  },
}) => {
  const navigate = useNavigate();

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
      <div className="h3-group">
        <div>
          <a>
            <h3 style={{ color: '#FC1B27' }}>{acronym}</h3>
            <h2 style={{ color: '#FC1B27' }}>{number}</h2>
          </a>
        </div>
        {prevCom && (
          <div className="ldod-reading-prevrecom">
            <div className="h3-div">
              <a onClick={fetchPrevRecom}>
                <h3>{prevCom.acronym}</h3>
                <h2 className="recom-h2">
                  {prevCom.number}
                  <span
                    className="visible-xs-inline"
                    style={{ float: 'right' }}
                  >
                    <img src={leftArrowUrl} />
                  </span>
                  <div className="arrows-recom hidden-xs">
                    <img src={leftArrowUrl} />{' '}
                  </div>
                </h2>
              </a>
            </div>
          </div>
        )}
        {recommendations &&
          recommendations.map(
            ({ acronym, number, fragmentXmlId, urlId }, index) => (
              <div key={index}>
                <a onClick={() => fetchNumberFragment(fragmentXmlId, urlId)}>
                  <h3>{acronym}</h3>
                  <h2 className="recom-h2">
                    {number}
                    <span
                      className="visible-xs-inline"
                      style={{ float: 'right' }}
                    >
                      <img src={rightArrowUrl} />
                    </span>
                    <div className="arrows-recom hidden-xs">
                      <img src={rightArrowUrl} />
                    </div>
                  </h2>
                </a>
              </div>
            )
          )}
      </div>
    </>
  );
};
