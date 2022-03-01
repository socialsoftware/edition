import { useState, useEffect } from 'react';
import { getSourceList } from '../api/documents';
import {
  setSource,
  documentsStore,
  setSourceList,
} from '../documentsStore';
import Search from '../components/Search';
import Table from '../components/Table';
import ReactTooltip from 'react-tooltip';
import DisplayDocModal from '../components/DisplayDocModal';
import { useNavigate } from 'react-router-dom';

export default ({ messages }) => {
  const {dataFiltered, sourceList, source} = documentsStore();
  const navigate = useNavigate();
  const [showModal, setShowModal] = useState();

  useEffect(() => {
    if (source) setSourceList(source, messages);
    else
      getSourceList()
        .then(({ data }) => setSource(data, messages))
        .catch((err) => {
          console.error(err);
          setSource([], messages);
        });
  }, [messages]);

  const displayDocument = (fileName) => {
    setShowModal(fileName);
  };

  const goToFragment = (path) => {
    console.log(path);
    navigate(`/fragments/fragment/${path}`);
  };

  useEffect(() => {
    document.querySelectorAll('.tb-data-surfaceString a').forEach((ele) => {
      ele.addEventListener('click', () => displayDocument(ele.id));
    });
    document.querySelectorAll('.tb-data-transcription a').forEach((ele) => {
      ele.addEventListener('click', () => goToFragment(ele.id));
    });
  }, [sourceList, dataFiltered]);

  return (
    <>
      <DisplayDocModal showModal={showModal} setShowModal={setShowModal} />
      <h3 className="text-center">
        {messages?.['authorial_source']} ({dataFiltered?.length ?? sourceList?.length})
        <ReactTooltip
          id="recom"
          type="light"
          place="bottom"
          effect="solid"
          className="info-tooltip"
          border={true}
          getContent={() => messages?.['source_info']}
        />
        <span
          data-tip=""
          data-for="recom"
          className="glyphicon glyphicon-info-sign"
        ></span>
      </h3>
      <div className="bootstrap-table">
        <div className="fixed-table-toolbar">
          <Search data={sourceList}/>
        </div>
        <div className="fixed-table-container" style={{ marginBottom: '20px' }}>
          <Table
            data={dataFiltered}
            headers={messages?.['source_table_headers']}
            classes="table table-hover table-striped table-bordered"
          />
        </div>
      </div>
    </>
  );
};
