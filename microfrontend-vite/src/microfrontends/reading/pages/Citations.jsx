import { useEffect, useState } from 'react';
import { getTwitterCitations } from '../api/reading';
import Search from '../components/Search';
import Table from '../components/Table';

export default ({ messages }) => {
  const [citations, setCitations] = useState();
  const searchStringState = useState();
  const [citationsFiltered, setCitationsFiltered] = useState();

  const formatData = (data) =>
    data?.map((entry) => {
      const { formatedDate, sourceLink, username } = entry;
      entry.formatedDate = `${formatedDate[2]}-${formatedDate[1]}-${formatedDate[0]} ${formatedDate[3]}:${formatedDate[4]}`;
      entry.sourceLink = `<a href="${sourceLink}" target="_blank">Tweet</a>`
      entry.username = `<a href="https://twitter.com/${username}" target="_blank">${username}</a>`
      return entry;
    });

  useEffect(() => {
    getTwitterCitations()
      .then(({ data }) => setCitations(formatData(data)))
      .catch((err) => {
        console.error(err);
        setCitations([]);
      });
  }, []);

  return (
    <>
      <h3 className="text-center">
        {messages?.['general_citations_twitter']} (
        {citationsFiltered?.length ?? citations?.length})
      </h3>
      <br />
      <div className="bootstrap-table">
        <div className="fixed-table-toolbar">
          <Search
            searchData={citations}
            searchStringState={searchStringState}
            setDataFiltered={setCitationsFiltered}
          />
        </div>
        <div className="fixed-table-container"  style={{ marginBottom: "20px" }}>
          <Table
            data={citationsFiltered ?? citations}
            headers={messages?.['citations_table_headers']}
          />
        </div>
      </div>
    </>
  );
};
