import { useEffect } from 'react';

const getNavLabel = (index, numberOfItems, length) => {
  const start = index * numberOfItems + 1;
  let end = numberOfItems * (index + 1);
  end = end > length ? length : end;
  return `${start} - ${end}`;    
}

export default ({ index, setIndex, setNumberOfItems, numberOfItems, length }) => {
  
  const handleSelect = (e) => {
    if (e.target.value === "All")  {
      setIndex(0);
      setNumberOfItems(length);
      return
    }
    setIndex(parseInt((index * numberOfItems) / e.target.value));
    setNumberOfItems(e.target.value);
  };

  useEffect(() => {
    document.getElementById(`option-${numberOfItems}`)?.setAttribute('selected', 'selected')
  },[])

  useEffect(() => {
    setIndex(0)
  },[length])

  const changeIndex = (val) => {
    switch (val) {
      case 'FIRST':
        setIndex(0);
        break;
      case 'LAST':
        setIndex(parseInt(length / numberOfItems ));
        break;
      default:
        setIndex((index) => index + val);
        break;
    }
  };

  return (
    <div className="pagination">
      <label htmlFor="pagination" style={{ marginRight: '5px' }}>
        Show
      </label>
      <select
        name="pagination"
        className="pagination-select"
        onChange={handleSelect}
      >
        <option id="option-10" value={10} className="option-select">
          10
        </option>
        <option id="option-25" value={25} className="option-select">
          25
        </option>
        <option id="option-50" value={50} className="option-select">
          50
        </option>
        <option id="option-100" value={100} className="option-select">
          100
        </option>
        <option id="option-all" value="All" className="option-select">
          All
        </option>
      </select>

      <div className="btn-group pagination-button" role="group" aria-label="">
        <button
          type="button"
          className="btn btn-default "
          aria-label="Left Align"
          id='first'
          onClick={() => changeIndex('FIRST')}
          disabled={index === 0 ? true : false}
        >
          <span
            className="glyphicon glyphicon-backward icon-align"
            aria-hidden="true"
          ></span>
        </button>
        <button
          type="button"
          className="btn btn-default"
          onClick={() => changeIndex(-1)}
          disabled={index === 0 ? true : false}
        >
          <span
            className="glyphicon glyphicon-chevron-left icon-align"
            aria-hidden="true"
          ></span>
        </button>
        <button disabled type="button" className="btn btn-default" style={{width: "100px"}}>
          {getNavLabel(index, numberOfItems, length)}
        </button>
        <button
          type="button"
          className="btn btn-default"
          onClick={() => changeIndex(+1)}
          disabled={index >= (length / numberOfItems - 1) && true}
        >
          <span className="glyphicon glyphicon-chevron-right icon-align"></span>
        </button>
        <button
          type="button"
          className="btn btn-default"
          onClick={() => changeIndex('LAST')}
          disabled={index >= (length / numberOfItems - 1) && true}
        >
          <span className="glyphicon glyphicon-forward icon-align"></span>
        </button>
      </div>
    </div>
  );
};
