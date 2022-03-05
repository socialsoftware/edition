import { useParams } from 'react-router-dom';
import { useEffect } from 'react';
import { getNoAuthFragmentInter } from '../api/fragment';
import Fragment from './Fragment';
import FragmentMetaData from './FragmentMetaData';
import ReactTooltip from 'react-tooltip';
import { fragmentStore, resetCheckboxesState, setCheckboxesState } from '../fragmentStore';
const selector = (sel) => (state) => state[sel];

export default ({ messages, language }) => {
  const checkboxes = fragmentStore(selector('checkboxesState'));
  const fragmentExpert = fragmentStore(selector('fragmentExpert'));
  const { xmlid, urlid } = useParams();

  useEffect(() => {
    getNoAuthFragmentInter(xmlid, urlid);
    return () => resetCheckboxesState();
  }, [urlid, language]);


  return (
    <div className="col-md-9">
      <div id="fragment-inter" className="row">
        <form className="form-inline" role="form">
          <div className="form-group">
            <div
              id="visualisation-properties-authorial"
              className="btn-group"
            >
              <div className="checkbox tip">
                <ReactTooltip
                  id="diff-tooltip"
                  type="dark"
                  place="bottom"
                  effect="solid"
                  className="checkbox-tooltip"
                  getContent={() => messages?.[language]['diff_info']}
                />
                <label data-tip="" data-for="diff-tooltip">
                  <input
                    type="checkbox"
                    className="btn checkbox-props"
                    checked={checkboxes.diff}
                    onChange={() => setCheckboxesState('diff',!checkboxes.diff)}
                  />
                  {messages?.[language].diff}
                </label>
              </div>
            </div>
          </div>
        </form>
        <br />
        <Fragment fragment={fragmentExpert} fontFamily="Georgia" fontSize="medium" expert={true}/>
        <br />
        <FragmentMetaData
          messages={messages}
          language={language}
          sourceInter={fragmentExpert?.sourceInter}
          expert={true}
        />
      </div>
    </div>
  );
};
