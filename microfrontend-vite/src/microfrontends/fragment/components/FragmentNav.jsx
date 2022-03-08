import { useNavigate } from 'react-router-dom';
import ReactTooltip from 'react-tooltip';
import {
  fragmentStore,
  addToAuthorialsInter,
  addToVirtualsInter,
  setAuthorialsInter,
  setVirtualsInter,
  resetCheckboxesState,
  getAuthorialsInter,
  getVirtualsInter,
} from '../fragmentStore';
import FragmentNavTable from './FragmentNavTable';
const selector = (sel) => (state) => state[sel];

export default ({ messages, language, fragmentNavData, id }) => {
  const {
    fragmentXmlId,
    externalId,
    sources,
    expertEditions,
    virtualEditions,
  } = fragmentNavData ?? {};
  const currentPath = `fragments/fragment/${fragmentXmlId ?? ''}`;
  const navigate = useNavigate();
  const authorialsInter = fragmentStore(selector('authorialsInter'));
  const virtualsInter = fragmentStore(selector('virtualsInter'));

  const goToAuthorials = (e, id, urlId, prevNext, isExpert = true) => {
    isExpert && resetCheckboxesState();
    e.preventDefault();
    setAuthorialsInter(id);
    navigate(
      `/${currentPath}/inter/${urlId}`,
      prevNext && { state: { prevNext } }
    );
  };

  const goToVirtuals = (e, id, urlId) => {
    resetCheckboxesState();
    e.preventDefault();
    setVirtualsInter(id);
    navigate(`/${currentPath}/inter/${urlId}`);
  };

  const addToAuthorials = (id, urlId, isExpert = true) => {
    isExpert && resetCheckboxesState();
    addToAuthorialsInter(id);
    const to =
      getAuthorialsInter().length === 0
        ? `/${currentPath}`
        : `/${currentPath}/inter/${urlId}`;
    navigate(to, { state: { inters: getAuthorialsInter() } });
  };

  const addToVirtuals = (id, urlId) => {
    resetCheckboxesState();
    addToVirtualsInter(id);
    const to =
      getVirtualsInter().length === 0
        ? `/${currentPath}`
        : `/${currentPath}/inter/${urlId}`;
    navigate(to, { state: { inters: getVirtualsInter() } });
  };

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
              {sources?.map(({ shortName, urlId, externalId }, index) => (
                <FragmentNavTable
                  key={index}
                  tbWidth="100%"
                  colsWidth={['10%', '10%', '60%', '20%']}
                  data={[
                    [
                      '',
                      <input
                        type="checkbox"
                        checked={
                          authorialsInter.includes(externalId) ? true : false
                        }
                        onChange={() =>
                          addToAuthorials(externalId, urlId, false)
                        }
                      />,
                      <a
                        onClick={(e) =>
                          goToAuthorials(e, externalId, urlId, false)
                        }
                      >
                        {shortName}
                      </a>,
                      '',
                      ,
                    ],
                  ]}
                />
              ))}
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
            {expertEditions?.map(({ acronym, editor, inter }, index) => (
              <div key={index} className="text-center">
                {inter && (
                  <FragmentNavTable
                    author={{ editor, acronym }}
                    tbWidth="100%"
                    colsWidth={['10%', '10%', '25%', '10%', '25%', '20%']}
                    data={inter.map(
                      ({ acronym, externalId, urlId, number }) => [
                        '',
                        <input
                          id={`expert-${acronym}-inter-checkbox`}
                          type="checkbox"
                          checked={authorialsInter.includes(externalId)}
                          onChange={() => addToAuthorials(externalId, urlId)}
                        />,
                        <a
                          onClick={(e) =>
                            goToAuthorials(e, externalId, urlId, 'prev')
                          }
                        >
                          <span className="glyphicon glyphicon-chevron-left"></span>
                        </a>,
                        <a
                          onClick={(e) => goToAuthorials(e, externalId, urlId)}
                        >
                          {number}
                        </a>,
                        <a
                          onClick={(e) =>
                            goToAuthorials(e, externalId, urlId, 'next')
                          }
                        >
                          <span className="glyphicon glyphicon-chevron-right"></span>
                        </a>,
                        '',
                      ]
                    )}
                  />
                )}
              </div>
            ))}
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
          {virtualEditions?.map(({ acronym, inter }, index) => (
            <div key={index} className="text-center">
              {inter && (
                <FragmentNavTable
                  author={{ editor: acronym, acronym }}
                  tbWidth="100%"
                  colsWidth={['10%', '10%', '25%', '10%', '25%', '20%']}
                  data={inter.map(({ urlId, number, externalId }) => [
                    '',
                    <input
                      id={`virtual-${acronym}-inter-checkbox`}
                      type="checkbox"
                      checked={virtualsInter.includes(externalId)}
                      onChange={() => addToVirtuals(externalId, urlId)}
                    />,
                    <a
                      onClick={(e) =>
                        goToVirtuals(e, externalId, `${urlId}-prev`)
                      }
                    >
                      <span className="glyphicon glyphicon-chevron-left"></span>
                    </a>,
                    <a onClick={(e) => goToVirtuals(e, externalId, urlId)}>
                      {number}
                    </a>,
                    <a
                      onClick={(e) =>
                        goToVirtuals(e, externalId, `${urlId}-next`)
                      }
                    >
                      <span className="glyphicon glyphicon-chevron-right"></span>
                    </a>,
                    '',
                  ])}
                />
              )}
            </div>
          ))}
        </div>
      </div>
    </>
  );
};
