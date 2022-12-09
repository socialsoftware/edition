import constants from '@src/pages/constants';
import style from './create-ve-form-style.css?inline';

export default ({ node }) => {
  return (
    <form id="VEForm" onSubmit={node.onCreateVE}>
      <style>{style}</style>
      <div class="form-row">
        <div class="form-col">
          <div class="input-group">
            <span class="input-group-text">LdoD-</span>
            <div class="form-floating">
              <input
                name="acronym"
                type="text"
                class="form-control"
                id="headerVeInfo_1"
                required
                placeholder="Acronym"
              />
              <label data-virtualkey="acronym">
                {node.getConstants('acronym')}
              </label>
            </div>
          </div>
        </div>
        <div class="form-col" style={{ width: '100%' }}>
          <div class="form-floating">
            <input
              id="headerVeInfo_2"
              class="form-control"
              type="text"
              name="title"
              required
              placeholder="Title"
            />
            <label data-virtualkey="title">{node.getConstants('title')}</label>
          </div>
        </div>
      </div>
      <div class="form-row">
        <div class="form-col">
          <div class="form-floating">
            <select name="pub" class="form-select" id="headerVeInfo_4">
              <option data-virtualkey="public" value="true">
                {node.getConstants('public')}
              </option>
              <option data-virtualkey="private" value="false">
                {node.getConstants('private')}
              </option>
            </select>
            <label for="public">{node.getConstants('access')}</label>
          </div>
        </div>
        <div class="form-col">
          <div class="form-floating">
            <select name="useEdition" class="form-select" id="headerVeInfo_5">
              <option value="">None</option>
              <option value="JPC">{constants.JPC}</option>
              <option value="TSC">{constants.TSC}</option>
              <option value="RZ">{constants.RZ}</option>
              <option value="JP">{constants.JP}</option>
              <option value="LdoD-Arquivo">LdoD-Arquivo</option>

              {node.virtualEditions.map(({ acronym }) => (
                <option value={acronym}>{acronym}</option>
              ))}
            </select>
            <label data-virtualkey="useEdition" for="public">
              {node.getConstants('useEdition')}
            </label>
          </div>
        </div>
      </div>
      <div style={{ display: 'flex', justifyContent: 'end' }}>
        <button
          type="submit"
          class="btn btn-primary"
          data-virtualkey="veCreate">
          {node.getConstants('veCreate')}
        </button>
      </div>
      {[1, 2, 4, 5].map((ind) => (
        <ldod-tooltip
          data-ref={`form #headerVeInfo_${ind}`}
          data-virtualtooltipkey={`headerVeInfo_${ind}`}
          placement="top"
          width="250px"
          content={node.getConstants(`headerVeInfo_${ind}`)}></ldod-tooltip>
      ))}
    </form>
  );
};
