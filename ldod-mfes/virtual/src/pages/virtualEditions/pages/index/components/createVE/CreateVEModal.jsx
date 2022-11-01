import 'shared/modal.js';
import CreateVEForm from './createVEForm';

export default ({ node }) => {
  return (
    <>
      <ldod-modal id="virtual-veCreateModal" dialog-class="modal-lg" no-footer>
        <span data-virtualkey="modalTitle" slot="header-slot">
          {node.getConstants('modalTitle')}
        </span>
        <div slot="body-slot">
          <CreateVEForm node={node} />
        </div>
      </ldod-modal>
    </>
  );
};
