import SimpleSearchForms from '../components/SimpleSearchForms';
import Table from '../../../shared/Table';
import { useEffect } from 'react';
import { searchStateSelector, setSearchResult } from '../searchStore';

const addLengthToLabels = (labels, frags, inters) => ({
  fragment: `${labels.fragment} (${frags})`,
  interpts: `${labels.interpts} (${inters})`,
});

export default ({ messages }) => {
  const searchResult = searchStateSelector('searchResult');

  useEffect(() => {
    setSearchResult();
  }, [messages]);

  return (
    <>
      <h3 className="text-center">{messages.simpleSearch}</h3>
      <br />
      <br />
      <SimpleSearchForms messages={messages} resultSetter={setSearchResult} />
      <br />
      <br />
      <div style={{ display: 'block', width: '100%' }}>
        {searchResult?.tableData && (
          <>
            <hr />
            <Table
              classes="table table-hover"
              data={searchResult.tableData}
              labels={addLengthToLabels(
                messages.simpleResultsLabels,
                searchResult.fragCount,
                searchResult.interCount
              )}
              pagination
              search
            />
          </>
        )}
      </div>
    </>
  );
};
