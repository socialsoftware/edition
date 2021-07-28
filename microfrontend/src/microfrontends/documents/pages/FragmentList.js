import React, { useEffect, useState } from 'react'
import { getFragmentList } from '../../../util/API/DocumentsAPI';
import { useHistory } from 'react-router-dom';
import InterMetaInfo from './InterMetaInfo';
import CircleLoader from "react-spinners/RotateLoader";
import { useTable, useGlobalFilter, useAsyncDebounce } from 'react-table'


const FragmentList = (props) => {

    const history = useHistory()
    const [fragmentsData, setFragmentsData] = useState([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
      var mounted = true
        getFragmentList()
            .then(res => {
              if(mounted){
                setFragmentsData(res.data)
                setLoading(false)
              }
                
            })
            .catch(err => {
                console.log((err));
            })
        return function cleanup() {
          mounted = false
        }
    }, [])

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
              className="documents-input-filter"
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
          <div className="documents-table-div">
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
                            <td key={i} className={cell.column.id==="title"?"documents-table-body-title":"table-body-row"}
                                {...cell.getCellProps()}
                                onClick={() => {
                                  if(cell.column.id==="title"){
                                    // @ts-ignore
                                    history.push(`/fragments/fragment/${row.original.fragmentXmlId}`)
                                  }
                                }}
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
            Header: `${props.messages.tableofcontents_title}`,
            accessor: "title",
            id: "title"
        },
        {
            Header: `Jacinto do Prado Coelho`,
            accessor:"JPC",
            id: "JPC",
            Cell: cellInfo => (
                <InterMetaInfo
                expertEditionMap = {cellInfo.row.original.expertEditionInterDtoMap}
                author="JPC"
                messages={props.messages}
            />
            )
        },
        {
            Header: `Teresa Sobral Cunha`,
            accessor:"TSC",
            id: "TSC",
            Cell: cellInfo => (
                <InterMetaInfo
                expertEditionMap = {cellInfo.row.original.expertEditionInterDtoMap}
                author="TSC"
                messages={props.messages}
            />
            )
        },
        {
            Header: `Richard Zenith`,
            accessor:"RZ",
            id: "RZ",
            Cell: cellInfo => (
                <InterMetaInfo
                expertEditionMap = {cellInfo.row.original.expertEditionInterDtoMap}
                author="RZ"
                messages={props.messages}
            />
            )
        },
        {
            Header: `Jerónimo Pizarro`,
            accessor:"JP",
            id: "JP",
            Cell: cellInfo => (
                <InterMetaInfo
                expertEditionMap = {cellInfo.row.original.expertEditionInterDtoMap}
                author="JP"
                messages={props.messages}
            />
            )
        },
        {
            Header: `${props.messages.authorial}`,
            accessor:"authorial",
            id: "authorial",
            Cell: cellInfo => (
                <InterMetaInfo
                sourceList={cellInfo.row.original.sourceInterDtoList[0]}
                messages={props.messages}
            />
            )
        },
        {
            Header: `${props.messages.authorial}`,
            accessor:"authorial2",
            id: "authorial2",
            Cell: cellInfo => (
                <InterMetaInfo
                sourceList={cellInfo.row.original.sourceInterDtoList[1]}
                messages={props.messages}
            />
            )
        },
        {
            Header: `${props.messages.authorial}`,
            accessor:"authorial3",
            id: "authorial3",
            Cell: cellInfo => (
                <InterMetaInfo
                sourceList={cellInfo.row.original.sourceInterDtoList[2]}
                messages={props.messages}
            />
            )
        },
        
    ]

    return (
    <div className="documents-frag-list">
        <p className="documents-list-title">{props.messages.fragment_codified} <span>{`(${fragmentsData.length})`}</span></p>
            {
                loading?
                    <CircleLoader loading={loading}></CircleLoader>
                :
                fragmentsData?
                    <Table columns={tableColumns} data={fragmentsData} />
                    :null
            }        
    </div>
    )
}


export default FragmentList