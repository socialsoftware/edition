import { useEffect, useRef } from 'react';
import { getFragmentList } from '../api/documents';
import {
  documentsStore,
  setDocPath,
  setFilteredEncodedFragments,
  toggleShow,
} from '../documentsStore';
import Search from '../components/Search';

import DisplayDocModal from '../components/DisplayDocModal';
import Table from '../components/Table';
const selector = (sel) => (state) => state[sel];

export default ({ messages, language }) => {
  const isMounted = useRef(false);
  const encodedFragments = documentsStore(selector('encodedFragments'));
  const filteredEncodedFragments = documentsStore(
    selector('filteredEncodedFragments')
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
      <DisplayDocModal />
      <h3 className="text-center">
        {messages?.[language]['fragment_codified']} (
        {filteredEncodedFragments?.length ?? encodedFragments?.length})
      </h3>
      <div className="bootstrap-table">
        <div className="fixed-table-toolbar">
          <Search
            data={encodedFragments}
            setDataFiltered={setFilteredEncodedFragments}
            language={language}
          />
        </div>
        <div className="fixed-table-container" style={{ marginBottom: '20px' }}>
          <Table
            data={filteredEncodedFragments ?? encodedFragments}
            headers={messages?.[language]['fragments_table_headers']}
            classes="table table-hover"
          />
        </div>
      </div>
    </>
  );
};
