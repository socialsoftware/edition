import Modal from 'react-modal';
import { isLoading, setLoading } from '../../store';

export default () => (
  <Modal
    isOpen={isLoading()}
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
