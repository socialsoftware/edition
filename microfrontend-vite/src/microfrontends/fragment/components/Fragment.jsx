import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import ReactTooltip from 'react-tooltip';
import FragmentInterTable from './FragmentInterTable';

export default ({ messages, language, frag, virtualFrag }) => {
  const { fragment, ldoD } = frag;
  const sourceInter = fragment?.sourceInterDtoList?.[0];
  const expertEditions = fragment?.expertEditionInterDtoMap;
  const sortedExpert = ldoD?.sortedExpert;

  useEffect(() => {
    console.log(expertEditions);
  }, [fragment]);

  return (
    <>
      <div className="col-md-9">
        <div id="fragmentInter" className="row">
          <h4 className="text-center">{fragment?.title}</h4>
        </div>
      </div>
      <div className="col-md-3">
        <div id="fragment" className="row">
          <div id={fragment?.externalId}></div>
          <div
            className="btn-group"
            id="baseinter"
            data-toggle="checkbox"
            style={{ width: '100%' }}
          >
            <h5 className="text-center">{messages?.[language]['witnesses']}</h5>
            <div className="text-center" style={{ paddingTop: '8px' }}>
              <FragmentInterTable
                tbWidth="100%"
                colsWidth={['10%', '10%', '60%', '20%']}
                data={[
                  '',
                  <input id="fragment-inter-checkbox" type="checkbox" />,
                  <Link to={`inter/${sourceInter?.urlId}`}>
                    {sourceInter?.shortName}
                  </Link>,
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
                className="info-tooltip"
                border={true}
                getContent={() => messages?.[language]['expert_edition_info']}
              />
              <span
                data-tip=""
                data-for="expert-info-tooltip"
                className="glyphicon glyphicon-info-sign gray-color"
              ></span>
            </h5>
            {sortedExpert?.map(({ acronym, editor }, index) => {
              const urlid = expertEditions[acronym]?.urlId;
              return (
                <div key={index} className="text-center">
                  <FragmentInterTable
                    expert={{editor, acronym}}
                    tbWidth="100%"
                    colsWidth={['10%', '10%', '25%', '10%', '25%', '20%']}
                    data={[
                      '',
                      <input
                        id={`expert-${acronym}-inter-checkbox`}
                        type="checkbox"
                      />,
                      <Link to={`inter/${urlid}/prev`}>
                        <span className="glyphicon glyphicon-chevron-left"></span>
                      </Link>,
                      <Link to={`inter/${urlid}`}>
                        {expertEditions[acronym]?.number}
                      </Link>,
                      <Link to={`inter/${urlid}/next`}>
                        <span className="glyphicon glyphicon-chevron-right"></span>
                      </Link>,
                      '',
                    ]}
                  />
                </div>
              );
            })}
          </div>
          <br /> <br />
          {/*} <div id="virtualinter" data-toggle="checkbox">
            <h5 className="text-center">
              Edições Virtuais
              <a
                id="infovirtualeditions"
                data-placement="bottom"
                className="infobutton"
                role="button"
                data-toggle="popover"
                data-content="As edições virtuais contêm fragmentos escolhidos pelos seus editores a partir de outras edições."
                data-original-title=""
                title=""
              >
                {' '}
                <span className="glyphicon glyphicon-info-sign"></span>
              </a>
            </h5>

            <div className="text-center">
              <div className="text-center" style="padding: 8px">
                <a href="/edition/acronym/LdoD-Arquivo">Arquivo LdoD</a>
              </div>
              <table width="100%">
                <thead>
                  <tr>
                    <th style={{width: "10%"}}></th>
                    <th style={{width: "10%"}}></th>
                    <th style={{width: "25%"}}></th>
                    <th style={{width: "10%"}}></th>
                    <th style={{width: "25%"}}></th>
                    <th style={{width: "20%"}}></th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td></td>
                    <td>
                      <input
                        type="checkbox"
                        name="281861523768029"
                        value="281861523768029"
                      />
                    </td>
                    <td>
                      <a href="/fragments/fragment/Fr001/inter/Fr001_WIT_ED_VIRT_LdoD-Arquivo_1/prev">
                        <span className="glyphicon glyphicon-chevron-left"></span>
                      </a>
                    </td>
                    <td>
                      <a href="/fragments/fragment/Fr001/inter/Fr001_WIT_ED_VIRT_LdoD-Arquivo_1">
                        1
                      </a>
                    </td>
                    <td>
                      <a href="/fragments/fragment/Fr001/inter/Fr001_WIT_ED_VIRT_LdoD-Arquivo_1/next">
                        <span className="glyphicon glyphicon-chevron-right"></span>
                      </a>
                    </td>
                    <td></td>
                  </tr>
                </tbody>
              </table>
            </div>

            <div className="text-center">
              <div className="text-center" style="padding: 8px">
                <a href="/edition/acronym/LdoD-JPC-anot">LdoD-JPC-anot</a>
              </div>
              <table width="100%">
                <thead>
                  <tr>
                    <th style={{width: "10%"}}></th>
                    <th style={{width: "10%"}}></th>
                    <th style={{width: "25%"}}></th>
                    <th style={{width: "10%"}}></th>
                    <th style={{width: "25%"}}></th>
                    <th style={{width: "20%"}}></th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td></td>
                    <td>
                      <input
                        type="checkbox"
                        name="281861523768018"
                        value="281861523768018"
                      />
                    </td>
                    <td>
                      <a href="/fragments/fragment/Fr001/inter/Fr001_WIT_ED_VIRT_LdoD-JPC-anot_1/prev">
                        <span className="glyphicon glyphicon-chevron-left"></span>
                      </a>
                    </td>
                    <td>
                      <a href="/fragments/fragment/Fr001/inter/Fr001_WIT_ED_VIRT_LdoD-JPC-anot_1">
                        500
                      </a>
                    </td>
                    <td>
                      <a href="/fragments/fragment/Fr001/inter/Fr001_WIT_ED_VIRT_LdoD-JPC-anot_1/next">
                        <span className="glyphicon glyphicon-chevron-right"></span>
                      </a>
                    </td>
                    <td></td>
                  </tr>
                </tbody>
              </table>

              <div className="text-center" style="padding: 8px">
                <a href="/edition/acronym/LdoD-Jogo-className">
                  LdoD-Jogo-className
                </a>
              </div>
              <table width="100%">
                <thead>
                  <tr>
                    <th style={{width: "10%"}}></th>
                    <th style={{width: "10%"}}></th>
                    <th style={{width: "25%"}}></th>
                    <th style={{width: "10%"}}></th>
                    <th style={{width: "25%"}}></th>
                    <th style={{width: "20%"}}></th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td></td>
                    <td>
                      <input
                        type="checkbox"
                        name="281861523768033"
                        value="281861523768033"
                      />
                    </td>
                    <td>
                      <a href="/fragments/fragment/Fr001/inter/Fr001_WIT_ED_VIRT_LdoD-Jogo-Class_1/prev">
                        <span className="glyphicon glyphicon-chevron-left"></span>
                      </a>
                    </td>
                    <td>
                      <a href="/fragments/fragment/Fr001/inter/Fr001_WIT_ED_VIRT_LdoD-Jogo-Class_1">
                        230
                      </a>
                    </td>
                    <td>
                      <a href="/fragments/fragment/Fr001/inter/Fr001_WIT_ED_VIRT_LdoD-Jogo-Class_1/next">
                        <span className="glyphicon glyphicon-chevron-right"></span>
                      </a>
                    </td>
                    <td></td>
                  </tr>
                </tbody>
              </table>

              <div className="text-center" style="padding: 8px">
                <a href="/edition/acronym/LdoD-Mallet">LdoD-Mallet</a>
              </div>
              <table width="100%">
                <thead>
                  <tr>
                    <th style={{width: "10%"}}></th>
                    <th style={{width: "10%"}}></th>
                    <th style={{width: "25%"}}></th>
                    <th style={{width: "10%"}}></th>
                    <th style={{width: "25%"}}></th>
                    <th style={{width: "20%"}}></th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td></td>
                    <td>
                      <input
                        type="checkbox"
                        name="281861523768048"
                        value="281861523768048"
                      />
                    </td>
                    <td>
                      <a href="/fragments/fragment/Fr001/inter/Fr001_WIT_ED_VIRT_LdoD-Mallet_1/prev">
                        <span className="glyphicon glyphicon-chevron-left"></span>
                      </a>
                    </td>
                    <td>
                      <a href="/fragments/fragment/Fr001/inter/Fr001_WIT_ED_VIRT_LdoD-Mallet_1">
                        500
                      </a>
                    </td>
                    <td>
                      <a href="/fragments/fragment/Fr001/inter/Fr001_WIT_ED_VIRT_LdoD-Mallet_1/next">
                        <span className="glyphicon glyphicon-chevron-right"></span>
                      </a>
                    </td>
                    <td></td>
                  </tr>
                </tbody>
              </table>

              <div className="text-center" style="padding: 8px">
                <a href="/edition/acronym/LdoD-Twitter">LdoD-Twitter</a>
              </div>
              <table width="100%">
                <thead>
                  <tr>
                    <th style={{width: "10%"}}></th>
                    <th style={{width: "10%"}}></th>
                    <th style={{width: "25%"}}></th>
                    <th style={{width: "10%"}}></th>
                    <th style={{width: "25%"}}></th>
                    <th style={{width: "20%"}}></th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td></td>
                    <td>
                      <input
                        type="checkbox"
                        name="281861523795554"
                        value="281861523795554"
                      />
                    </td>
                    <td>
                      <a href="/fragments/fragment/Fr001/inter/Fr001_WIT_ED_VIRT_LdoD-Twitter_1/prev">
                        <span className="glyphicon glyphicon-chevron-left"></span>
                      </a>
                    </td>
                    <td>
                      <a href="/fragments/fragment/Fr001/inter/Fr001_WIT_ED_VIRT_LdoD-Twitter_1">
                        58
                      </a>
                    </td>
                    <td>
                      <a href="/fragments/fragment/Fr001/inter/Fr001_WIT_ED_VIRT_LdoD-Twitter_1/next">
                        <span className="glyphicon glyphicon-chevron-right"></span>
                      </a>
                    </td>
                    <td></td>
                  </tr>
                </tbody>
              </table>
            </div>
  </div>*/}
        </div>
      </div>
    </>
  );
};
