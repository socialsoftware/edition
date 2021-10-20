import React from 'react'
import { Link } from 'react-router-dom';
import { useTable, useGlobalFilter, useAsyncDebounce } from 'react-table'

const SimpleResultTable = (props) => {
    
    function GlobalFilter({
        preGlobalFilteredRows,
        globalFilter,
        setGlobalFilter,
      }) {
        // @ts-ignore
        const [value, setValue] = React.useState(globalFilter)
        const onChange = useAsyncDebounce(value => {
          setGlobalFilter(value || undefined)
        }, 200)
      
        return (
          <span>
            <input
              value={value || ""}
              onChange={e => {
                setValue(e.target.value);
                onChange(e.target.value);
              }}
              placeholder={`Search`}
              className="input-filter"
            />
          </span>
        )
      }

      function Table({ columns, data }) {
        const {
          getTableProps,
          getTableBodyProps,
          headerGroups,
          prepareRow,
          rows,
          // @ts-ignore
          preGlobalFilteredRows,
          // @ts-ignore
          setGlobalFilter,
          state,
        } = useTable(
          {
            columns,
            data,
          },
          useGlobalFilter,
        )
      
        return (
          <div className="search-table-search">
          <GlobalFilter
                  preGlobalFilteredRows={preGlobalFilteredRows}
                  // @ts-ignore
                  globalFilter={state.globalFilter}
                  setGlobalFilter={setGlobalFilter}
                />
           <div className="table-div" style={{marginTop:"80px", borderLeft:"1px solid #ddd"}}>
            <div className="tableWrap">
            <table className="table" {...getTableProps()} >
                <thead >
                    {headerGroups.map((headerGroup, i) => (
                    <tr key={i} {...headerGroup.getHeaderGroupProps()} className="table-row">
                        {headerGroup.headers.map((column, i) => (
                        <th key={i}
                            {...column.getHeaderProps()}                        >
                            {column.render('Header')}
                        </th>
                        ))}
                    </tr>
                    ))}
                </thead>
                <tbody {...getTableBodyProps()}>
                    {rows.map((row, i) => {
                    prepareRow(row)
                    return (
                        <tr key={i} {...row.getRowProps()} className="table-row">
                        {row.cells.map((cell, i) => {
                            return (
                            <td key={i} className={"table-body-row"}
                                {...cell.getCellProps()}
                              >
                                {cell.render('Cell')}
                            </td>
                            )
                        })}
                        </tr>
                    )
                    })}
                </tbody>
            </table>
          </div>
          </div>
          </div>
        )
      }

    const tableColumns = [
        {
            Header: `${props.messages.fragment} (${props.data?props.data.fragCount:0})`,
            accessor: "fragment_title",
            id: "fragment_title",
            Cell: cellInfo => {
                return <Link className="table-body-title" to={`/fragments/fragment/${cellInfo.row.original.fragment_xmlId}`}> {cellInfo.row.original.fragment_title}
                            </Link>
            }
        },
        {
            Header: `${props.messages.interpretations} (${props.data?props.data.interCount:0})`,
            id: "title",
            accessor: "editor",
            Cell: cellInfo => {
                if(cellInfo.row.original.simpleName==="ExpertEditionInter"&&cellInfo.row.original.type==="MANUSCRIPT")
                    return <Link className="table-body-title" to={`/fragments/fragment/${cellInfo.row.original.xmlId}/inter/${cellInfo.row.original.urlId}`}>{cellInfo.row.original.shortName}</Link>
                else if(cellInfo.row.original.simpleName==="ExpertEditionInter")
                    return <Link className="table-body-title" to={`/fragments/fragment/${cellInfo.row.original.xmlId}/inter/${cellInfo.row.original.urlId}`}>{cellInfo.row.original.title} ({cellInfo.row.original.editor})</Link>
                else
                    return <Link className="table-body-title" to={`/fragments/fragment/${cellInfo.row.original.xmlId}/inter/${cellInfo.row.original.urlId}`}>{cellInfo.row.original.title}</Link>
                
            }
        },
    ]

    return(
        <div className="result">
            {
                props.data?
                <Table columns={tableColumns} data={props.data.listFragments} />
                :null
            }  
        </div>
        
    )
    
}

export default SimpleResultTable