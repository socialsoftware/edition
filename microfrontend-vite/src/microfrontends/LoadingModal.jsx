import Modal from 'react-modal';
import { useEffect } from 'react';
import { isLoading, setLoading, useStore} from '../store';

export default () => {
 // const {loading} = useStore();
//  useEffect(() => setLoading(loading),[loading]);
  

  return (
    <Modal
      isOpen={isLoading()}
      onRequestClose={() => setLoading(false)}
      ariaHideApp={false}
      shouldCloseOnOverlayClick={false}
      shouldCloseOnEsc={false}
      overlayClassName="overlay-modal"
      className="loading-modal"

    >
      <div className='lds-dual-ring'></div>
    </Modal>
  );
};
