import { generateCitations, removeTweets } from '@src/api-requests.js';

export default ({ generate, remove, node }) => {
  const handleGenerateCitations = async () => {
    const data = await generateCitations();
    node.tweets = data;
    node.render();
  };

  const handleRemoveCitations = async () => {
    const data = await removeTweets();
    node.tweets = data;
    node.render();
  };

  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'center',
        flexWrap: 'wrap',
        gap: '20px',
        marginTop: '50px',
      }}>
      <button class="btn btn-success" onClick={handleGenerateCitations}>
        <span class="icon icon-plus"></span>
        <span data-key="generate">{generate}</span>
      </button>
      <button class="btn btn-danger" onClick={handleRemoveCitations}>
        <span class="icon icon-xmark"></span>
        <span data-key="remove" data-args={node.numberOfTweets}>
          {remove}
        </span>
      </button>
    </div>
  );
};
