import React, { useEffect, useState } from 'react'
import { useHistory } from 'react-router'
import { useTable, useGlobalFilter, useAsyncDebounce } from 'react-table'
import { getTwitterCitations } from '../../../util/API/ReadingAPI'
import CircleLoader from "react-spinners/RotateLoader";

const Citations = (props) => {

    const [loading, setLoading] = useState(true)
    const [data, setData] = useState([])
    const history = useHistory()

    useEffect(() => {
        var mounted = true
        getTwitterCitations()
            .then(res => {
                if(mounted){
                  let auxArray = []
                  for(let aux of res.data){
                    let rowObject = {}
                    rowObject["date"] = aux["formatedDate"][2] + "-" + aux["formatedDate"][1] + "-" + aux["formatedDate"][0] + " " + aux["formatedDate"][3] + ":" + aux["formatedDate"][4]
                    rowObject["title"] = aux["title"]
                    rowObject["location"] = aux["location"]
                    rowObject["sourceLink"] = aux["sourceLink"]
                    rowObject["country"] = aux["country"]
                    rowObject["tweetText"] = aux["tweetText"]
                    rowObject["username"] = aux["username"]
                    rowObject["xmlId"] = aux["xmlId"]
                    rowObject["tweet"] = "Tweet"
                    auxArray.push(rowObject)
                  }
                  setData(auxArray)
                  setLoading(false)
                }
            })
        return function cleanup() {
          mounted = false
          }
    }, [])


    const tableColumns = React.useMemo(
        () => [
          {
            Header: props.messages.general_date,
            accessor: 'date',
          },
          {
            Header: props.messages.fragment,
            accessor: 'title',
          },
          {
            Header: "Tweet",
            accessor: 'tweet',
          },
          {
            Header: props.messages.general_text,
            accessor: 'tweetText',
          },
          {
            Header: props.messages.criteria_geolocation,
            accessor: 'location',
          },
          {
            Header: props.messages.general_country,
            accessor: 'country',
          },
          {
            Header: props.messages.user_role_user,
            accessor: 'username',
          },
        ],
        [props.messages.criteria_geolocation, props.messages.fragment, props.messages.general_country, props.messages.general_date, props.messages.general_text, props.messages.user_role_user]
    )
    
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
              className="reading-search"
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
          <div className="reading-table-div">
          <GlobalFilter
                  preGlobalFilteredRows={preGlobalFilteredRows}
                  // @ts-ignore
                  globalFilter={state.globalFilter}
                  setGlobalFilter={setGlobalFilter}
                />
           <div className="table-div" style={{marginTop:"80px", borderLeft:"1px solid #ddd"}}>
            <div className="tableWrap">
            <table {...getTableProps()} >
                <thead>
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
                            <td key={i} className={cell.column.id==="title"?"table-d-link":"table-d"}
                                {...cell.getCellProps()}
                                onClick={() => {
                                  if(cell.column.id==="title"){
                                    // @ts-ignore
                                    history.push(`/fragments/fragment/${row.original.xmlId}`)
                                  }
                                }}
                              >
                                {cell.column.id==="tweet"?
                                  // @ts-ignore
                                    <a className="table-d-link" rel="noreferrer" target="_blank" href={row.original.sourceLink}>{cell.render('Cell')}</a>
                                  :cell.column.id==="username"?
                                  // @ts-ignore
                                    <a className="table-d-link" rel="noreferrer" target="_blank" href={`https://twitter.com/${row.original.username}`}>{cell.render('Cell')}</a>
                                  :cell.column.id==="title"?
                                    <p className="table-d-link">{cell.render('Cell')}</p>
                                  :cell.render('Cell')
                                }
                                
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
    return (
        <div className="citations">
            <p className="citations-title">{props.messages.general_citations_twitter} ({data.length})</p>
            {
              loading?
              <CircleLoader loading={true}></CircleLoader>
              :
              <div className="result-adv">
                  <Table columns={tableColumns} data={data} />
              </div>
            }
            
        </div>
    )
}

export default Citations