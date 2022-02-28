import Modal from 'react-modal';
import { useRef } from 'react';

export default ({ showModal, setShowModal }) => {
  const toggleZoom = (e) => {
    const imgClasses = e.target.classList;
    imgClasses.contains('fac-image-zoomed')
      ? imgClasses.remove('fac-image-zoomed')
      : imgClasses.add('fac-image-zoomed');
  };
  return (
    <Modal
      isOpen={showModal ? true : false}
      onRequestClose={() => setShowModal()}
      ariaHideApp={false}
      shouldCloseOnOverlayClick={true}
      shouldCloseOnEsc={true}
      overlayClassName="overlay-modal"
      className="image-modal"
    >
      <button className='image-close' onClick={() => setShowModal()}>Close</button>
      <img
        onClick={toggleZoom}
        className="fac-image"
        src={`https://ldod.uc.pt/facs/${showModal}`}
      />

    </Modal>
  );
};
