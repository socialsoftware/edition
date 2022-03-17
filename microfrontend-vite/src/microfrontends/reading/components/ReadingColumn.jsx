import { useNavigate, useParams } from 'react-router-dom';
import {
  getCurrentReadingFragment,
  getPrevNextReadingFragment,
  getStartReadingFragment,
} from '../api/reading';
import { rightArrowUrl, leftArrowUrl, xmlId, urlId } from '../pages/ReadingMain';
import { getFragment, getRecommendation } from '../readingStore';

const getExpertFrag = (fragment, expert) =>
fragment?.expertEditionDtoList.find(
    ([exp]) => exp?.acronym === expert?.acronym
  );

const isOpen = (fragment, expert) =>
fragment?.expertEditionInterDto.acronym === expert.acronym;

export default ({ expert, fragment }) => {
  const { xmlid, urlid } = useParams();
  const navigate = useNavigate();

  const fetchStartFragment = async () => {
    await getStartReadingFragment(expert.acronym, getRecommendation());
    navigate(
      `/reading/fragment/${xmlId(getFragment())}/inter/${urlId(getFragment())}`
    );
  };

  const fetchPrevNextFragment = async (type) => {
    await getPrevNextReadingFragment(xmlid, urlid, getRecommendation(), type);
    navigate(
      `/reading/fragment/${xmlId(getFragment())}/inter/${urlId(getFragment())}`
    );
  };

  return (
    <div
      className={`reading__column${
        isOpen(fragment, expert) ? '--open' : ''
      } col-xs-12 no-pad`}
    >
      <h4>
        <a onClick={fetchStartFragment}>{expert.editor}</a>
      </h4>
      {!fragment ? (
        <a onClick={fetchStartFragment}>
          <img src={rightArrowUrl} />
        </a>
      ) : (
        getExpertFrag(fragment, expert)?.map(
          ({ number, urlId, fragmentXmlId }, index) => {
            return (
              <div key={index}>
                <div className="hidden-xs" style={{ marginBottom: '25px' }}>
                  <a
                    onClick={() =>
                      getCurrentReadingFragment(
                        fragmentXmlId,
                        urlId,
                        getRecommendation()
                      )
                    }
                  >
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
          }
        )
      )}
    </div>
  );
};
