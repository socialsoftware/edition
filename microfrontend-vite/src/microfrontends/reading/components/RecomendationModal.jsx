import Modal from 'react-modal';
import { useParams } from 'react-router-dom';
import { getCurrentReadingFragment } from '../api/reading';
import {
  getRecommendation,
  readingStore,
  resetRecommendations,
  setRecommendationAttribute,
} from '../readingStore';

export default ({ show, toggle, messages }) => {
  const { recommendation } = readingStore();
  const { xmlid, urlid } = useParams();

  const onClose = () => toggle(false);

  const changeWeight = (attribute, value) =>
    setRecommendationAttribute(attribute, value);

  const recommend = () => {
    xmlid && urlid && getCurrentReadingFragment(xmlid, urlid, getRecommendation());
    onClose();
  };

  const reset = () => {
    resetRecommendations();
    recommend();
  };

  return (
    <Modal
      isOpen={show}
      onRequestClose={onClose}
      shouldCloseOnOverlayClick={true}
      shouldCloseOnEsc={true}
      ariaHideApp={false}
      overlayClassName="overlay-modal"
      className="recom-modal modal-content ldod-default"
    >
      <div className="modal-header">
        <button
          type="button"
          className="close"
          aria-label="Close"
          onClick={onClose}
        >
          <span aria-hidden="true">×</span>
        </button>
        <h3 className="modal-title text-center">
          {messages['general_recommendation_config']}
        </h3>
      </div>
      <div className="modal-body">
        <h3 className="text-center">{messages['recommendation_criteria']}</h3>
      </div>
      <div className="row text-center" style={{ padding: '15px' }}>
        <div className="col-md-3 col-sm-4">
          {messages['heteronym']}
          <input
            type="range"
            className="range"
            onChange={(e) => changeWeight('heteronymWeight', e.target.value)}
            value={recommendation.heteronymWeight}
            max="1"
            min="0"
            step="0.2"
          />
        </div>
        <div className="col-md-3 col-sm-4">
          {messages['date']}
          <input
            type="range"
            className="range"
            onChange={(e) => changeWeight('dateWeight', e.target.value)}
            value={recommendation.dateWeight}
            max="1"
            min="0"
            step="0.2"
          />
        </div>
        <div className="col-md-3 col-sm-4">
          {messages['text']}
          <input
            type="range"
            className="range"
            onChange={(e) => changeWeight('textWeight', e.target.value)}
            value={recommendation.textWeight}
            max="1"
            min="0"
            step="0.2"
          />
        </div>
        <div className="col-md-3 col-sm-4">
          {messages['taxonomy']}
          <input
            type="range"
            className="range"
            onChange={(e) => changeWeight('taxonomyWeight', e.target.value)}
            value={recommendation.taxonomyWeight}
            max="1.0"
            min="0.0"
            step="0.2"
          />
        </div>
      </div>
      <div className="modal-footer">
        <button type="submit" className="btn btn-danger" onClick={reset}>
          <span className="glyphicon glyphicon-saved"></span>
          {messages['general_reset']}
        </button>

        <button type="button" className="btn btn-primary" onClick={recommend}>
          {messages['general_close']}
        </button>
      </div>
    </Modal>
  );
};
