import { useEffect } from 'react';
import { getTwitterCitations } from '../api/reading';
import { TwiterCitation } from '../CitationModel';
import Search from '../components/Search';
import Table from '../components/Table';
import {useStore} from '../../../store'
import {
  readingStore,
  setCitations,
  setFilteredCitations,
} from '../readingStore';
const selector = (sel) => (state) => state[sel];

export default ({ messages }) => {
  const language = useStore(selector('language'))
  const citations = readingStore(selector('citations'));
  const filteredCitations = readingStore(selector('filteredCitations'));
  
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
    //    return () => setFilteredCitations(null)
  }, []);

  return (
    <>
      <h3 className="text-center">
        {messages[language]['general_citations_twitter']} (
        {filteredCitations?.length ?? citations?.length})
      </h3>
      <br />
      <div className="bootstrap-table">
        <div className="fixed-table-toolbar">
          <Search data={citations} setDataFiltered={setFilteredCitations} />
        </div>
        <div className="fixed-table-container" style={{ marginBottom: '20px' }}>
          <Table
            data={filteredCitations ?? citations}
            headers={messages[language]['citations_table_headers']}
            classes="table table-hover"
          />
        </div>
      </div>
    </>
  );
};
