import { useEffect, useState } from 'react';
import ReactTooltip from 'react-tooltip';
import DisplayDocModal from '../../../shared/DisplayDocModal';
import Table from '../../../shared/Table';
import { getSourceList } from '../api/documents';
import {
  documentStateSelector, setDocPath,
  setSourceLength, toggleShow
} from '../documentsStore';

export default ({ messages }) => {
  const [mounted, setMounted] = useState(false);
  const sourceList = documentStateSelector('sourceList');
  const length = documentStateSelector('sourceLength');
  const showModal = documentStateSelector('showModal');
  const docPath = documentStateSelector('docPath');
  
  useEffect(() => {
    !sourceList && getSourceList(messages, displayDocument);
    mounted && getSourceList(messages, displayDocument);
    setMounted(true)
  }, [messages]);


  const displayDocument = (filename) => {
    toggleShow();
    setDocPath(filename);
  };

  return (
    <>
      {sourceList && (
        <>
          <DisplayDocModal toggleShow={toggleShow} showModal={showModal} docPath={docPath}/>
          <h3 className="text-center">
            {messages['authorial_source']} ({length})
            <ReactTooltip
              id="recom"
              type="light"
              place="bottom"
              effect="solid"
              className="info-tooltip"
              border={true}
              getContent={() => messages['source_info']}
            />
            <span
              data-tip=""
              data-for="recom"
              className="glyphicon glyphicon-info-sign"
            ></span>
          </h3>
          <br />
          <Table
            data={sourceList}
            labels={messages.sourceTableLabels}
            classes="table table-hover table-striped table-bordered"
            setLength={setSourceLength}
            pagination
            search
          />
        </>
      )}
    </>
  );
};
