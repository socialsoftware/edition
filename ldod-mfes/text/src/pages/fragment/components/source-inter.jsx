import { getSourceInter } from '../../fragments/components/source-inter';
import Checkboxes from './checkboxes';
import SourceInterTranscription from './source-inter-transcription';
import Title from './title';
const cbs = ['diff', 'del', 'ins', 'sub', 'note', 'fac'];

export default ({ node, inter }) => {
  return (
    <>
      <Checkboxes node={node} checkboxes={cbs} />
      {cbs.map((cb) => {
        return (
          <ldod-tooltip
            data-ref={`div#${cb}`}
            data-tooltipkey={`${cb}Info`}
            placement="top"
            content={node.getConstants(`${cb}Info`)}></ldod-tooltip>
        );
      })}
      <Title title={node.data.title} />
      <SourceInterTranscription node={node} inter={inter} key={0} />
      <br />
      <div id="interMetaIndo" class="well">
        {getSourceInter(inter, node, 0)}
      </div>
    </>
  );
};
