import React, { useEffect, useState } from 'react'
import { useTable, usePagination, useGlobalFilter, useAsyncDebounce } from 'react-table'
import {useHistory} from 'react-router-dom'

const ResultTable = (props) => {

  const [tableColumns, setTableColumns] = useState([])
  const [tableData, setTableData] = useState([])
  const history = useHistory()

    useEffect(() => {
      let aux = []
      aux.push(
        {
          Header: `${props.messages.fragment} (${props.data.fragCount})`,
          accessor: "title",
          id: "title"
        }
      )
      aux.push(
        {
          Header: `${props.messages.interpretations} (${props.data.interCount})`,
          accessor:"interp",
          id: "interp"
        }
      )
      let i = 1
      // @ts-ignore
      for(let el of props.data.search){
        let h = `C${i}`
        let a = `c${i}`
        aux.push({
          Header: h,
          accessor: a,
          id: "c"+i
        })
        i += 1
      }
      if(props.data.showSource){
        aux.push(
          {
            Header: `${props.messages.search_source}`,
            accessor: "type",
            id: "type"
          }
        )
      }
      if(props.data.showEdition){
        aux.push(
          {
            Header: `${props.messages.navigation_edition}`,
            accessor:"edition",
            id: "edition"
          }
        )
      }
      if(props.data.showDate){
        aux.push(
          {
            Header: `${props.messages.general_date}`,
            accessor: "ldodPrint",
            id: "ldodPrint"
            
          }
        )
      }
      if(props.data.showLdoD){
        aux.push(
          {
            Header: `${props.messages.general_LdoDLabel}`,
            accessor: "ldodLabel",
            id: "ldodLabel"
            
          }
        )
      }
      if(props.data.showSourceType){
        aux.push(
          {
            Header: `${props.messages.authorial_source}`,
            accessor: "sourceType",
            id: "sourceType"
          }
        )
      }
      if(props.data.showHeteronym){
        aux.push(
          {
            Header: `${props.messages.general_heteronym}`,
            accessor: "heteronym",
            id: "heteronym"
          }
        )
      }
      setTableColumns(aux)
    }, [props.data])

    useEffect(() => {
        let auxData = []
        console.log(props);
        for(let el of props.data.listResult){
            let rowObject = {}
            rowObject["title"] = el.fragment_title
            rowObject["xmlId"] = el.xmlId
            rowObject["urlId"] = el.urlId
            if(el.simpleName === "SourceInter" && el.sourceType === "MANUSCRIPT") rowObject["interp"] = el.shortName
            else if(el.simpleName === "ExpertEditionInter") rowObject["interp"] = `${el.title} (${el.editor})`
            else rowObject["interp"] = el.title
            let i = 0
            for(let el1 of props.data.search){
              i += 1
              for(let el2 of el.search){
                if(JSON.stringify(el1) === JSON.stringify(el2)){
                  let key = `c${i}`
                  rowObject[key] = "X"
                  
                }
              }
            }
            if(props.data.showSource){
              rowObject["type"] = el.type
            }
            if(props.data.showSourceType){
              if(el.simpleName === "SourceInter" && el.sourceType === "MANUSCRIPT" && !el.typeNoteSetEmptyBoolean) rowObject["sourceType"] = props.messages.general_typescript
              else if(el.simpleName === "SourceInter" && el.sourceType === "MANUSCRIPT" && !el.handNoteSetEmptyBoolean) rowObject["sourceType"] = props.messages.general_manuscript
            }
            if(props.data.showLdoD){
              if(el.simpleName === "SourceInter" && el.sourceType === "MANUSCRIPT"){
                if(el.hasLdoDLabel) rowObject["ldodLabel"] = props.messages.general_yes
                else rowObject["ldodLabel"] = props.messages.general_no
              }
            }
            if(props.data.showPubPlace){
              if(el.simpleName === "SourceInter" && el.sourceType === "PRINTED"){
                rowObject["pubPlace"] = el.title
              }
            }
            if(props.data.showEdition){
              if(el.simpleName === "ExpertEditionInter"){
                rowObject["edition"] = el.editor
              }
            }
            if(props.data.showHeteronym){
              if(el.simpleName === "ExpertEditionInter"){
                rowObject["heteronym"] = el.name
              }
            }
            if(props.data.showDate){
              if(el.simpleName === "SourceInter" && el.sourceType === "MANUSCRIPT"){
                rowObject["ldodPrint"] = el.ldoDDatePrint
              }
              else{
                rowObject["ldodPrint"] = el.ldoDDatePrintExpert
              }
            }
            auxData.push(rowObject)
        }
        setTableData(auxData)

    }, [props.data])

    function GlobalFilter({
      preGlobalFilteredRows,
      globalFilter,
      setGlobalFilter,
    }) {
      // @ts-ignore
      const count = preGlobalFilteredRows.length
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
        )
      
        return (

          
          <div className="search-table-search">
          <GlobalFilter
                  preGlobalFilteredRows={preGlobalFilteredRows}
                  // @ts-ignore
                  globalFilter={state.globalFilter}
                  setGlobalFilter={setGlobalFilter}
                />
           <div className="table-div" style={{marginTop:"80px"}}>
            <div className="tableWrap">
            <table {...getTableProps()}>
              <thead>
                {headerGroups.map(headerGroup => (
                  <tr {...headerGroup.getHeaderGroupProps()} className="table-row">
                    {headerGroup.headers.map(column => (
                      <th style={{textAlign:"left"}} {...column.getHeaderProps()}>{column.render('Header')}</th>
                    ))}
                  </tr>
                ))}
              </thead>
              <tbody {...getTableBodyProps()}>
                {page.map((row, 
// @ts-ignore
                i) => {
                  prepareRow(row)
                  return (
                    <tr {...row.getRowProps()} className="table-row">
                      {row.cells.map(cell => {
                        return <td className={cell.column.id==="title" || cell.column.id==="interp"?"table-d-link":"table-d"} {...cell.getCellProps()} onClick={() => {
                          console.log(cell.column.id)
                          if(cell.column.id==="title"){
                            history.push(`/fragments/fragment/${row.original.xmlId}`)
                          }
                          else if(cell.column.id==="interp"){
                            history.push(`/fragments/fragment/${row.original.xmlId}/inter/${row.original.urlId}`)
                          }
                        }}>{cell.render('Cell')}</td>
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
            </div>
          </div>
          </div>

        )
      }
    
    return (
        <div className="result-adv" style={{border:"none"}}>
          <Table columns={tableColumns} data={tableData} />
        </div>
    )
}

export default ResultTable