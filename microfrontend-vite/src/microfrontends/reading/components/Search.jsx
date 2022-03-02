import { useEffect, useState, useMemo } from 'react';

const filterTable = (data, searchString) =>
  data?.filter((item) =>
    item.searchData.toLowerCase().includes(searchString.toLowerCase().trim())
  );

export default ({ data, setDataFiltered }) => {
  const [searchString, setSearchString] = useState();
  const result = useMemo(() => filterTable(data, searchString), [searchString]);

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
          onChange={(e) => setSearchString(e.target.value)}
        />
      </div>
    </>
  );
};
