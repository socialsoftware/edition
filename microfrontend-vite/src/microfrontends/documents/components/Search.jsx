import { useEffect } from 'react';

export default ({ searchData, searchStringState, setDataFiltered }) => {
  const [searchString, setSearchString] = searchStringState;

  const filterFunction = (data) =>
    data?.filter((cit) =>
      Object.values(cit).some((value) =>
        value?.toLowerCase().includes(searchString.toLowerCase())
      )
    );

  useEffect(() => {
    setDataFiltered(searchString ? filterFunction(searchData) : searchData);
  }, [searchString]);

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
