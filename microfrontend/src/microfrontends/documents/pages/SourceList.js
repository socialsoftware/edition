import React, {useEffect, useState} from 'react'
import CircleLoader from "react-spinners/RotateLoader";
import informationIcon from '../../../resources/assets/information.svg'
import { getSourceList } from '../../../util/API/DocumentsAPI';
import { Link, useHistory } from 'react-router-dom';
import { useTable, useGlobalFilter, useAsyncDebounce } from 'react-table'


const SourceList = (props) => {

    const history = useHistory()
    const [sourceList, setSourceList] = useState([])
    const [loading, setLoading] = useState(true)
    const [info, setInfo] = useState(false)
    
    useEffect(() => {
        var mounted = true 
        getSourceList()
            .then(res => { 
                if(mounted){
                    setSourceList(res.data)
                    setLoading(false)
                }
                
            })
            .catch(err => {
                console.log(err);
            })
        return function cleanup() {
            mounted = false
            }
    }, [])

    const getInterSetMap = (val) => {
        return val.map((el, i) => {
            return (
                <p key={i}>
                    <Link className="documents-link" to={`/fragments/fragment/${el.xmlId}/inter/${el.urlId}`}>
                        {el.title}
                    </Link>
                </p>
            )
        })
    }
    const getHandNoteMap = (val) => {
        return val.map((nota, i) => {
            return (
                <p style={{textAlign:"center"}} key={i}>
                    {props.messages.general_manuscript}
                    <span style={{textTransform:"uppercase"}}>({nota.desc?nota.desc:null})</span>
                </p>
            )
        })
    }

    const getTypeNoteMap = (val) => {
        return val.map((nota, i) => {
            return (
                <p style={{textAlign:"center"}} key={i}>
                    {props.messages.general_typescript}
                    <span style={{textTransform:"uppercase"}}>({nota.desc?nota.desc:null})</span>
                </p>
            )
        })
    }

    const getDimensionsMap = (val) => {
        return val.map((elem,key) => {
            return (
                <p style={{textAlign:"center", maxWidth: "100px", minWidth:"100px"}} key={key}>{elem.height}cm X {elem.width}cm</p>
            )
            
        })
    }

    const getSurfacesMap = (val, title) => {
        if(val!==null){
            return val.map((elem,key) => {
                return <p key={key}><Link key={key} className="documents-linkFac" to={`/facs/${elem.graphic}`}>({key+1}) {title}</Link></p>
            })
        }
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
                            <td key={i} className={"table-body-row"}
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
            Header: `${props.messages.header_documents}`,
            accessor: "title",
            id: "title"
        },
        {
            Header: `${props.messages.general_transcription}`,
            id: "sourceInterSet",
            accessor: "sourceInterSet",
            Cell: cellInfo => {
                return getInterSetMap(cellInfo.row.original.sourceInterSet)
            }
        },
        {
            Header: `${props.messages.general_date}`,
            accessor: "date",
            id: "date"
        },
        {
            Header: `${props.messages.general_type}`,
            id: "noteMap",
            accessor: "noteMap",
            Cell: cellInfo => {
                if(cellInfo.row.original.sourceType==="MANUSCRIPT")
                    return getHandNoteMap(cellInfo.row.original.handNoteDtoSet)
                else if(cellInfo.row.original.sourceType==="MANUSCRIPT")
                    return getTypeNoteMap(cellInfo.row.original.typeNoteSet)
                else return null
            }
        },
        {
            Header: `${props.messages.general_LdoDLabel}`,
            id: "label",
            Cell: cellInfo => {
                if(cellInfo.row.original.hadLdoDLabel) return <p>{props.messages.general_yes}</p>
                else return <p >{props.messages.general_no}</p>
            }
        },
        {
            Header: `${props.messages.general_dimensions}`,
            id: "dimensions",
            Cell: cellInfo => {
                if(cellInfo.row.original.dimensionSetSize>0) return getDimensionsMap(cellInfo.row.original.dimensionDtoList)
                else return null
            }
        },
        {
            Header: `${props.messages.general_facsimiles}`,
            id: "facsimilies",
            Cell: cellInfo => {
                if(getSurfacesMap(cellInfo.row.original.surfaceDto, cellInfo.row.original.title))
                    return getSurfacesMap(cellInfo.row.original.surfaceDto, cellInfo.row.original.title)
                else return null
            }
        },        
    ] 

    return (
        <div className="documents-sourceList">
            <p className="documents-source-title">
                {props.messages.authorial_source}
                <span> {`(${sourceList.length})`}</span>
                <img alt="img" src={informationIcon} className="documents-glyphicon" onClick={() => setInfo(!info)}></img>
                <span className={info?"documents-showInfo":"documents-noInfo"}>
                    {props.messages.sourcelist_tt_sources}
                </span>
            </p>
            {
                loading?
                <CircleLoader loading={loading}></CircleLoader>
                :
                sourceList?
                <Table columns={tableColumns} data={sourceList} />
                :null
                
            }   
        </div>
    )
}

export default SourceList