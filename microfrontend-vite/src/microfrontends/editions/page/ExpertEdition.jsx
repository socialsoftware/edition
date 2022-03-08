import { useEffect } from 'react';
import { getExpertEdition } from '../api/edition';
import Search from '../components/Search';
import Table from '../components/Table';
import {
  editionStore,
  getAcronym,
  setAcronym,
  setDataFiltered,
} from '../editionStore';
const selector = (sel) => (state) => state[sel];

export default ({ acronym, messages }) => {
  const edition = editionStore(selector('edition'));
  const dataFiltered = editionStore(selector('dataFiltered'));
  const getTitle = () =>
    `${messages['edition_of']} ${edition.editor} (${
      dataFiltered?.length ?? edition.tableData?.length
    })`;

    useEffect(() => {
      acronym !== getAcronym() && getExpertEdition(acronym);
      setAcronym(acronym);
    }, [])
 
  return (
    <div>
      {edition && (
        <>
          <h3 className="text-center">{getTitle()}</h3>
          <br />
          <div className="bootstrap-table">
            <div className="fixed-table-toolbar">
              <Search
                data={edition?.tableData}
                setDataFiltered={setDataFiltered}
                language={messages}
              />
            </div>
            <div className="fixed-table-container">
              <div className="fixed-table-toolbar"></div>
              <Table
                data={dataFiltered ?? edition?.tableData}
                headers={messages?.expertTableHeaders}
                classes="table table-hover"
                messages={messages}
              />
            </div>
          </div>
        </>
      )}
    </div>
  );
};
