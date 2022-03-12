import { useEffect, useState } from 'react';

const filterTable = (data, searchString) =>
  data?.filter((item) =>
    item?.searchData.toLowerCase().includes(searchString?.toLowerCase().trim())
  );

export default ({ data, setDataFiltered }) => {
  const [result, setResult] = useState();
  const onSearch = (term) => setResult(filterTable(data, term));

  useEffect(() => {
    setDataFiltered(result);
  }, [result?.length]);

  return (
    <>
      <div className="pull-right search">
        <input
          className="form-control"
          type="text"
          placeholder="Search"
          onChange={(e) => onSearch(e.target.value)}
        />
      </div>
    </>
  );
};
