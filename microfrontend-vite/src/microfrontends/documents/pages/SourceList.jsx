import { useState, useEffect, useLayoutEffect, useRef } from 'react';
import { getSourceList } from '../api/documents';
import {
  documentsStore,
  setSourceList,
  setFilteredSourceList,
} from '../documentsStore';
import Search from '../components/Search';
import Table from '../components/Table';
import ReactTooltip from 'react-tooltip';
import DisplayDocModal from '../components/DisplayDocModal';
import { Source } from '../models/Source';
const selector = (sel) => (state) => state[sel];

export default ({ messages, language }) => {
  const isMounted = useRef(false);
  const sourceList = documentsStore(selector('sourceList'));
  const filteredSourceList = documentsStore(selector('filteredSourceList'));

  const [showModal, setShowModal] = useState();

  const fetchData = () =>
    getSourceList()
      .then(({ data }) =>
        setSourceList(
          data.map((item) => Source(item, messages?.[language], displayDocument))
        )
      )
      .catch((err) => {
        console.error(err);
        setSourceList();
      });

  useLayoutEffect(() => {
    !sourceList && fetchData();
  }, []);

  useEffect(() => {
    console.log(language);
    isMounted.current && fetchData();
    isMounted.current = true;
  }, [language]);

  const displayDocument = (fileName) => {
    setShowModal(fileName);
  };

  return (
    <>
      <DisplayDocModal showModal={showModal} setShowModal={setShowModal} />
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
