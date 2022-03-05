import { useNavigate } from 'react-router-dom';
import ReactTooltip from 'react-tooltip';
import { useEffect } from 'react';
import {
  addToExpertsInter,
  fragmentStore,
  setSourceInter,
  setExpertInter,
  addSourceToInters,
  setVirtualsInter,
  addToVirtualsInter,
} from '../fragmentStore';
import FragmentNavTable from './FragmentNavTable';
const selector = (sel) => (state) => state[sel];

export default ({
  messages,
  language,
  fragmentNavData: {
    fragmentXmlId,
    externalId,
    sourceUrlId,
    sourceShortName,
    sourceExternalId,
    expertEditions,
    virtualEditions,
  },
}) => {
  const currentPath = `fragments/fragment/${fragmentXmlId}`;
  const navigate = useNavigate();
  const sourceInter = fragmentStore(selector('sourceInter'));
  const expertsInter = fragmentStore(selector('expertsInter'));
  const virtualsInter = fragmentStore(selector('virtualsInter'));

  const goToSourceInter = (e) => {
    e.preventDefault();
    setSourceInter(sourceExternalId);
    navigate(`/${currentPath}/inter/${sourceUrlId}`);
  };

  const addSourceInter = () => {
    const to = sourceInter
      ? `/fragments/fragment/${fragmentXmlId}`
      : `/${currentPath}/inter/${sourceUrlId}`;
    addSourceToInters(sourceExternalId);
    navigate(to);
  };

  const goToExpert = (e, to, externalID) => {
    e.preventDefault();
    setExpertInter(externalID);
    navigate(to);
  };

  const addToExperts = (e, urlId, externalID) => {
    const to = expertsInter.includes(externalID)
      ? `/${currentPath}`
      : `/${currentPath}/expert/${urlId}`;
    addToExpertsInter(externalID);
    navigate(to);
  };

  const goToVirtuals = (e, to, externalId) => {
    e.preventDefault();
    setVirtualsInter(externalId);
    navigate(to);
  };

  const addToVirtuals = (e, urlId, externalId) => {
    const to = virtualsInter.includes(externalId)
      ? `/${currentPath}`
      : `/${currentPath}/virtual/${urlId}`;
    addToVirtualsInter(externalId);
    navigate(to);
  };

  useEffect(() => {
    return () => {
      setSourceInter();
      setExpertInter([]);
      setVirtualsInter([]);
    };
  }, []);

  return (
    <>
      <div className="col-md-3">
        <div id="fragment" className="row">
          <div id={externalId}></div>
          <div
            className="btn-group"
            id="baseinter"
            data-toggle="checkbox"
            style={{ width: '100%' }}
          >
            <h5 className="text-center">{messages?.[language]['witnesses']}</h5>
            <div className="text-center" style={{ paddingTop: '8px' }}>
              <FragmentNavTable
                tbWidth="100%"
                colsWidth={['10%', '10%', '60%', '20%']}
                data={[
                  '',
                  <input
                    type="checkbox"
                    checked={sourceInter ? true : false}
                    onInput={addSourceInter}
                  />,
                  <a onClick={goToSourceInter}> {sourceShortName}</a>,
                  '',
                ]}
              />
            </div>
            <br />
            <h5 className="text-center">
              {messages?.[language]['expert_editions']}
              <ReactTooltip
                id="expert-info-tooltip"
                type="light"
                place="bottom"
                effect="solid"
                border={true}
                borderColor="rgba(0,0,0,.2)"
                className="fragment-nav-tooltip"
                getContent={() => messages?.[language]['expert_edition_info']}
              />
              <span
                data-tip=""
                data-for="expert-info-tooltip"
                className="glyphicon glyphicon-info-sign gray-color"
              ></span>
            </h5>
            {expertEditions?.map(
              ({ acronym, editor, urlId, number, externalID }, index) => (
                <div key={index} className="text-center">
                  <FragmentNavTable
                    author={{ editor, acronym }}
                    tbWidth="100%"
                    colsWidth={['10%', '10%', '25%', '10%', '25%', '20%']}
                    data={[
                      '',
                      <input
                        id={`expert-${acronym}-inter-checkbox`}
                        type="checkbox"
                        checked={expertsInter.includes(externalID)}
                        onInput={(e) => addToExperts(e, urlId, externalID)}
                      />,
                      <a
                        onClick={(e) =>
                          goToExpert(
                            e,
                            `/${currentPath}/expert/${urlId}/prev`,
                            externalID
                          )
                        }
                      >
                        <span className="glyphicon glyphicon-chevron-left"></span>
                      </a>,
                      <a
                        onClick={(e) =>
                          goToExpert(
                            e,
                            `/${currentPath}/expert/${urlId}`,
                            externalID
                          )
                        }
                      >
                        {number}
                      </a>,
                      <a
                        onClick={(e) =>
                          goToExpert(
                            e,
                            `/${currentPath}/expert/${urlId}/next`,
                            externalID
                          )
                        }
                      >
                        <span className="glyphicon glyphicon-chevron-right"></span>
                      </a>,
                      '',
                    ]}
                  />
                </div>
              )
            )}
          </div>
          <br /> <br />
          <h5 className="text-center">
            {messages?.[language]['virtual_editions']}
            <ReactTooltip
              id="virtual-info-tooltip"
              type="light"
              place="bottom"
              effect="solid"
              border={true}
              borderColor="rgba(0,0,0,.2)"
              className="fragment-nav-tooltip"
              getContent={() => messages?.[language]['virtual_editions_info']}
            />
            <span
              data-tip=""
              data-for="virtual-info-tooltip"
              className="glyphicon glyphicon-info-sign gray-color"
            ></span>
          </h5>
          {virtualEditions?.map(
            ({ acronym, urlId, number, externalId }, index) => (
              <div key={index} className="text-center">
                <FragmentNavTable
                  author={{ editor: acronym, acronym }}
                  tbWidth="100%"
                  colsWidth={['10%', '10%', '25%', '10%', '25%', '20%']}
                  data={
                    urlId && [
                      '',
                      <input
                        id={`virtual-${acronym}-inter-checkbox`}
                        type="checkbox"
                        checked={virtualsInter.includes(externalId)}
                        onInput={(e) => addToVirtuals(e, urlId, externalId)}
                      />,
                      <a
                        onClick={(e) =>
                          goToVirtuals(
                            e,
                            `/${currentPath}/virtual/${urlId}/prev`,
                            externalId
                          )
                        }
                      >
                        <span className="glyphicon glyphicon-chevron-left"></span>
                      </a>,
                      <a
                        onClick={(e) =>
                          goToVirtuals(
                            e,
                            `/${currentPath}/virtual/${urlId}`,
                            externalId
                          )
                        }
                      >
                        {number}
                      </a>,
                      <a
                        onClick={(e) =>
                          goToVirtuals(
                            e,
                            `/${currentPath}/virtual/${urlId}/next`,
                            externalId
                          )
                        }
                      >
                        <span className="glyphicon glyphicon-chevron-right"></span>
                      </a>,
                      '',
                    ]
                  }
                />
              </div>
            )
          )}
        </div>
      </div>
    </>
  );
};
