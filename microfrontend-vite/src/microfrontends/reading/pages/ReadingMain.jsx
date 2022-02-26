import { Route, Routes, useNavigate } from 'react-router-dom';
import { lazy, useState, useEffect } from 'react';
import Reading from './Reading';
import { getLanguage } from '../../../store';
import { getCurrentReadingFragment, getPrevRecom } from '../api/reading';
import { getRecommendation, setRecommendation } from '../readingStore';

const Fragment = lazy(() => import('./Fragment'));

export const rightArrowUrl = new URL(
  '../resources/assets/arrow-right.svg',
  import.meta.url
).href;

export const leftArrowUrl = new URL(
  '../resources/assets/arrow-left.svg',
  import.meta.url
).href;

const getMessages = () =>
  import(`../resources/constants/messages-${getLanguage()}.js`);

export const xmlId = (data) => data.fragment.fragmentXmlId;
export const urlId = (data) => data.expertEditionInterDto.urlId;

export default () => {
  const navigate = useNavigate();
  const [messages, setMessages] = useState();

  useEffect(() => {
    getMessages().then(({ messages }) => setMessages(messages));
  }, [getLanguage()]);

  const fetchNumberFragment = (xmlid, urlid) => {
    getCurrentReadingFragment(xmlid, urlid, getRecommendation())
      .then(({ data }) => {
        setRecommendation(data.readingRecommendation);
        navigate(`/reading/fragment/${xmlId(data)}/inter/${urlId(data)}`, {
          state: data,
        });
      })
      .catch((err) => {
        console.error(err);
        navigate(-1);
      });
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
      <div className="row">
        <div className="main-content">
          <div className="row reading-grid">
            <Routes>
              <Route
                index
                element={
                  <Reading
                    messages={messages}
                    fetchNumberFragment={fetchNumberFragment}
                    fetchPrevRecom={fetchPrevRecom}
                  />
                }
              />
              <Route
                path="/:xmlid/:urlid"
                element={
                  <Reading
                    messages={messages}
                    fetchNumberFragment={fetchNumberFragment}
                    fetchPrevRecom={fetchPrevRecom}
                  />
                }
              />
              <Route
                path="/fragment/:xmlid/inter/:urlid"
                element={
                  <Fragment
                    messages={messages}
                    fetchNumberFragment={fetchNumberFragment}
                    fetchPrevRecom={fetchPrevRecom}
                  />
                }
              />
            </Routes>
          </div>
        </div>
      </div>
    </>
  );
};
