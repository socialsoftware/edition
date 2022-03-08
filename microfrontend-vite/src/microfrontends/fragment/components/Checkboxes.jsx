import ReactTooltip from 'react-tooltip';

const checkboxesSelector =  (state) => state.checkboxesState;


import { fragmentStore,setCheckboxesState } from '../fragmentStore';

export default ({ messages, language, checkboxes}) => {
  const checkboxesState = fragmentStore(checkboxesSelector);
  return (
    <form className="form-inline" role="form">
      <div className="form-group">
        <div id="visualisation-properties-authorial" className="btn-group">
          {checkboxesState &&
            checkboxes.map((cb, index) => (
              <div key={index} className="checkbox tip">
                <ReactTooltip
                  id={`${cb}-tooltip`}
                  type="dark"
                  place="bottom"
                  effect="solid"
                  className="checkbox-tooltip"
                  getContent={() => messages?.[language][`${cb}_info`]}
                />
                <label data-tip="" data-for={`${cb}-tooltip`}>
                  <input
                    type="checkbox"
                    className="btn checkbox-props"
                    checked={checkboxesState[cb]}
                    onChange={() =>
                      setCheckboxesState(cb, !checkboxesState[cb])
                    }
                  />
                  {messages?.[language][cb]}
                </label>
              </div>
            ))}
        </div>
      </div>
    </form>
  );
} 

