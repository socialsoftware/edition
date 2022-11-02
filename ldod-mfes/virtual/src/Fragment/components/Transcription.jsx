import { dom } from 'shared/utils.js';
import Taxonomy from './Taxonomy';

export default ({ node }) => {
  const inter = node.inter;
  const taxonomy = node.taxonomy;
  return (
    <>
      <h4>
        {`${inter.editionTitle} - `}
        <span data-virtual-key="uses">{node.getConstants('uses')}</span>
        <span>{` ${inter.usesEditionReference} (${inter.usesReference})`}</span>
      </h4>
      <div id="virtual-content">
        <h4 class="text-center">{inter.title}</h4>
        <div id="virtual-transcription" class="well">
          <p></p>
          {dom(inter.transcription)}
        </div>
        {taxonomy.canManipulateAnnotation && (
          <div
            id="virtual-associateButton"
            style={{ display: 'flex', justifyContent: 'end' }}>
            <button
              title={`Associate new category to '${inter.title}' interpretation`}
              type="button"
              class="btn btn-sm btn-primary"
              onClick={node.associateTag}>
              <span
                class="icon icon-plus"
                style={{
                  margin: '0',
                  pointerEvents: 'none',
                }}></span>
            </button>
          </div>
        )}

        <div id="virtual-taxonomy">
          <Taxonomy node={node} inter={inter} />
        </div>
      </div>
    </>
  );
};
