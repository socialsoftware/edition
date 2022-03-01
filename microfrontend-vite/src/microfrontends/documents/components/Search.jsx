import { useEffect, useState, useMemo } from 'react';
import { setDataFiltered } from '../documentsStore';

export default ({ data }) => {
  const [searchString, setSearchString] = useState("");

  const filterTable = () =>
  data?.filter((item) =>
    Object.values(item).some((value) =>
      value?.toLowerCase().includes(searchString?.toLowerCase().trim())
    )
  );
  const result = useMemo(() => filterTable(searchString), [searchString]);
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
