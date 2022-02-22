import ReactTooltip from 'react-tooltip';
import RecomendationFrags from './RecomendationFrags';

export default ({ messages, state }) => {
  return (
    <div className="reading__column col-xs-12 col-sm-1 no-pad recommendation-line">
      <h4 className="f--condensed">
        <a href="#" className="f--condensed--link">
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
      {state && <RecomendationFrags state={state} />}
    </div>
  );
};
