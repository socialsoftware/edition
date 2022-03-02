import { useEffect, useRef } from 'react';
import { getLanguage } from '../../../store';
import { getTwitterCitations } from '../api/reading';
import { TwiterCitation } from '../CitationModel';
import Search from '../components/Search';
import Table from '../components/Table';
import {
  readingStore,
  setCitations,
  setFilteredCitations,
} from '../readingStore';
const selector = (sel) => (state) => state[sel];

export default ({ messages }) => {
  const citations = readingStore(selector('citations'));
  const filteredCitations = readingStore(selector('filteredCitations'));
console.log('teste');
  useEffect(() => {
    !citations &&
      getTwitterCitations()
        .then(({ data }) =>
          setCitations(data?.map((entry) => TwiterCitation(entry)))
        )
        .catch((err) => {
          console.error(err);
          setCitations([]);
        });
  }, []);

  return (
    <>
      <h3 className="text-center">
        {messages[getLanguage()]['general_citations_twitter']} (
        {filteredCitations?.length})
      </h3>
      <br />
      <div className="bootstrap-table">
        <div className="fixed-table-toolbar">
          <Search
            data={citations}
            setDataFiltered={setFilteredCitations}
          />
        </div>
        <div className="fixed-table-container" style={{ marginBottom: '20px' }}>
          <Table
            data={filteredCitations}
            headers={messages[getLanguage()]['citations_table_headers']}
            classes="table table-hover"
          />
        </div>
      </div>
    </>
  );
};
