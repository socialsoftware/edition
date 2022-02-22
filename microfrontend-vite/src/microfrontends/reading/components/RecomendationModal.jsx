import Modal from 'react-modal';
import { useParams } from 'react-router-dom';
import {
  getRecommendation,
  readingStore,
  resetRecommendations,
  setRecommendationAttribute,
} from '../readingStore';

export default ({ show, toggle, messages, fetchNumberFragment }) => {
  const { recommendation } = readingStore();
  const { xmlid, urlid } = useParams();


  const changeWeight = (attribute, value) =>
    setRecommendationAttribute(attribute, value);

  const onClose = () => {
    xmlid && urlid && fetchNumberFragment(xmlid, urlid, recommendation);
    toggle(false);
  };

  const reset = () => {
    resetRecommendations();
    onClose();
  };

  return (
    <Modal
      isOpen={show}
      onRequestClose={onClose}
      shouldCloseOnOverlayClick={true}
      shouldCloseOnEsc={true}
      ariaHideApp={false}
      className="recom-modal modal-content ldod-default"
      style={{
        overlay: {
          overflowX: 'hidden',
          overflowY: 'auto',
          position: 'fixed',
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          zIndex: 1050,
          outline: 0,
          backgroundColor: 'rgba(0, 0, 0, 0.5)',
        },
      }}
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
          {messages && messages['general_recommendation_config']}
        </h3>
      </div>
      <div className="modal-body">
        <h3 className="text-center">
          {messages && messages['recommendation_criteria']}
        </h3>
      </div>
      <div className="row text-center" style={{ padding: '15px' }}>
        <div className="col-md-3 col-sm-4">
          {messages && messages['heteronym']}
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
          {messages && messages['date']}
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
          {messages && messages['text']}
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
          {messages && messages['taxonomy']}
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
          {messages && messages['general_reset']}
        </button>

        <button type="button" className="btn btn-primary" onClick={onClose}>
          {messages && messages['general_close']}
        </button>
      </div>
    </Modal>
  );
};
