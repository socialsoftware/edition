import Modal from 'react-modal';

export default ({docPath, showModal, toggleShow}) => {
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
