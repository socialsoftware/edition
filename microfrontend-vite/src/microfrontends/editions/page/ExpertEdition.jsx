import { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { getExpertEdition } from '../api/edition';
import Table from '../../../shared/Table';
import {
  editionStoreSelector,
  setLength,
} from '../editionStore';
import { isExpertEdition } from '../Models/ExpertEdition';


export default ({ messages }) => {
  const { acronym } = useParams();
  const data = editionStoreSelector('data');
  const length = editionStoreSelector('length')

  useEffect(() => {
    getExpertEdition(acronym);
  }, [messages, acronym]);

  return (
    <div>
      {data?.tableData && isExpertEdition(data) && (
        <>
          <h3 className="text-center">
            {messages['edition_of']} {data.editor} ({length})
          </h3>
          <br />
          <Table
            data={data.tableData}
            classes="table table-hover"
            labels={messages.expertTableLabels}
            setLength={setLength}
            pagination
            search
          />
        </>
      )}
    </div>
  );
};
