import { useEffect, useRef } from 'react';
import { getSourceList } from '../api/documents';
import {
  documentStateSelector,
  setFilteredSourceList,
  toggleShow,
  setDocPath,
} from '../documentsStore';
import Search from '../components/Search';
import Table from '../components/Table';
import ReactTooltip from 'react-tooltip';
import DisplayDocModal from '../components/DisplayDocModal';

export default ({ messages, language }) => {
  const isMounted = useRef(false);
  const sourceList = documentStateSelector('sourceList');
  const filteredSourceList = documentStateSelector('filteredSourceList');
  useEffect(() => {
    !sourceList && getSourceList(messages?.[language], displayDocument);
    return () => {
      isMounted.current = false;
      setFilteredSourceList(null);
    };
  }, []);

  useEffect(() => {
    isMounted.current && getSourceList(messages?.[language], displayDocument);
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
        {messages?.[language]['authorial_source']} (
        {filteredSourceList?.length ?? sourceList?.length})
        <ReactTooltip
          id="recom"
          type="light"
          place="bottom"
          effect="solid"
          className="info-tooltip"
          border={true}
          getContent={() => messages?.[language]['source_info']}
        />
        <span
          data-tip=""
          data-for="recom"
          className="glyphicon glyphicon-info-sign"
        ></span>
      </h3>
      <div className="bootstrap-table">
        <div className="fixed-table-toolbar">
          <Search
            data={sourceList}
            setDataFiltered={setFilteredSourceList}
            language={language}
          />
        </div>
        <div className="fixed-table-container" style={{ marginBottom: '20px' }}>
          <Table
            data={filteredSourceList ?? sourceList}
            headers={messages?.[language]['source_table_headers']}
            classes="table table-hover table-striped table-bordered"
          />
        </div>
      </div>
    </>
  );
};
