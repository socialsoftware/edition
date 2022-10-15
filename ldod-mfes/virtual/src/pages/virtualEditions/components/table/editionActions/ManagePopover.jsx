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
  await veAdminDelete(node.edition.externalId)
    .then(({ virtualEditions, user }) => {
      node.virtualEditions = virtualEditions;
      node.user = user;
      node.render();
    })
    .catch((message) => node.dispatchCustomEvent('ldod-error', message));
  node.edition = null;
};

const onEditorsModal = async (node) => {
  await import('../editors/LdodVeEditors');
  const element = node.querySelector('ldod-ve-editors');
  element.edition = await getVirtualEdition(node.edition.externalId).catch(
    (error) => node.dispatchCustomEvent('ldod-error', error)
  );
  element.parent = node;
  element.toggleAttribute('show');
};

const onGamesModal = async (node) => {
  await import('../games/LdodVeGames');
  const ldodVeGames = node.querySelector('ldod-ve-games');
  ldodVeGames.edition = node.edition;
  const data = await getEditionGames(node.edition.externalId).catch((error) =>
    node.dispatchCustomEvent('ldod-error', error)
  );
  ldodVeGames.updateData(data);
  ldodVeGames.parent = node;
  ldodVeGames.toggleAttribute('show');
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
  const data = await getVeIntersWithRecommendation(
    node.edition.externalId
  ).catch((error) => node.dispatchCustomEvent('ldod-error', error));
  ldodVeAssisted.parent = node;
  ldodVeAssisted.updateData(data);
};

const onManualModal = async (node) => {
  await import('../manual/LdodVeManual');
  const ldodVeManual = node.querySelector('ldod-ve-manual');
  ldodVeManual.edition = node.edition;
  const data = await getVeIntersForManual(node.edition.externalId).catch(
    (error) => node.dispatchCustomEvent('ldod-error', error)
  );
  ldodVeManual.parent = node;
  ldodVeManual.initialInters = Array.from(data);
  ldodVeManual.updateData(data);
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
