import ReactTooltip from 'react-tooltip';
import RecomendationFrags from './RecomendationFrags';
import { useState } from 'react';
import RecomendationModal from './RecomendationModal';

export default ({ messages, fragment }) => {
  const [showModal, setShowModal] = useState(false);
  return (
    <div className="thin-column">
      <RecomendationModal
        show={showModal}
        toggle={setShowModal}
        messages={messages}
      />
      <div className="reading__column col-xs-12 no-pad recommendation-line">
        <h4 className="f--condensed">
          <a onClick={() => setShowModal(true)} className="f--condensed--link">
            {messages['general_recommendation']}
          </a>
          <span className="visible-xs-inline">&nbsp;</span>

          <ReactTooltip
            id="recom"
            type="light"
            place="bottom"
            effect="solid"
            border={true}
            borderColor="rgba(0, 0, 0, 0.2)"
            getContent={() => messages['reading_tt_recom']}
            className="info-tooltip"
          />
          <span
            data-tip=""
            data-for="recom"
            className="glyphicon glyphicon-info-sign"
          ></span>
        </h4>
        {fragment && <RecomendationFrags fragment={fragment} />}
      </div>
    </div>
  );
};
