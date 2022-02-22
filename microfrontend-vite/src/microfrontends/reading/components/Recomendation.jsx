import ReactTooltip from 'react-tooltip';
import RecomendationFrags from './RecomendationFrags';
import { useState } from 'react';
import RecomendationModal from './RecomendationModal';

export default ({ messages, state, fetchPrevRecom, fetchNumberFragment }) => {
  const [showModal, setShowModal] = useState(false);
  return (
    <>
      <RecomendationModal
        show={showModal}
        toggle={setShowModal}
        messages={messages}
        fetchNumberFragment={fetchNumberFragment}
      />
      <div className="reading__column col-xs-12 col-sm-1 no-pad recommendation-line">
        <h4 className="f--condensed">
          <a onClick={() => setShowModal(true)} className="f--condensed--link">
            {messages && messages['general_recommendation']}
          </a>
          <span className="visible-xs-inline">&nbsp;</span>

          <ReactTooltip
            id="recom"
            type="light"
            place="bottom"
            effect="solid"
            className="reading-tooltip"
            border={true}
            getContent={() => messages && messages['reading_tt_recom']}
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
    </>
  );
};
