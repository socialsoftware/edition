import React, {useState, useEffect} from 'react'
import { Link } from 'react-router-dom';
import CircleLoader from "react-spinners/RotateLoader";
import { getVirtualEditionList } from '../../../util/API/EditionAPI';
import { useTable, useGlobalFilter, useAsyncDebounce } from 'react-table'
import he from 'he'

const VirtualEditionList = (props) => {

    const [editionData, setEditionData] = useState([])
    const [participant, setParticipant] = useState(null)
    const [listSize, setListSize] = useState(0)
    const [title, setTitle] = useState("")
    const [acronym, setAcronym] = useState(null)
    const [synopsis, setSynopsis] = useState("")
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        if(props.acronym !== "JPC" && props.acronym !== "JP" && props.acronym !== "RZ" && props.acronym !== "TSC"){
            getVirtualEditionList(props.acronym)
                .then(res => {
                    setEditionData(res.data.sortedInterpsList)
                    setListSize(res.data.interpsSize)
                    setParticipant(res.data.participantList)
                    setSynopsis(res.data.synopsis?he.decode(res.data.synopsis):res.data.synopsis)
                    setTitle(res.data.title?he.decode(res.data.title):res.data.title)
                    setAcronym(res.data.acronym)
                    setLoading(false)
                })
        }
        
    }, [props.page, props.acronym])

    const mapUsed = (val) => {
        return val.map((el, i) => {
            return <p key={i}>{"->"}<Link className="edition-usedLink" to={`/fragments/fragment/${el.xmlId}/inter/${el.urlId}`}>{el.shortName}</Link></p>
        })
    }

    const mapCategoryList = (categoryList) => {
        return categoryList.map((el, i) => {
            return (
                <p key={i}><Link className="edition-usedLink" to={`/edition/acronym/${el.acronym}/category/${el.urlId}`}>{el.name}</Link></p>
            )
        })
    }


    const getParticipanList = () => {
        return participant.map((val, i) => {
            if(i===participant.length-1){
                return (
                    <span key={i}><Link  to={`/edition/user/${val.userName}`} className="edition-participant">{val.firstName} {val.lastName}</Link></span>
                )
            }
            else{
                return (
                    <span key={i}><Link key={i} to={`/edition/user/${val.userName}`} className="edition-participant">{val.firstName} {val.lastName}</Link>, </span>
                )
            }
            
        })
    }


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
            Header: `${props.messages.general_category}`,
            id: "categoryList",
            accessor: "categoryList",
            Cell: cellInfo => {
                return mapCategoryList(cellInfo.row.original.categoryList)}
            },
        {
            Header: `${props.messages.tableofcontents_usesEditions}`,
            id: "usedList",
            accessor: "usedList",
            Cell: cellInfo => {
                return mapUsed(cellInfo.row.original.usedList)
            }
        },
    ]

    return (
        <div>
            <p className="edition-list-title">{props.messages.virtualedition}: {title}</p>

             
            
            <div className={loading?"loading-table":"edition-editionTop"} >
                {participant?<div style={{marginBottom:"15px"}}><strong>{props.messages.general_editors}:</strong> <span>{getParticipanList()}</span></div>:null}
                {synopsis?synopsis.length>0?<span><strong>{props.messages.virtualedition_synopsis}:</strong> <span style={{lineHeight:"1.42857143"}}>{synopsis}</span></span>:null:null}
                {acronym?<p style={{marginTop:"15px"}}><strong>{props.messages.general_taxonomy}:</strong> <Link className="table-body-title" style={{color:"#337ab7"}}
                            to={`/edition/acronym/${acronym}/taxonomy/`}>{title}</Link></p>:null}
                <p style={{marginTop:"15px"}}><strong>{listSize} {props.messages.fragments}</strong></p>
            </div>

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

export default VirtualEditionList