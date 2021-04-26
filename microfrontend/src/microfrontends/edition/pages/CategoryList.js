import React, { useEffect, useState, useRef } from 'react'
import { Link } from 'react-router-dom';
import CircleLoader from "react-spinners/RotateLoader";
import { getCategoryList } from '../../../util/utilsAPI';
import lupaIcon from '../../../resources/assets/lupa.svg'


const CategoryList = (props) => {

    const [categoryData, setCategoryData] = useState(null)
    const [loading, setLoading] = useState(false)
    const [search, setSearch] = useState("")
    const inputEl = useRef(null)

    useEffect(() => {
        getCategoryList(props.acronym, props.category)
            .then(res => {
                console.log(res);
                setCategoryData(res.data)
                setLoading(false)
            })
    }, [props.acronym])


    const mapUsed = (val) => {
        return val.map((el, i) => {
            return <p key={i}>{"->"}<Link className="usedLink" to={`/fragments/fragment/${el.xmlId}/inter/${el.urlId}`}>{el.shortName}</Link></p>
        })
    }

    const mapUsers = (val) => {
        return val.map((el, i) => {
            return <p className="inter-list-p" key={i}><Link className="usedLink" to={`/edition/user/${el.userName}`}>{el.firstName} {el.lastName} ({el.userName})</Link></p>
        })
    }

    const mapEditionToTable = () => {
        return categoryData.sortedIntersList.map((interp,i) => {
            if(search==="" || handleSearchFiltering(interp)){
                return(
                    <tr className="table-body-row-row" key={i}>
                        <td className="table-body-row">
                            {<Link className="table-body-title"
                            to={`/fragments/fragment/${interp.xmlId}/category/${interp.urlId}`}>{interp.title}</Link>}
                        </td>

                        <td className="table-body-row">
                            {<Link className="table-body-title"
                            to={`/edition/acronym/${interp.acronym}`}>{interp.editionTitle}</Link>}
                        </td>

                        <td className="table-body-row">
                            {mapUsers(interp.userDtoList)}
                        </td>

                        <td className="table-body-row">
                            {mapUsed(interp.usedList)}
                        </td> 
                    </tr>
                    )
            }
            
        })
    }

    const getSearchInList = (arr, type) => {
        if(type === 1){
            for(let el of arr){
                if(el["firstName"].toLowerCase().includes(search.toLowerCase()) || el["lastName"].toLowerCase().includes(search.toLowerCase())
                || el["userName"].toLowerCase().includes(search.toLowerCase())) return true
            }
        }
        else if(type === 2){
            for(let el of arr){
                if(el["shortName"].toLowerCase().includes(search.toLowerCase())) return true
            }
        }
        return false
    }

    const handleSearchFiltering = (interp) => {
        if(interp.title?interp.title.toLowerCase().includes(search.toLowerCase()):false) return true
        else if(interp.editionTitle?interp.editionTitle.toString().toLowerCase().includes(search.toLowerCase()):false) return true
        else if(interp.userDtoList?getSearchInList(interp.userDtoList, 1):false) return true
        else if(interp.usedList?getSearchInList(interp.usedList, 2):false) return true
        else return false
    }

    const handleSearchUpdate = () => {
        console.log(inputEl.current.value)
        setSearch(inputEl.current.value)
    }
    
    return (
        <div>
            <p className="list-title">{props.messages.general_category}: {categoryData?categoryData.name:null}</p>
            <CircleLoader loading={loading}></CircleLoader>
            <div className={loading?"loading-table":"editionTop"} >
                <p style={{marginTop:"15px"}}><strong>{props.messages.general_taxonomy}:</strong> <Link className="table-body-title" style={{color:"#337ab7"}}
                            to={`/edition/acronym/${categoryData?categoryData.acronym:null}/taxonomy`}>{categoryData?categoryData.title:null}</Link> </p>

                <p style={{marginTop:"10px"}}><strong>{categoryData?categoryData.size:null} {props.messages.fragments}:</strong></p>
            </div>
            <div style={{marginTop:"40px"}} className={loading?"loading-table":"search-container"}>
                    <input ref={inputEl} className="search" placeholder="Search"></input>
                    <img src={lupaIcon} alt="lupa" className="search-icon" onClick={() => handleSearchUpdate()}></img>
            </div>
            <div style={{marginTop:"10px"}} className={loading?"loading-table":"table-div"}>
            <table className={loading?"loading-table":"frag-table"} data-pagination="false">
                <thead>
                    <tr>
                        <th>{props.messages.tableofcontents_title}</th>
                        <th>{props.messages.virtualedition}</th>
                        <th>{props.messages.user_user}</th>
                        <th>{props.messages.tableofcontents_usesEditions}</th>
                    </tr>
                </thead>
                <tbody>
                    {categoryData?mapEditionToTable():null}
                </tbody>
            </table>
        </div>
        </div>
    )
}

export default CategoryList