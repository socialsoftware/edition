import { submitParticipation } from '@src/apiRequests';

const onSubmitParticipation = (node, id) => {
  submitParticipation(id)
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
        id="action_3"
        class="icon icon-add"
        onClick={() => onSubmitParticipation(node, edition.externalId)}></span>
      <ldod-tooltip
        data-ref={`tr[id="${edition.externalId}"] #action_3`}
        data-virtualtooltipkey={`action_3`}
        placement="right"
        content={node.getConstants(`action_3`)}></ldod-tooltip>
    </>
  );
};
