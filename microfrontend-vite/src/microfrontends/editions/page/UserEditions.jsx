import { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { getUserContributions } from '../api/edition';
import Table from '../../../shared/Table';
import {
  editionStoreSelector,
} from '../editionStore';

export default ({ messages }) => {
  const { username } = useParams();
  const data = editionStoreSelector('data');

  useEffect(() => {
    getUserContributions(username);
  }, [messages]);


  return (
    <>
      {data?.tableData && (
        <>
          <h3 className="text-center">{data.title}</h3>
          <br />
          <p>
            <strong>{messages.editions}: </strong>
            {data.editions}
          </p>
          <p>
            <strong>{messages.participant}: </strong>
            {data.games}
          </p>
          <p>
            <strong>{messages.points}: </strong>
            {data.score}
          </p>
          <p>
            <strong>{messages.position}: </strong>
            {data.position}
          </p>
          <p>
            <strong>
              {data.fragments} {messages.fragments}:
            </strong>
          </p>
          <br />
          <Table 
          data={data.tableData}
          labels={messages.userTableLabels}
          classes="table table-hover"
          pagination
          search
          />
        </>
      )}
    </>
  );
};
