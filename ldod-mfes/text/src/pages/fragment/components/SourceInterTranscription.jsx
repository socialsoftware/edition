import { dom } from 'shared/utils.js';
import Viewer from './Viewer';

export default ({ node, inter, clazz, key }) => {
  return (
    <>
      <div
        class={
          node.transcriptionCheckboxes.fac
            ? 'text-fac-transcription'
            : 'text-nofac-transcription'
        }>
        {node.transcriptionCheckboxes.fac && (
          <Viewer surfaceList={inter.surfaceDetailsList} />
        )}
        <div class="well authorialStyle" id="transcriptionContainer">
          {clazz ? (
            <p class={clazz}>{dom(node.data.transcriptions[key])}</p>
          ) : (
            dom(node.data.transcriptions[0])
          )}
        </div>
      </div>
    </>
  );
};
