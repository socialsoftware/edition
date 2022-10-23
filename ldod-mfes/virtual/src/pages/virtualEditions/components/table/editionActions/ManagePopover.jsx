import {
  veAdminDelete,
  getEditionGames,
  getVirtualEdition,
} from '@src/apiRequests';
import { getVeIntersWithRecommendation } from '@src/apiRequests';
import { getVeIntersForManual } from '../../../../../apiRequests';
import style from './manageStyle.css?inline';

const onRemoveVE = async (node) => {
  if (!confirm(`Delete ${node.edition.acronym} ?`)) return;
  veAdminDelete(node.edition.externalId)
    .then(({ virtualEditions, user }) => {
      node.virtualEditions = virtualEditions;
      node.user = user;
      node.render();
      node.edition = null;
    })
    .catch((message) => node.dispatchCustomEvent('ldod-error', message));
};

const onEditorsModal = async (node) => {
  await import('../editors/LdodVeEditors');
  const element = node.querySelector('ldod-ve-editors');
  getVirtualEdition(node.edition.externalId)
    .then((data) => {
      element.edition = data;
      element.parent = node;
      element.toggleAttribute('show');
    })
    .catch((error) => node.dispatchCustomEvent('ldod-error', error));
};

const onGamesModal = async (node) => {
  await import('../games/LdodVeGames');
  const ldodVeGames = node.querySelector('ldod-ve-games');
  ldodVeGames.edition = node.edition;
  getEditionGames(node.edition.externalId)
    .then((data) => {
      ldodVeGames.updateData(data);
      ldodVeGames.parent = node;
      ldodVeGames.toggleAttribute('show');
    })
    .catch((error) => node.dispatchCustomEvent('ldod-error', error));
};

const onEditVe = async (node) => {
  await import('../editVE/LdodVeEdit.jsx');
  const ldodVeEdit = node.querySelector('ldod-ve-edit');
  ldodVeEdit.edition = node.edition;
  ldodVeEdit.parent = node;
  ldodVeEdit.toggleAttribute('show');
};

const onAssistModal = async (node) => {
  await import('../assisted/LdodVeAssisted');
  const ldodVeAssisted = node.querySelector('ldod-ve-assisted');
  ldodVeAssisted.edition = node.edition;
  getVeIntersWithRecommendation(node.edition.externalId)
    .then((data) => {
      ldodVeAssisted.parent = node;
      ldodVeAssisted.updateData(data);
    })
    .catch((error) => node.dispatchCustomEvent('ldod-error', error));
};

const onManualModal = async (node) => {
  await import('../manual/LdodVeManual');
  const ldodVeManual = node.querySelector('ldod-ve-manual');
  ldodVeManual.edition = node.edition;
  getVeIntersForManual(node.edition.externalId)
    .then((data) => {
      ldodVeManual.parent = node;
      ldodVeManual.initialInters = Array.from(data);
      ldodVeManual.updateData(data);
    })
    .catch((error) => node.dispatchCustomEvent('ldod-error', error));
};

export default (node) => {
  return () => (
    <>
      <style>{style}</style>
      <ul class="manage-menu" id="virtual-managePopover">
        <li>
          <a class="manage-item" onClick={() => onGamesModal(node)}>
            <span class="icon-popover icon-popover-play"></span>
            <span data-virtualkey="game">{node.getConstants('game')}</span>
          </a>
        </li>
        <li>
          <a class="manage-item">
            <span class="icon-popover icon-popover-taxonomy"></span>
            <span data-virtualkey="taxonomy">
              {node.getConstants('taxonomy')}
            </span>
          </a>
        </li>
        <li>
          <a class="manage-item" onClick={() => onManualModal(node)}>
            <span class="icon-popover icon-popover-edit"></span>
            <span data-virtualkey="manualSort">
              {node.getConstants('manualSort')}
            </span>
          </a>
        </li>
        <li>
          <a class="manage-item" onClick={() => onAssistModal(node)}>
            <span class="icon-popover icon-popover-assist"></span>
            <span data-virtualkey="assisted">
              {node.getConstants('assisted')}
            </span>
          </a>
        </li>
        <li>
          <a class="manage-item" onClick={() => onEditorsModal(node)}>
            <span class="icon-popover icon-popover-users"></span>
            <span data-virtualkey="editors">
              {node.getConstants('editors')}
            </span>
          </a>
        </li>
        {node.edition?.member?.admin && (
          <>
            <li>
              <a class="manage-item" onClick={() => onEditVe(node)}>
                <span class="icon-popover icon-popover-pen"></span>
                <span data-virtualkey="edit">{node.getConstants('edit')}</span>
              </a>
            </li>
            <li>
              <a class="manage-item" onClick={() => onRemoveVE(node)}>
                <span class="icon-popover icon-popover-trash"></span>
                <span data-virtualkey="remove">
                  {node.getConstants('remove')}
                </span>
              </a>
            </li>
          </>
        )}
      </ul>
    </>
  );
};
