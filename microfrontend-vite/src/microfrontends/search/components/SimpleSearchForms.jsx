import { useState } from 'react';
import { getSimpleSearchResults } from '../api/search';

const setStateParam = (param, val, setter) => {
  setter((state) => ({ ...state, [param]: val }));
};

export default ({ messages }) => {
  const [searchParameters, setSearchParameters] = useState({
    searchTerm: '',
    searchSource: '',
    searchType: '',
  });

  const onSearch = () =>
    searchParameters.searchTerm && getSimpleSearchResults(searchParameters);

  return (
    <>
      <div className="form-group search-simple">
        <div className="col-sm-4">
          <input
            type="text"
            className="form-control tip"
            placeholder={messages.searchFor}
            value={searchParameters.searchTerm}
            onChange={(e) =>
              setStateParam('searchTerm', e.target.value, setSearchParameters)
            }
          />
        </div>

        <div className="col-sm-3">
          <div className="tip" title="text div">
            <select
              className="form-control"
              data-width="100%"
              id="searchType"
              value={searchParameters.searchType}
              onChange={(e) =>
                setStateParam('searchType', e.target.value, setSearchParameters)
              }
            >
              <option value="">{messages.completeSearch}</option>
              <option value="title">{messages.titleSearch}</option>
            </select>
          </div>
        </div>

        <div className="col-sm-3">
          <select
            className="form-control"
            data-width="100%"
            id="sourceType"
            value={searchParameters.searchSource}
            onChange={(e) =>
              setStateParam('searchSource', e.target.value, setSearchParameters)
            }
          >
            {['', 'coelho', 'cunha', 'zenith', 'pizarro', 'BNP'].map(
              (option, index) => (
                <option value={option} key={index}>
                  {messages[option] ?? messages.fontTypes}
                </option>
              )
            )}
          </select>
        </div>

        <div className="col-sm-2">
          <button
            className="btn btn-default"
            type="button"
            id="searchbutton"
            onClick={onSearch}
          >
            <span
              className="glyphicon glyphicon-search"
              style={{ marginRight: '5px' }}
            ></span>
            {messages.search}
          </button>
        </div>
      </div>
    </>
  );
};
