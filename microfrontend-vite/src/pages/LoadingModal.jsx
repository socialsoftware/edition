import Modal from 'react-modal';
import {
  setLoading,
  storeStateSelector,
} from '../store';

export default () => {
  
  const loading = storeStateSelector('loading')

  return (
    <Modal
      isOpen={loading}
      onRequestClose={() => setLoading(false)}
      ariaHideApp={false}
      shouldCloseOnOverlayClick={false}
      shouldCloseOnEsc={false}
      overlayClassName="overlay-modal"
      className="loading-modal"
    >
      <div className="lds-dual-ring"></div>
    </Modal>
  );
};
