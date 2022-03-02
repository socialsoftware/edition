import { useState, useEffect, useRef, useMemo, useCallback } from 'react';
import { getSourceList } from '../api/documents';
import { documentsStore, setSourceList } from '../documentsStore';
import Search from '../components/Search';
import Table from '../components/Table';
import ReactTooltip from 'react-tooltip';
import DisplayDocModal from '../components/DisplayDocModal';
import { useNavigate } from 'react-router-dom';
import { getLanguage } from '../../../store';

export default ({ messages }) => {
  const mounted = useRef(false);
  const navigate = useNavigate();
  const { sourceList } = documentsStore();
  const [sourceListFiltered, setDataFiltered] = useState();
  const setSourceListFiltered = useCallback((data) => setDataFiltered(data));
  const [showModal, setShowModal] = useState();

  const fetchData = () =>
    getSourceList()
      .then(({ data }) => setSourceList(data, messages))
      .catch((err) => {
        console.error(err);
        setSourceList();
      });

  useEffect(() => {
    !sourceList && fetchData()
    return () => mounted.current = false
  }, []);
  useEffect(() => {
    console.log(mounted.current);
    mounted.current && fetchData();
    mounted.current = true
  }, [getLanguage()]);

  const displayDocument = (fileName) => {
    setShowModal(fileName);
  };

  const goToFragment = (path) => {
    console.log(path);
    navigate(`/fragments/fragment/${path}`);
  };

  return (
    <>
      <DisplayDocModal showModal={showModal} setShowModal={setShowModal} />
      <h3 className="text-center">
        {messages?.['authorial_source']} (
        {sourceListFiltered?.length ?? sourceList?.length})
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
          <Search />
        </div>
        <div className="fixed-table-container" style={{ marginBottom: '20px' }}>
          <Table
            data={sourceList}
            headers={messages?.['source_table_headers']}
            classes="table table-hover table-striped table-bordered"
          />
        </div>
      </div>
    </>
  );
};
