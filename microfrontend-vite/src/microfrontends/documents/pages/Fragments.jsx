import { useEffect, useRef } from 'react';
import { getFragmentList } from '../api/documents';
import {
  documentStateSelector,
  setDocPath,
  setFilteredEncodedFragments,
  toggleShow,
} from '../documentsStore';
import Search from '../components/Search';

import DisplayDocModal from '../components/DisplayDocModal';
import Table from '../components/Table';

export default ({ messages, language }) => {
  const isMounted = useRef(false);
  const encodedFragments = documentStateSelector('encodedFragments');
  const filteredEncodedFragments = documentStateSelector(
    'filteredEncodedFragments'
  );

  useEffect(() => {
    !encodedFragments && getFragmentList(messages?.[language], displayDocument);
    return () => {
      isMounted.current = false;
      setFilteredEncodedFragments(null);
    };
  }, []);

  useEffect(() => {
    isMounted.current && getFragmentList(messages?.[language], displayDocument);
    isMounted.current = true;
  }, [language]);

  const displayDocument = (filename) => {
    toggleShow();
    setDocPath(filename);
  };

  return (
    <>
      {encodedFragments && (
        <>
          <DisplayDocModal />
          <h3 className="text-center">
            {messages?.[language]['fragment_codified']} (
            {filteredEncodedFragments?.length ?? encodedFragments?.length})
          </h3>
          <br />
          <Table
            data={encodedFragments}
            dataFiltered={filteredEncodedFragments ?? encodedFragments}
            setDataFiltered={setFilteredEncodedFragments}
            headers={messages?.[language]['fragments_table_headers']}
            classes="table table-hover"
            messages={messages[language]}
          />
        </>
      )}
    </>
  );
};
