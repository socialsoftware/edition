import { useNavigate, useParams } from 'react-router-dom';
import ReactTooltip from 'react-tooltip';
import { isEditorial } from '../dataExtraction';
import {
  addToAuthorialsInter,
  addToVirtualsInter,
  fragmentStateSelector,
  getAuthorialsInter,
  getVirtualsInter,
  resetCheckboxesState,
  setAuthorialsInter,
  setVirtualsInter,
} from '../fragmentStore';
import FragmentNavTable from './FragmentNavTable';

export default ({ messages, fragmentNavData }) => {
  const navigate = useNavigate();
  const currentUrlId = useParams()?.urlid;

  const {
    fragmentXmlId,
    externalId,
    sources,
    expertEditions,
    virtualEditions,
  } = fragmentNavData ?? {};

  const currentPath = `fragments/fragment/${fragmentXmlId ?? ''}`;
  const authorialsInter = fragmentStateSelector('authorialsInter');
  const virtualsInter = fragmentStateSelector('virtualsInter');

  const goToAuthorials = (e, id, urlId, state) => {
    e.preventDefault();
    isEditorial(state) && resetCheckboxesState();
    setAuthorialsInter(id);
    urlId !== currentUrlId &&
      navigate(`/${currentPath}/inter/${urlId}`, { state });
  };

  const goToVirtuals = (e, id, urlId, state) => {
    resetCheckboxesState();
    e.preventDefault();
    setVirtualsInter(id);
    urlId !== currentUrlId &&
      navigate(`/${currentPath}/inter/${urlId}`, { state });
  };

  const addToAuthorials = (id, urlId, state) => {
    resetCheckboxesState();
    addToAuthorialsInter(id);
    const to =
      getAuthorialsInter().length === 0
        ? `/${currentPath}`
        : `/${currentPath}/inter/${urlId}`;
    navigate(to, { state: { ...state, inters: getAuthorialsInter() } });
  };

  const addToVirtuals = (id, urlId, state) => {
    resetCheckboxesState();
    addToVirtualsInter(id);
    const to =
      getVirtualsInter().length === 0
        ? `/${currentPath}`
        : `/${currentPath}/inter/${urlId}`;
    navigate(to, { state: { ...state, inters: getVirtualsInter() } });
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
            style={{ width: '100%' }}>
            <h5 className="text-center">{messages['witnesses']}</h5>
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
                        checked={authorialsInter.includes(externalId)}
                        onChange={() =>
                          addToAuthorials(externalId, urlId, {
                            type: 'AUTHORIAL',
                          })
                        }
                      />,
                      <a
                        onClick={(e) =>
                          goToAuthorials(e, externalId, urlId, {
                            type: 'AUTHORIAL',
                          })
                        }>
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
              {messages['expert_editions']}
              <ReactTooltip
                id="expert-info-tooltip"
                type="light"
                place="bottom"
                effect="solid"
                border={true}
                borderColor="rgba(0,0,0,.2)"
                className="fragment-nav-tooltip"
                getContent={() => messages['expert_edition_info']}
              />
              <span
                data-tip=""
                data-for="expert-info-tooltip"
                className="glyphicon glyphicon-info-sign gray-color"></span>
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
                          onChange={() =>
                            addToAuthorials(externalId, urlId, {
                              type: 'EDITORIAL',
                            })
                          }
                        />,
                        <a
                          onClick={(e) =>
                            goToAuthorials(e, externalId, urlId, {
                              type: 'EDITORIAL',
                              prevNext: 'prev',
                            })
                          }>
                          <span className="glyphicon glyphicon-chevron-left"></span>
                        </a>,
                        <a
                          onClick={(e) =>
                            goToAuthorials(e, externalId, urlId, {
                              type: 'EDITORIAL',
                            })
                          }>
                          {number}
                        </a>,
                        <a
                          onClick={(e) =>
                            goToAuthorials(e, externalId, urlId, {
                              type: 'EDITORIAL',
                              prevNext: 'next',
                            })
                          }>
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
            {messages['virtual_editions']}
            <ReactTooltip
              id="virtual-info-tooltip"
              type="light"
              place="bottom"
              effect="solid"
              border={true}
              borderColor="rgba(0,0,0,.2)"
              className="fragment-nav-tooltip"
              getContent={() => messages['virtual_editions_info']}
            />
            <span
              data-tip=""
              data-for="virtual-info-tooltip"
              className="glyphicon glyphicon-info-sign gray-color"></span>
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
                      onChange={() =>
                        addToVirtuals(externalId, urlId, {
                          type: 'VIRTUAL',
                        })
                      }
                    />,
                    <a
                      onClick={(e) =>
                        goToVirtuals(e, externalId, urlId, {
                          type: 'VIRTUAL',
                          prevNext: 'prev',
                        })
                      }>
                      <span className="glyphicon glyphicon-chevron-left"></span>
                    </a>,
                    <a
                      onClick={(e) =>
                        goToVirtuals(e, externalId, urlId, {
                          type: 'VIRTUAL',
                        })
                      }>
                      {number}
                    </a>,
                    <a
                      onClick={(e) =>
                        goToVirtuals(e, externalId, urlId, {
                          type: 'VIRTUAL',
                          prevNext: 'next',
                        })
                      }>
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
