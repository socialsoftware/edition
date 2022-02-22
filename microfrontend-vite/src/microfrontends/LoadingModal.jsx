import Modal  from 'react-bootstrap/Modal';
export default () => {

  return (
    <Modal
      style={{ backgroundColor: 'transparent', border: 'none' }}
      show={true}
      backdrop="static"
      keyboard={false}
    >
      <Modal.Dialog style={{ backgroundColor: 'transparent', border: 'none' }}>
        <span>Loading...</span>
      </Modal.Dialog>
    </Modal>
  );
};
