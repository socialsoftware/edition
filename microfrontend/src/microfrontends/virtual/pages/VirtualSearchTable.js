import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useTable, usePagination, useGlobalFilter, useAsyncDebounce, useRowSelect } from 'react-table'

const VirtualSearchTable = (props) => {

    const [tableData, setTableData] = useState(null)

    useEffect(() => {
        if(props.tableData){
            const aux = []
            for(let el of props.tableData.listFragments){
                let rowObject = {}
                rowObject["title"] = el.fragment_title
                if(el.type === "EDITORIAL"){
                    rowObject["interp"] = `${el.fragment_title} (${el.editor})`
                }
                else{
                    rowObject["interp"] = el.shortName
                }
                rowObject["xmlId"] = el.xmlId
                rowObject["urlId"] = el.urlId
                rowObject["externalId"] = el.externalId
                rowObject["fragmentXmlId"] = el.fragment_xmlId
                aux.push(rowObject)
            }
            setTableData(aux)
        }
    },[props])


    const tableColumns = [
        {
            Header: `${props.messages.fragment} (${props.tableData?props.tableData.fragCount:null})`,
            accessor: "title",
            id: "title"
        },
        {
            Header: `${props.messages.interpretations} (${props.tableData?props.tableData.interCount:null})`,
            accessor:"interp",
            id: "interp"
        }
    ]

    function GlobalFilter({
        preGlobalFilteredRows,
        globalFilter,
        setGlobalFilter,
      }) {
        // @ts-ignore
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
              className="virtual-input-input-table"
            />
          </span>
        )
      }

      const IndeterminateCheckbox = React.forwardRef(
        // @ts-ignore
        ({ indeterminate, ...rest }, ref) => {
          const defaultRef = React.useRef()
          const resolvedRef = ref || defaultRef
      
          React.useEffect(() => {
            // @ts-ignore
            resolvedRef.current.indeterminate = indeterminate
          }, [resolvedRef, indeterminate])
      
          return (
            <>
              <input type="checkbox" ref={resolvedRef} {...rest} />
            </>
          )
        }
      )

      const handleAddition = (selected) => {
        props.addInterpsCallback(selected)
      }
  
      function Table({ columns, data }) {
          const {
            getTableProps,
            getTableBodyProps,
            headerGroups,
            prepareRow,
            // @ts-ignore
            page,
            // @ts-ignore
            canPreviousPage,
            // @ts-ignore
            canNextPage,
            // @ts-ignore
            pageOptions,
            // @ts-ignore
            pageCount,
            // @ts-ignore
            gotoPage,
            // @ts-ignore
            nextPage,
            // @ts-ignore
            previousPage,
            // @ts-ignore
            setPageSize,
            // @ts-ignore
            preGlobalFilteredRows,
            // @ts-ignore
            setGlobalFilter,
            // @ts-ignore
            selectedFlatRows,
            state,
            // @ts-ignore
            state: { pageIndex, pageSize },
          } = useTable(
            {
              columns,
              data,
              // @ts-ignore
              initialState: { pageIndex: 0 },
            },
            useGlobalFilter,
            usePagination,
            useRowSelect,
            hooks => {
              hooks.visibleColumns.push(columns => [
                // Let's make a column for selection
                {
                  id: 'selection',
                  // The cell can use the individual row's getToggleRowSelectedProps method
                  // to the render a checkbox
                  Cell: ({ row }) => (
                    <div>
                      <IndeterminateCheckbox
                      // eslint-disable-next-line dot-location
                      {...row.
// @ts-ignore
                      getToggleRowSelectedProps()} />
                    </div>
                  ),
                },
                ...columns,
              ])
            }
          )
        
          return (
  
            
            <div className="virtual-search-table-search">
            <GlobalFilter
                    preGlobalFilteredRows={preGlobalFilteredRows}
                    // @ts-ignore
                    globalFilter={state.globalFilter}
                    setGlobalFilter={setGlobalFilter}
                  />
             <div className="virtual-search-table-div" style={{marginTop:"80px"}}>
              <div>
              <table className="virtual-editions-table" {...getTableProps()}>
                <thead>
                  {headerGroups.map(headerGroup => (
                    <tr {...headerGroup.getHeaderGroupProps()} className="table-row">
                      {headerGroup.headers.map((column, i) => (
                        <th key={i} style={{textAlign:"left"}} {...column.getHeaderProps()}>{column.render('Header')}</th>
                      ))}
                    </tr>
                  ))}
                </thead>
                <tbody {...getTableBodyProps()}>
                  {page.map((row, 
  // @ts-ignore
                  // @ts-ignore
                  i) => {
                    prepareRow(row)
                    return (
                      <tr key={i} {...row.getRowProps()} >
                        {row.cells.map((cell, i) => {
                          return <td key={i} style={{textAlign:"left"}} {...cell.getCellProps()}>
                            {
                              cell.column.id==="title" || cell.column.id==="interp"?
                              <Link className="virtual-link" style={{color: "#0C4EF6"}}
                              to={cell.column.id==="title"?`/fragments/fragment/${row.original.xmlId}`:`/fragments/fragment/${row.original.xmlId}/inter/${row.original.urlId}`}>{cell.render('Cell')}</Link>
                              :cell.render('Cell')
                            }
                            </td>
                        })}
                      </tr>
                    )
                  })}
                </tbody>
              </table>
              <div className="pagination">
                <button onClick={() => gotoPage(0)} disabled={!canPreviousPage}>
                  {'<<'}
                </button>{' '}
                <button onClick={() => previousPage()} disabled={!canPreviousPage}>
                  {'<'}
                </button>{' '}
                <button onClick={() => nextPage()} disabled={!canNextPage}>
                  {'>'}
                </button>{' '}
                <button onClick={() => gotoPage(pageCount - 1)} disabled={!canNextPage}>
                  {'>>'}
                </button>{' '}
                <span>
                  Página{' '}
                  <strong>
                    {pageIndex + 1} de {pageOptions.length}
                  </strong>{' '}
                </span>
                <span>
                  | Ir para a página:{' '}
                  <input
                    type="number"
                    defaultValue={pageIndex + 1}
                    onChange={e => {
                      const page = e.target.value ? Number(e.target.value) - 1 : 0
                      gotoPage(page)
                    }}
                    style={{ width: '100px' }}
                  />
                </span>{' '}
                <select
                  value={pageSize}
                  onChange={e => {
                    setPageSize(Number(e.target.value))
                  }}
                >
                  {[10, 20, 30, 40, 50].map(pageSize => (
                    <option key={pageSize} value={pageSize}>
                      Mostrar {pageSize}
                    </option>
                  ))}
                </select>
              </div>
              <div style={{borderTop:"1px solid #ddd", height:"60px", position:"relative", marginTop:"10px"}}>
                <span className="virtual-create-add-button" onClick={() => handleAddition(selectedFlatRows)}>
                    <p>{props.messages.general_add}</p>
                </span>
            </div>
              </div>
            </div>
            </div>
  
          )
        }

    return (
        <div className="virtual-search-result" style={{border:"none"}}>
            {tableData?
                <Table columns={tableColumns} data={tableData} />
                :null
            }
            

        </div>
    )
}

export default VirtualSearchTable