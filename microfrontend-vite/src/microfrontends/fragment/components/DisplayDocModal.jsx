import Modal from 'react-modal';
import { fragmentStore, toggleShow } from '../fragmentStore';

export default () => {
  const { docPath, showModal } = fragmentStore();
  const toggleZoom = (e) => {
    const imgClasses = e.target.classList;
    imgClasses.contains('fac-image-zoomed')
      ? imgClasses.remove('fac-image-zoomed')
      : imgClasses.add('fac-image-zoomed');
  };

  return (
    <Modal
      isOpen={showModal}
      onRequestClose={toggleShow}
      ariaHideApp={false}
      shouldCloseOnOverlayClick={true}
      shouldCloseOnEsc={true}
      overlayClassName="overlay-modal"
      className="image-modal"
    >
      <button className="image-close" onClick={toggleShow}>
        Close
      </button>
      <img
        onClick={toggleZoom}
        className="fac-image"
        src={`https://ldod.uc.pt/facs/${docPath}`}
      />
    </Modal>
  );
};
