import React, {useEffect, useState} from 'react'
import { getExpertEditionList } from '../../../util/API/EditionAPI';
import CircleLoader from "react-spinners/RotateLoader";
import { Link, useHistory } from 'react-router-dom';
import eyeIcon from '../../../resources/assets/eye.svg'
import { useTable, useGlobalFilter, useAsyncDebounce } from 'react-table'

const ExpertEditionList = (props) => {
    
    const history = useHistory()
    const [editionData, setEditionData] = useState([])
    const [editor, setEditor] = useState(null)
    const [listSize, setListSize] = useState(0)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        if(props.acronym === "JPC" || props.acronym === "JP" || props.acronym === "RZ" || props.acronym === "TSC"){
            getExpertEditionList(props.acronym)
                .then(res => {
                    setEditor(res.data.editor);
                    setEditionData(res.data.sortedInterpsList)
                    setListSize(res.data.sortedSize)
                    setTimeout(() => setLoading(false), 500)
                })
        }
    }, [props.page, props.acronym])


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
              className="edition-input-filter"
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
          <div className="edition-table-div">
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
                                onClick={() => {
                                  if(cell.column.id==="title"){
                                    // @ts-ignore
                                    history.push(`/fragments/fragment/${row.original.xmlId}/inter/${row.original.urlId}`)
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
            Header: `${props.messages.tableofcontents_number}`,
            accessor: "number",
            id: "number",
            Cell: cellInfo => {
                if(cellInfo.row.original.number!==0) return cellInfo.row.original.number
                else return null
            }
        },
        {
            Header: `${props.messages.tableofcontents_title}`,
            id: "title",
            accessor: "title",
            Cell: cellInfo => {
                return <Link className="table-body-title"
                to={`/fragments/fragment/${cellInfo.row.original.xmlId}/inter/${cellInfo.row.original.urlId}`}>{cellInfo.row.original.title}</Link>
            }
        },
        {
            Header: `${props.messages.general_reading}`,
            id: "eye",
            Cell: cellInfo => {
                return <Link className="table-body-title"
                            to={`/reading/fragment/${cellInfo.row.original.xmlId}/inter/${cellInfo.row.original.urlId}/start`}><img alt="eye" className="edition-icon" src={eyeIcon}></img></Link>
            }
        },
        {
            Header: `${props.messages.tableofcontents_volume}`,
            id: "volume",
            accessor: "volume",
            Cell: cellInfo => {
                return <p style={{textAlign:"center", minWidth:"unset", margin:"0 auto"}}>{cellInfo.row.original.volume}</p>
            }
        },
        {
            Header: `${props.messages.tableofcontents_page}`,
            accessor: "startPage",
            id: "startPage",
            Cell: cellInfo => {
                return <p style={{textAlign:"center", minWidth:"unset", margin:"0 auto"}}>{cellInfo.row.original.startPage}</p>
            }
        },
        {
            Header: `${props.messages.general_date}`,
            accessor: "date",
            id: "date",
        },
        {
            Header: `${props.messages.general_heteronym}`,
            accessor: "heteronym",
            id: "heteronym",
        },        
    ] 

    return (
        <div>
            <p className="edition-list-title">{props.messages.tableofcontents_editionof} {editor}<span> {`(${listSize})`}</span></p>

            {
                loading?
                <CircleLoader loading={loading}></CircleLoader>
                :
                editionData?
                <Table columns={tableColumns} data={editionData} />
                :null
                
            }   
        </div>
    )
}

export default ExpertEditionList