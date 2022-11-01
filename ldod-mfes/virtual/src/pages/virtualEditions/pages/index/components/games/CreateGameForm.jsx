import { createClassGame } from '@src/restrictedApiRequests';

const addZero = (num) => String(num).padStart(2, '0');

const getCurrentDateTime = () => {
  const date = new Date();
  return `${date.getFullYear()}-${addZero(date.getMonth() + 1)}-${addZero(
    date.getDate()
  )}T${addZero(date.getHours())}:${addZero(date.getMinutes())}`;
};

const onCreateCG = async (e, node) => {
  e.preventDefault();
  const gameBody = Object.fromEntries(new FormData(e.target));
  const data = await createClassGame(node.edition.externalId, gameBody);
  node.updateData(data);
};

export default ({ node }) => {
  return (
    <>
      <h4 class="text-center">{node.getConstants('createCG')}</h4>
      <div class="mb-4">
        <label class="control-label">
          <strong>{node.getConstants('players')}:</strong>
        </label>{' '}
        <em>
          {node.pubAnnotation
            ? node.getConstants('all')
            : node.getConstants('members')}
        </em>
      </div>
      <form onSubmit={(e) => onCreateCG(e, node)}>
        <div
          style={{
            display: 'flex',
            justifyContent: 'space-between',
            columnGap: '16px',
          }}>
          <div style={{ width: '90%' }}>
            <div
              style={{
                display: 'flex',
                justifyContent: 'space-between',
                columnGap: '16px',
                marginBottom: '16px',
              }}>
              <div style={{ width: '100%' }} class="form-floating">
                <input
                  id="gameDescription"
                  class="form-control"
                  type="text"
                  name="description"
                  required
                  placeholder={node.getConstants('description')}
                />
                <label>{node.getConstants('description')}</label>
              </div>

              <div style={{ width: '100%' }} class="form-floating">
                <input
                  id="gameDateTime"
                  class="form-control"
                  type="datetime-local"
                  name="date"
                  value={getCurrentDateTime()}
                  required
                  placeholder={node.getConstants('date')}
                />
                <label>{node.getConstants('date')}</label>
              </div>
            </div>

            <div class="form-floating">
              <select
                name="interExternalId"
                class="form-select"
                id="gameInter"
                required
                placeholder={node.getConstants('inter')}>
                {node.inters.map((inter) => (
                  <option value={inter.externalId}>{inter.title}</option>
                ))}
              </select>
              <label for="public">{node.getConstants('inter')}</label>
            </div>
          </div>
          <button
            style={{ width: '10%' }}
            type="submit"
            class="btn btn-primary">
            {node.getConstants('create')}
          </button>
        </div>
      </form>
    </>
  );
};
