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
  const fragmentSource = fragmentStore(selector('fragmentSource'));
  const { xmlid, urlid } = useParams();

  useEffect(() => {
    getNoAuthFragmentInter(xmlid, urlid);
    return () => resetCheckboxesState();
  }, [urlid, language]);

  useEffect(() => {
  }, [checkboxes]);

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

              <div className="checkbox tip">
                <ReactTooltip
                  id="del-tooltip"
                  type="dark"
                  place="bottom"
                  effect="solid"
                  className="checkbox-tooltip"
                  getContent={() => messages?.[language]['del_info']}
                />
                <label data-tip="" data-for="del-tooltip">
                  <input
                    type="checkbox"
                    className="btn checkbox-props"
                    checked={checkboxes.del}
                    onChange={() => setCheckboxesState('del',!checkboxes.del)}
                  />
                  {messages?.[language].del}
                </label>
              </div>

              <div className="checkbox tip">
                <ReactTooltip
                  id="ins-tooltip"
                  type="dark"
                  place="bottom"
                  effect="solid"
                  className="checkbox-tooltip"
                  getContent={() => messages?.[language]['ins_info']}
                />
                <label data-tip="" data-for="ins-tooltip">
                  <input
                    type="checkbox"
                    className="btn checkbox-props"
                    checked={checkboxes.ins}
                    onChange={() => setCheckboxesState('ins',!checkboxes.ins)}
                  />
                  {messages?.[language].ins}
                </label>
              </div>

              <div className="checkbox tip">
                <ReactTooltip
                  id="sub-tooltip"
                  type="dark"
                  place="bottom"
                  effect="solid"
                  className="checkbox-tooltip"
                  getContent={() => messages?.[language]['sub_info']}
                />
                <label data-tip="" data-for="sub-tooltip">
                  <input
                    type="checkbox"
                    className="btn checkbox-props"
                    checked={checkboxes.sub}
                    onChange={() => setCheckboxesState('sub',!checkboxes.sub)}
                  />
                  {messages?.[language].sub}
                </label>
              </div>

              <div className="checkbox tip">
                <ReactTooltip
                  id="note-tooltip"
                  type="dark"
                  place="bottom"
                  effect="solid"
                  className="checkbox-tooltip"
                  getContent={() => messages?.[language]['note_info']}
                />
                <label data-tip="" data-for="note-tooltip">
                  <input
                    type="checkbox"
                    className="btn checkbox-props"
                    checked={checkboxes.note}
                    onChange={() => setCheckboxesState('note',!checkboxes.note)}
                  />
                  {messages?.[language].note}
                </label>
              </div>

              <div className="checkbox tip">
                <ReactTooltip
                  id="fac-tooltip"
                  type="dark"
                  place="bottom"
                  effect="solid"
                  className="checkbox-tooltip"
                  getContent={() => messages?.[language]['fac_info']}
                />
                <label data-tip="" data-for="fac-tooltip">
                  <input
                    type="checkbox"
                    className="btn checkbox-props"
                    checked={checkboxes.fac}
                    onChange={() => setCheckboxesState('fac',!checkboxes.fac)}
                  />
                  {messages?.[language].fac}
                </label>
              </div>
            </div>
          </div>
        </form>
        <br />
        <Fragment fragment={fragmentSource} fontFamily="Courier"/>
        <br />
        <FragmentMetaData
          messages={messages}
          language={language}
          sourceInter={fragmentSource?.sourceInter}
        />
      </div>
    </div>
  );
};
