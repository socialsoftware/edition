import { generateTopics } from './taxonomyApiRequests';
import GeneratedTopicsTable from './GeneratedTopicsTable';

export default ({ node, veId }) => {
  const onGenerate = (e) => {
    e.preventDefault();
    const body = Object.fromEntries(new FormData(e.target));
    generateTopics(veId, body)
      .then((data) => {
        node.topics = data;
        node
          .querySelector('#generatedTopicsContainer')
          .replaceWith(<GeneratedTopicsTable node={node} data={data} />);
      })
      .catch((error) => console.error(error));

    // TODO handle Error
  };

  return (
    <ldod-modal id="virtual-generateTopicsModal" dialog-class="modal-xl">
      <span slot="header-slot">
        <span>{node.getConstants('generateTopicsTitle')}</span>
      </span>
      <div slot="body-slot">
        <div style={{ padding: '20px' }}>
          <form onSubmit={onGenerate}>
            <div
              id="generateTopicsInputs"
              style={{
                display: 'flex',
                justifyContent: 'center',
                gap: '20px',
                flexWrap: 'wrap',
              }}>
              <div class="form-floating">
                <input
                  name="numTopics"
                  type="number"
                  min={1}
                  max={100}
                  class="form-control"
                  id="topics"
                  required
                  placeholder="Topics"
                />
                <label data-virtual-key="topics">
                  {node.getConstants('topics')}
                </label>
              </div>
              <div class="form-floating">
                <input
                  name="numWords"
                  type="number"
                  min={1}
                  max={10}
                  class="form-control"
                  id="words"
                  required
                  placeholder="Words"
                />
                <label data-virtual-key="words">
                  {node.getConstants('words')}
                </label>
              </div>
              <div class="form-floating">
                <input
                  name="thresholdCategories"
                  type="number"
                  min={0}
                  class="form-control"
                  id="cut"
                  required
                  placeholder="threshold"
                />
                <label data-virtual-key="cut">{node.getConstants('cut')}</label>
              </div>
              <div class="form-floating">
                <input
                  name="numIterations"
                  type="input"
                  min={1}
                  class="form-control"
                  id="iterations"
                  required
                  placeholder="Iterations"
                />
                <label data-virtual-key="iterations">
                  {node.getConstants('iterations')}
                </label>
              </div>
              <button type="submit" class="btn btn-primary">
                <span class="icon icon-gear-light"></span>
                <span data-virtual-key="generate">
                  {node.getConstants('generate')}
                </span>
              </button>
              {['topics', 'words', 'cut', 'iterations'].map((id) => (
                <ldod-tooltip
                  data-ref={`input#${id}`}
                  data-virtual-tooltip-key={`${id}Info`}
                  width="500px"
                  content={node.getConstants(`${id}Info`)}></ldod-tooltip>
              ))}
            </div>
          </form>
          <div id="generatedTopicsContainer"></div>
        </div>
      </div>
      <div slot="footer-slot">
        <div>
          <button
            type="button"
            class="btn btn-primary"
            onClick={() => node.onAddTopics(veId)}>
            {node.getConstants('addCats')}
          </button>
        </div>
      </div>
    </ldod-modal>
  );
};
