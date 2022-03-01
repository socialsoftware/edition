import { useState, useEffect } from 'react';
import { getSourceList } from '../api/documents';
import { documentsStore } from '../documentsStore';
import Search from '../components/Search';
import Table from '../components/Table';
import ReactTooltip from 'react-tooltip';
import DisplayDocModal from '../components/DisplayDocModal';
import { useNavigate } from 'react-router-dom';

export default ({ messages }) => {
  const navigate = useNavigate();
  const { sourceList, setSourceList } = documentsStore();
  const searchStringState = useState();
  const [listFiltered, setListFiltered] = useState();
  const [showModal, setShowModal] = useState();

  const formatData = (data) =>
    data?.map((entry) => {
      const {
        transcription,
        dimensionDtoList,
        surfaceString,
        xmlId,
        urlId,
        typeNoteDto,
        handNoteDto,
        sourceType,
      } = entry;
      const handNote = handNoteDto?.desc;
      const typeNote = typeNoteDto?.desc;
      const type = `${messages?.[sourceType]?.[handNote] ?? ''}\n${
        messages?.[sourceType]?.[typeNote] ?? ''
      }`;
      return {
        title: entry.title,
        transcription: `<a id="${xmlId}/inter/${urlId}">${transcription}</a>`,
        date: entry.date,
        hadLdoDLabel: messages?.[entry.hadLdoDLabel],
        sourceType: type === '\n' ? `${messages?.otherType[sourceType]}` : type,
        dimensionDtoList: `${
          dimensionDtoList
            ?.reduce(
              (prev, cur) => [...prev, ` ${cur?.height}cm X ${cur.width}cm`],
              []
            )
            .join(',<br />') ?? ''
        }`,
        surfaceString: `${
          surfaceString
            ?.reduce(
              (prev, cur, index) => [
                ...prev,
                `<a id="${cur}"> (${index + 1}) ${entry.title}</a>`,
              ],
              []
            )
            .join('<br />') ?? ''
        }`,
      };
    });

  useEffect(() => {
    !sourceList &&
      getSourceList()
        .then(({ data }) => setSourceList(formatData(data)))
        .catch((err) => {
          console.error(err);
          setSourceList([]);
        });
  }, []);

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
  });

  return (
    <>
      <DisplayDocModal showModal={showModal} setShowModal={setShowModal} />
      <h3 className="text-center">
        {messages?.['authorial_source']} (
        {listFiltered?.length ?? sourceList?.length})
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
          <Search
            searchData={sourceList}
            searchStringState={searchStringState}
            setDataFiltered={setListFiltered}
          />
        </div>
        <div className="fixed-table-container" style={{ marginBottom: '20px' }}>
          <Table
            data={listFiltered ?? sourceList}
            headers={messages?.['source_table_headers']}
            classes="table table-hover table-striped table-bordered"
          />
        </div>
      </div>
    </>
  );
};
