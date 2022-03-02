import ReactTooltip from 'react-tooltip';
import RecomendationFrags from './RecomendationFrags';
import { useState } from 'react';
import RecomendationModal from './RecomendationModal';

export default ({
  messages,
  state,
  fetchPrevRecom,
  fetchNumberFragment,
  language,
}) => {
  const [showModal, setShowModal] = useState(false);
  return (
    <div className="thin-column">
      <RecomendationModal
        show={showModal}
        toggle={setShowModal}
        messages={messages}
        language={language}
        fetchNumberFragment={fetchNumberFragment}
      />
      <div className="reading__column col-xs-12 no-pad recommendation-line">
        <h4 className="f--condensed">
          <a onClick={() => setShowModal(true)} className="f--condensed--link">
            {messages[language]['general_recommendation']}
          </a>
          <span className="visible-xs-inline">&nbsp;</span>

          <ReactTooltip
            id="recom"
            type="light"
            place="bottom"
            effect="solid"
            className="info-tooltip"
            border={true}
            getContent={() => messages[language]['reading_tt_recom']}
          />
          <span
            data-tip=""
            data-for="recom"
            className="glyphicon glyphicon-info-sign"
          ></span>
        </h4>
        {state && (
          <RecomendationFrags
            state={state}
            fetchPrevRecom={fetchPrevRecom}
            fetchNumberFragment={fetchNumberFragment}
          />
        )}
      </div>
    </div>
  );
};
