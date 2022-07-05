import { useEffect, useState } from 'react';
import DisplayDocModal from '../../../shared/DisplayDocModal';
import Table from '../../../shared/Table';
import { getFragmentList } from '../api/documents';
import {
  documentStateSelector,
  setDocPath, setFragmentLength, toggleShow
} from '../documentsStore';


export default ({ messages }) => {
  const [mounted, setMounted] = useState(false);
  const encodedFragments = documentStateSelector('encodedFragments');
  const length = documentStateSelector('fragmentLength');
  const showModal = documentStateSelector('showModal');
  const docPath = documentStateSelector('docPath');

  useEffect(() => {
    !encodedFragments && getFragmentList(messages, displayDocument);
    mounted && getFragmentList(messages, displayDocument);
    setMounted(true);
  }, [messages]);

  const displayDocument = (filename) => {
    setDocPath(filename);
    toggleShow();
  };

  return (
    <>
      {encodedFragments && (
        <>
          <DisplayDocModal
            toggleShow={toggleShow}
            docPath={docPath}
            showModal={showModal}
          />
          <h3 className="text-center">
            {messages['fragment_codified']} ({length})
          </h3>
          <br />
          <Table
            data={encodedFragments}
            classes="table table-hover"
            labels={messages.fragmentsTableLabels}
            setLength={setFragmentLength}
            itemsPerPage={10}
            pagination
            search
          />
        </>
      )}
    </>
  );
};
