import { useEffect, useState, useMemo } from 'react';

const filterTable = (data, searchString) =>
data?.filter((item) =>
  Object.values(item).some((value) =>
    value?.toLowerCase().includes(searchString?.toLowerCase().trim())
  )
);

export default ({ data, setDataFiltered }) => {
  const [searchString, setSearchString] = useState();
  const result = useMemo(() => filterTable(data, searchString), [searchString]);
  useEffect(() => searchString?.length > 1 && setDataFiltered(result), [result?.length]);
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
