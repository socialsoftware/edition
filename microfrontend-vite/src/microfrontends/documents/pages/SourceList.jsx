import { useEffect, useRef } from 'react';
import { getSourceList } from '../api/documents';
import {
  documentStateSelector,
  setFilteredSourceList,
  toggleShow,
  setDocPath,
} from '../documentsStore';
import Table from '../components/Table';
import ReactTooltip from 'react-tooltip';
import DisplayDocModal from '../components/DisplayDocModal';
import { setDataFiltered } from '../../editions/editionStore';

export default ({ messages, language }) => {
  const isMounted = useRef(false);
  const sourceList = documentStateSelector('sourceList');
  const dataFiltered = documentStateSelector('filteredSourceList');
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
      {sourceList && (
        <>
          <DisplayDocModal />
          <h3 className="text-center">
            {messages?.[language]['authorial_source']} (
            {dataFiltered?.length ?? sourceList?.length})
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
          <br />
          <Table
            data={sourceList}
            setDataFiltered={setDataFiltered}
            dataFiltered={dataFiltered ?? sourceList}
            headers={messages?.[language]['source_table_headers']}
            classes="table table-hover table-striped table-bordered"
            messages={messages[language]}
          />
        </>
      )}
    </>
  );
};
