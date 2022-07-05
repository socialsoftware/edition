import { useEffect } from 'react';
import Table from '../../../shared/Table';
import { getTwitterCitations } from '../api/reading';
import { readingStateSelector, setLength } from '../readingStore';

export default ({ messages }) => {
  const citations = readingStateSelector('citations');
  const length = readingStateSelector('length');

  useEffect(() => {
    !citations && getTwitterCitations();
  }, []);

  return (
    <>
      <h3 className="text-center">
        {messages['general_citations_twitter']} ({length})
      </h3>
      <br />
      <Table
        data={citations}
        labels={messages.citationsTableLabels}
        classes="table table-hover"
        setLength={setLength}
        pagination
        search
      />
    </>
  );
};
