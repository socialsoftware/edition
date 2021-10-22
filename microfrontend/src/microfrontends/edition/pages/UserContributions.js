import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import CircleLoader from "react-spinners/RotateLoader";
import { getUserContributions } from '../../../util/API/EditionAPI';
import { useTable, useGlobalFilter, useAsyncDebounce } from 'react-table'
import he from 'he'

const UserContributions = (props) => {

    const [userData, setUserData] = useState(null)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        getUserContributions(props.user)
            .then(res => {
                setUserData(res.data)
                setLoading(false)
            })
    }, [props.user])
    

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


    const mapPublicEditionList = () => {
        return userData.publicEditionList.map((val, i) => {
            if(i===userData.publicEditionList.length-1){
                return (
                    <span key={i}><Link className="edition-participant" to={`/edition/acronym/${val.acronym}`}>{he.decode(val.title)}</Link></span>
                )
            }
            else{
                return (
                    <span key={i}><Link className="edition-participant" to={`/edition/acronym/${val.acronym}`}>{he.decode(val.title)}</Link>, </span>
                )
            }
        })
    }

    const getGameList = () => {
        return userData.games.map((val, i) => {
            if(i===userData.games.length-1){
                return (
                    <span key={i}><Link  to={`/edition/game/${val.virtualExternalId}/classificationGame/${val.externalId}`} className="edition-participant">{val.virtualTitle} - {val.interTitle}</Link></span>
                )
            }
            else{
                return (
                    <span key={i}><Link  to={`/edition/game/${val.virtualExternalId}/classificationGame/${val.externalId}`} className="edition-participant">{val.virtualTitle} - {val.interTitle}</Link>, </span>
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
            Header: `${props.messages.tableofcontents_title}`,
            accessor: "title",
            id: "title",
            Cell: cellInfo => {
                return <Link className="table-body-title"
                to={`/fragments/fragment/${cellInfo.row.original.xmlId}/inter/${cellInfo.row.original.urlId}`}>{cellInfo.row.original.title}</Link>
            }
        },
        {
            Header: `${props.messages.general_edition}`,
            id: "reference",
            accessor: "reference",
            Cell: cellInfo => {
                return <Link className="table-body-title"
                to={`/edition/acronym/${cellInfo.row.original.acronym}`}>{cellInfo.row.original.reference}</Link>
            }
        },
        {
            Header: `${props.messages.general_category}`,
            id: "acronym",
            accessor: "acronym",
            Cell: cellInfo => {
                return mapCategoryList(cellInfo.row.original.categoryList)             
            }
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

    return(
        <div>
            <p className="edition-list-title">{userData?userData.userFirst:null} {userData?userData.userLast:null}  ({userData?userData.username:null})</p>
                   
            <div className={loading?"loading-table":"edition-editionTop"} >
                <p style={{marginTop:"15px"}}><strong>{props.messages.header_editions}:</strong> {userData?mapPublicEditionList():null}</p>
                
                {userData?userData.games?
                    <div style={{marginTop:"10px"}}>
                        <strong>{props.messages.general_participant}: </strong>
                        <span>{getGameList()}</span>
                        <p style={{marginTop:"10px"}}><strong>{props.messages.general_points}:</strong> {userData.score}</p>
                        {userData?userData.position!==-1?
                            <p style={{marginTop:"10px"}}><strong>{props.messages.general_position}:</strong> {userData.position}</p>:null:null
                        }
                    </div>:null:null}

                <p style={{marginTop:"10px"}}><strong>{userData?userData.fragInterSize:null} {props.messages.fragments}</strong></p>
            </div>

            {
                loading?
                <CircleLoader loading={true}></CircleLoader>
                :
                <Table columns={tableColumns} data={userData.fragInterSet} />

            }
        </div>
    )
}

export default UserContributions

