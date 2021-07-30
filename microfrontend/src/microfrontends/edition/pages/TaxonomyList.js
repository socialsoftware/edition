import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';
import CircleLoader from "react-spinners/RotateLoader";
import { getTaxonomyList } from '../../../util/API/EditionAPI';
import { useTable, useGlobalFilter, useAsyncDebounce } from 'react-table'
import he from 'he'

const TaxonomyList = (props) => {

    const [taxonomyData, setTaxonomyData] = useState(null)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        getTaxonomyList(props.acronym)
            .then(res => {
                setTaxonomyData(res.data)
                setLoading(false)
            })
    }, [props.acronym])

    const mapSortedInters = (category) => {
        return category.sortedIntersShortList.map((inter, i) => {
            return (
                <p className="edition-inter-list-p" key={i}>
                    <Link className="table-body-title"
                        to={`/fragments/fragment/${inter.xmlId}/inter/${inter.urlId}`}> {inter.title}</Link>
                </p>
            )
        })
    }

    const mapSortedUser = (category) => {
        return category.sortedUserShortList.map((user, i) => {
            return (
                <p className="edition-inter-list-p" key={i}>
                    <Link className="table-body-title"
                        to={`/edition/user/${user.userName}`}>{user.firstName} {user.lastName} ({user.userName})</Link>
                </p>
                 
            )
        })
    }

    const mapSortedEditions = (category) => {
        return category.sortedTitleList.map((title, i) => {
            return (
                <p className="edition-inter-list-p" key={i}>
                    <Link className="table-body-title"
                        to={`/edition/acronym/${taxonomyData.acronym}`}> {he.decode(title)}</Link>
                </p>
                 
            )
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
            Header: `${props.messages.general_category}`,
            accessor: "name",
            id: "name",
            Cell: cellInfo => {
                return <Link className="table-body-title"
                to={`/edition/acronym/${taxonomyData.acronym}/category/${cellInfo.row.original.urlId}`}>{cellInfo.row.original.name}</Link>
            }
        },
        {
            Header: `Users`,
            id: "sortedUser",
            accessor: "sortedUser",
            Cell: cellInfo => {
                return mapSortedUser(cellInfo.row.original)                
            }
        },
        {
            Header: `Editions`,
            id: "sortedEditions",
            accessor: "sortedEditions",
            Cell: cellInfo => {
                return mapSortedEditions(cellInfo.row.original)                
            }
        },
        {
            Header: `${props.messages.interpretations}`,
            id: "sortedInters",
            accessor: "sortedInters",
            Cell: cellInfo => {
                return mapSortedInters(cellInfo.row.original)                
            }
        },
    ]
    
    return (
        <div>
            <p className="edition-list-title">{props.messages.general_taxonomy}: {taxonomyData?he.decode(taxonomyData.title):null}</p>
            <div className={loading?"loading-table":"edition-editionTop"} >
                <p style={{marginTop:"15px"}}><strong>{props.messages.virtualedition}:</strong> <Link className="table-body-title" style={{color:"#337ab7"}}
                            to={`/edition/acronym/${taxonomyData?taxonomyData.acronym:null}`}>{taxonomyData?he.decode(taxonomyData.title):null}</Link> </p>

                <p style={{marginTop:"10px"}}><strong>{taxonomyData?taxonomyData.categorySetSize:null} {props.messages.general_categories}:</strong></p>
            </div>
            {
                loading?
                <CircleLoader loading={true}></CircleLoader>
                :
                <Table columns={tableColumns} data={taxonomyData.categorySet} />

            }
        </div>
    )
}

export default TaxonomyList