import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom';
import CircleLoader from "react-spinners/RotateLoader";
import { getCategoryList } from '../../../util/API/EditionAPI';
import '../../../resources/css/common/Table.css'
import '../../../resources/css/common/SearchInput.css'
import { useTable, useGlobalFilter, useAsyncDebounce } from 'react-table'
import he from 'he'

const CategoryList = (props) => {

    const [categoryData, setCategoryData] = useState(null)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        getCategoryList(props.acronym, props.category)
            .then(res => {
                console.log(res.data);
                setCategoryData(res.data)
                setLoading(false)
            })
    }, [props.acronym, props.category])


    const mapUsed = (val) => {
        return val.map((el, i) => {
            return <p key={i}>{"->"}<Link className="edition-usedLink" to={`/fragments/fragment/${el.xmlId}/inter/${el.urlId}`}>{el.shortName}</Link></p>
        })
    }

    const mapUsers = (val) => {
        return val.map((el, i) => {
            return <p className="edition-inter-list-p" key={i}><Link className="edition-usedLink" to={`/edition/user/${el.userName}`}>{el.firstName} {el.lastName} ({el.userName})</Link></p>
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
                to={`/fragments/fragment/${cellInfo.row.original.xmlId}/category/${cellInfo.row.original.urlId}`}>{cellInfo.row.original.title}</Link>
            }
        },
        {
            Header: `${props.messages.virtualedition}`,
            id: "editionTitle",
            accessor: "editionTitle",
            Cell: cellInfo => {
                return <Link className="table-body-title"
                to={`/edition/acronym/${cellInfo.row.original.acronym}`}>{he.decode(cellInfo.row.original.editionTitle)}</Link>           
            }
        },
        {
            Header: `${props.messages.user_user}`,
            id: "acronym",
            accessor: "acronym",
            Cell: cellInfo => {
                return mapUsers(cellInfo.row.original.userDtoList)                
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
    
    
    return (
        <div>
            <p className="edition-list-title">{props.messages.general_category}: {categoryData?categoryData.name:null}</p>
            <div className={loading?"loading-table":"edition-editionTop"} >
                <p style={{marginTop:"15px"}}><strong>{props.messages.general_taxonomy}:</strong> <Link className="table-body-title" style={{color:"#337ab7"}}
                            to={`/edition/acronym/${categoryData?categoryData.acronym:null}/taxonomy`}>{categoryData?he.decode(categoryData.title):null}</Link> </p>

                <p style={{marginTop:"10px"}}><strong>{categoryData?categoryData.size:null} {props.messages.fragments}:</strong></p>
            </div>

            {
                loading?
                <CircleLoader loading={true}></CircleLoader>
                :
                <Table columns={tableColumns} data={categoryData.sortedIntersList} />

            }
        </div>
    )
}

export default CategoryList