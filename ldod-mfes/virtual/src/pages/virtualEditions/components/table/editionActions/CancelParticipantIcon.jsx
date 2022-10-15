import { cancelParticipation } from '@src/apiRequests';

const onCancelParticipation = (node, id) => {
  cancelParticipation(id)
    .then((ve) => node.updateEdition(ve))
    .catch((error) => {
      error?.message &&
        node.dispatchCustomEvent('ldod-error', { message: error.message });
    });
};

export default ({ node, edition }) => {
  return (
    <>
      <span
        id="action_4"
        class="icon icon-minus"
        data-external-id={edition.externalId}
        onClick={() => onCancelParticipation(node, edition.externalId)}></span>
      <ldod-tooltip
        data-ref={`tr[id="${edition.externalId}"] #action_4`}
        data-virtualtooltipkey={`action_4`}
        placement="right"
        content={node.getConstants(`action_4`)}></ldod-tooltip>
    </>
  );
};
