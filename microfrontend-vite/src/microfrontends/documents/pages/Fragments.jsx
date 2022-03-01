import { useState, useEffect } from 'react';
import { getFragmentList } from '../api/documents';
import {
  documentsStore,
  setFragments,
  setFragmentsList,
} from '../documentsStore';
import Search from '../components/Search';
import Table from '../components/Table';
import { useNavigate } from 'react-router-dom';
import DisplayDocModal from '../components/DisplayDocModal';

export default ({ messages }) => {
  const navigate = useNavigate();
  const { fragments, fragmentsList, dataFiltered } = documentsStore();
  const [showModal, setShowModal] = useState();

  useEffect(() => {
    if (fragments) setFragmentsList(fragments, messages);
    else
      getFragmentList()
        .then(({ data }) => setFragments(data, messages))
        .catch((err) => {
          console.error(err);
          setFragments([], messages);
        });
  }, [messages]);

  const goToFragment = (xmlid) => navigate(`/fragments/fragment/${xmlid}`);

  const displayDocument = (fileName) => setShowModal(fileName);

  useEffect(() => {
    document
      .querySelectorAll('.tb-data-title a')
      .forEach((ele) =>
        ele.addEventListener('click', () => goToFragment(ele.id))
      );

    [
      '.tb-data-TSC a',
      '.tb-data-JPC a',
      '.tb-data-JP a',
      '.tb-data-RZ a',
    ].forEach((expert) =>
      document.querySelectorAll(expert).forEach((ele) => {
        if (!ele.id) {
          const path = ele.href.split('/');
          const id = path[path.length - 1];
          ele.id = id;
          ele.removeAttribute('href');
        }
        ele.addEventListener('click', () => goToFragment(ele.id));
      })
    );

    [
      '.tb-data-sourceInterDtoList_0 a',
      '.tb-data-sourceInterDtoList_1 a',
      '.tb-data-sourceInterDtoList_2 a',
    ].forEach((source) =>
      document
        .querySelectorAll(source)
        .forEach((ele) =>
          ele.addEventListener('click', () => displayDocument(ele.id))
        )
    );
  }, [fragmentsList, dataFiltered]);

  return (
    <>
      <DisplayDocModal showModal={showModal} setShowModal={setShowModal} />
      <h3 className="text-center">
        {messages?.['fragment_codified']} (
        {dataFiltered?.length ?? fragmentsList?.length})
      </h3>
      <div className="bootstrap-table">
        <div className="fixed-table-toolbar">
          <Search data={fragmentsList} />
        </div>
        <div className="fixed-table-container" style={{ marginBottom: '20px' }}>
          <Table
            data={dataFiltered}
            headers={messages?.['fragments_table_headers']}
            classes="table table-hover"
          />
        </div>
      </div>
    </>
  );
};
