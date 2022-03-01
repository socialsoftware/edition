import { useState, useEffect } from 'react';
import { getFragmentList } from '../api/documents';
import { documentsStore } from '../documentsStore';
import Search from '../components/Search';
import Table from '../components/Table';
import { useNavigate } from 'react-router-dom';
import DisplayDocModal from '../components/DisplayDocModal';

export default ({ messages }) => {
  const navigate = useNavigate();
  const { fragmentsList, setFragmentsList } = documentsStore();
  const searchStringState = useState();
  const [fragsFiltered, setFragsFiltered] = useState();
  const [showModal, setShowModal] = useState();


  const getExpertData = (data) => {
    const keyValues = {title: data.title, heteronym: data.heteronym, volume: data.volume,      number: data.number, startPage: data.startPage, notes: data.notes };
    
    const expertData = Object.entries(keyValues).reduce((prev, [key, val]) => 
      [...prev, val && `<strong>${messages?.[key]}: </strong>${val}<br />`],
      []).join('');
    
      const annexNote = data.annexNoteDtoList?.reduce((prev, curr) => 
      [...prev, `<strong>${messages?.notes}: </strong>${curr.presentationText}`],
      []).join('<br />');
      return `${expertData ?? ''}${annexNote ?? ''}`;
  };

  const getSourceData = ({altIdentifier,heteronym, form, material, columns, hadLdoDLabel, typeNoteDto, handNoteDto, surfaceString, notes, date, desc, sourceType,title, journal, issue, pubPlace, startPage, endPage }) => {

    const identifiers = surfaceString?.reduce((prev, curr) => 
      [...prev, `${curr.split('.jpg')[0]}`],
      []).join(', <br />');

    const keyValues = {
      altIdentifier: sourceType === "MANUSCRIPT" &&  identifiers ? `: ${identifiers}` : "",
      title: sourceType === "PRINTED" && title ? `: ${title}` :  "", 
      heteronym: heteronym && `: ${heteronym.slice(0,1).toUpperCase()}${heteronym.slice(1)}`,
      journal: journal && `: ${journal}`,
      issue: issue && `: ${issue}`,
      startPage: startPage !== 0 || endPage !== 0 ? `: ${startPage}${endPage ? ` - ${endPage}` : ""} `: "",
      pubPlace: pubPlace && `: ${pubPlace}`,
      form: form && `: ${messages?.[form]}`,
      material: material && `: ${messages?.[material]}`,
      columns: columns && `: ${columns}`,
      hadLdoDLabel: messages?.[hadLdoDLabel] && `: ${messages?.[hadLdoDLabel]}`,
      typeNoteDto: typeNoteDto && ` (<em>${typeNoteDto?.desc}</em>): ${typeNoteDto?.note ?? ""}`,
      handNoteDto: handNoteDto && `(<em>${handNoteDto?.desc}</em>): ${handNoteDto?.note ?? ""}`,
      notes: notes && `: ${notes}`,
      date: date && `: ${date} ${desc ? `(${desc})` : ""}`
    };
    
    const sourceData = Object.entries(keyValues).reduce((prev, [key, val]) => 
      [...prev, val && `<strong>${messages?.[key]}</strong>${val}<br />`],
      []).join('');
    
    const surfaceData = surfaceString?.reduce((prev, curr) => 
      [...prev, `<a id="${curr}">${altIdentifier}</a>`],
      []).join(', ');
      return `${sourceData ?? ''}<strong>${messages?.surfaceString}: </strong>${surfaceData}`;
  };

  const formatData = (data) =>
    data?.map(
      ({
        title,
        expertEditionInterDtoMap,
        sourceInterDtoList,
        fragmentXmlId,
      }) => ({
          title: `<a id="${fragmentXmlId}">${title}</a>`,
          JPC: getExpertData(expertEditionInterDtoMap.JPC ?? ''),
          TSC: getExpertData(expertEditionInterDtoMap.TSC ?? ''),
          RZ: getExpertData(expertEditionInterDtoMap.RZ ?? ''),
          JP: getExpertData(expertEditionInterDtoMap.JP ?? ''),
          sourceInterDtoList_0: sourceInterDtoList?.[0] && getSourceData(sourceInterDtoList?.[0]),
          sourceInterDtoList_1: sourceInterDtoList?.[1] && getSourceData(sourceInterDtoList?.[1]),
          sourceInterDtoList_2: sourceInterDtoList?.[2] && getSourceData(sourceInterDtoList?.[2])
        }));

  useEffect(() => {
    !fragmentsList &&
      getFragmentList()
        .then(({ data }) => setFragmentsList(formatData(data)))
        .catch((err) => {
          console.error(err);
          setFragmentsList([]);
        });
  }, []);

  const goToFragment = (xmlid) => {
    navigate(`/fragments/fragment/${xmlid}`);
  };

  const displayDocument = (fileName) => {
    setShowModal(fileName);
  };

  useEffect(() => {
    document.querySelectorAll('.tb-data-title a').forEach((ele) => {
      ele.addEventListener('click', () => goToFragment(ele.id));
    });

    [
      '.tb-data-TSC a',
      '.tb-data-JPC a',
      '.tb-data-JP a',
      '.tb-data-RZ a',
    ].forEach((expert) => {
      document.querySelectorAll(expert).forEach((ele) => {
        if (!ele.id) {
          const path = ele.href.split('/');
          const id = path[path.length - 1];
          ele.id = id;
          ele.removeAttribute('href');
        }
        ele.addEventListener('click', () => goToFragment(ele.id));
      });
    });

    [
      '.tb-data-sourceInterDtoList_0 a',
      '.tb-data-sourceInterDtoList_1 a',
      '.tb-data-sourceInterDtoList_1 a',
    ].forEach((source) => {
      document.querySelectorAll(source).forEach((ele) => {
        ele.addEventListener('click', () => displayDocument(ele.id));
      });
    });
  });

  return (
    <>
      <DisplayDocModal showModal={showModal} setShowModal={setShowModal} />
      <h3 className="text-center">
        {messages?.['fragment_codified']} (
        {fragsFiltered?.length ?? fragmentsList?.length})
      </h3>
      <div className="bootstrap-table">
        <div className="fixed-table-toolbar">
          <Search
            searchData={fragmentsList}
            searchStringState={searchStringState}
            setDataFiltered={setFragsFiltered}
          />
        </div>
        <div className="fixed-table-container" style={{ marginBottom: '20px' }}>
          <Table
            data={fragsFiltered ?? fragmentsList}
            headers={messages?.['fragments_table_headers']}
            classes="table table-hover"
          />
        </div>
      </div>
    </>
  );
};
