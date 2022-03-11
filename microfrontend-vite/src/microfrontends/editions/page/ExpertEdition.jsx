import { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { getExpertEdition } from '../api/edition';
import Table from '../components/Table';
import {
  editionStoreSelector,
  getAcronym,
  setAcronym,
  setDataFiltered,
} from '../editionStore';
import { isExpertEdition } from '../Models/ExpertEdition';

const getTitle = (messages, edition, dataFiltered) =>
  `${messages['edition_of']} ${edition.editor} (${
    dataFiltered?.length ?? edition.tableData?.length
  })`;

export default ({ messages }) => {
  const { acronym } = useParams();
  const edition = editionStoreSelector('edition');
  const dataFiltered = editionStoreSelector('dataFiltered');

  useEffect(() => {
    acronym !== getAcronym() && getExpertEdition(acronym);
    setAcronym(acronym);
  }, [acronym]);


  return (
    <div>
      {edition && isExpertEdition(edition) && (
        <>
          <h3 className="text-center">
            {getTitle(messages, edition, dataFiltered)}
          </h3>
          <br />
          <Table 
          data={edition?.tableData}
          setDataFiltered={setDataFiltered}          
          dataFiltered={dataFiltered ?? edition?.tableData}
          headers={messages?.expertTableHeaders}
          classes="table table-hover"
          messages={messages}
          />
        </>
      )}
    </div>
  );
};
