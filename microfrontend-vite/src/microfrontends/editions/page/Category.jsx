import { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Table from '../../../shared/Table';
import { getCategoryFrags } from '../api/edition';
import {
  editionStoreSelector
} from '../editionStore';


export default ({ messages }) => {
  const { acronym, category } = useParams();
  const data = editionStoreSelector('data');

  useEffect(() => {
     getCategoryFrags(acronym, category);
  }, [messages, category]);

  useEffect(() => {
 console.log(data);
  }, [data]);

  return (
    <div>
      {data.tableData && (
        <>
          <h3 className="text-center">
            {messages.category}: {data.title}
          </h3>
          <br />
          <p>
            <strong>{messages.taxonomy}: </strong>
            {data.subtitle}
          </p>
          <p>
            <strong>
              {data.size} {messages.fragments}:
            </strong>
          </p>
          <br />
          <Table
          data={data.tableData}
          classes="table table-hover"
          labels={messages.categoryTableLabels}
          pagination
          search
          />
        </>
      )}
    </div>
  );
};
