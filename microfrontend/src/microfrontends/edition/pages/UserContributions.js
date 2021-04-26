import React, { useState, useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';
import CircleLoader from "react-spinners/RotateLoader";
import { getUserContributions } from '../../../util/utilsAPI';
import lupaIcon from '../../../resources/assets/lupa.svg'

const UserContributions = (props) => {

    const [userData, setUserData] = useState(null)
    const [loading, setLoading] = useState(true)
    const [search, setSearch] = useState("")
    const inputEl = useRef(null)

    useEffect(() => {
        console.log(props.user);
        getUserContributions(props.user)
            .then(res => {
                console.log(res);
                setUserData(res.data)
                setLoading(false)
            })
    }, [props.user])
    

    const mapUsed = (val) => {
        return val.map((el, i) => {
            return <p key={i}>{"->"}<Link className="usedLink" to={`/fragments/fragment/${el.xmlId}/inter/${el.urlId}`}>{el.shortName}</Link></p>
        })
    }

    const mapCategoryList = (categoryList) => {
        return categoryList.map((el, i) => {
            return (
                <p key={i}><Link className="usedLink" to={`/edition/acronym/${el.acronym}/category/${el.urlId}`}>{el.name}</Link></p>
            )
        })
    }

    const mapEditionToTable = () => {
        return userData.fragInterSet.map((interp,i) => {
            if(search==="" || handleSearchFiltering(interp)){
                return(
                    <tr className="table-body-row-row" key={i}>
                        <td className="table-body-row">
                            <Link className="table-body-title"
                            to={`/fragments/fragment/${interp.xmlId}/inter/${interp.urlId}`}>{interp.title}</Link>
                        </td>      
                        <td className="table-body-row">
                            <Link className="table-body-title"
                            to={`/edition/acronym/${interp.acronym}`}>{interp.reference}</Link>
                        </td>                  
                        
                        <td className="table-body-row">
                            {mapCategoryList(interp.categoryList)}
                        </td>
                        <td className="table-body-row">
                            {mapUsed(interp.usedList)}
                        </td>
                    </tr>
                    )
            }
            
        })
    }

    const mapPublicEditionList = () => {
        return userData.publicEditionList.map((val, i) => {
            if(i===userData.publicEditionList.length-1){
                return (
                    <span><Link className="participant" key={i} to={`/edition/acronym/${val.acronym}`}>{val.title}</Link></span>
                )
            }
            else{
                return (
                    <span><Link className="participant" key={i} to={`/edition/acronym/${val.acronym}`}>{val.title}</Link>, </span>
                )
            }
        })
    }

    const getGameList = () => {
        return userData.games.map((val, i) => {
            if(i===userData.games.length-1){
                return (
                    <span><Link key={i} to={`/virtualeditions/${val.virtualExternalId}/classificationGame/${val.externalId}`} className="participant">{val.virtualTitle} - {val.interTitle}</Link></span>
                )
            }
            else{
                return (
                    <span><Link key={i} to={`/virtualeditions/${val.virtualExternalId}/classificationGame/${val.externalId}`} className="participant">{val.virtualTitle} - {val.interTitle}</Link>, </span>
                )
            }
            
        })
    }

    const getSearchInList = (arr, type) => {
        if(type === 1){
            for(let el of arr){
                if(el["name"].toLowerCase().includes(search.toLowerCase())) return true
            }
        }
        if(type === 2){
            for(let el of arr){
                if(el["shortName"].toLowerCase().includes(search.toLowerCase())) return true
            }
        }
        return false
    }

    const handleSearchFiltering = (interp) => {
        if(interp.title?interp.title.toLowerCase().includes(search.toLowerCase()):false) return true
        else if(interp.acronym?interp.acronym.toString().toLowerCase().includes(search.toLowerCase()):false) return true
        else if(interp.categoryList?getSearchInList(interp.categoryList, 1):false) return true
        else if(interp.usedList?getSearchInList(interp.usedList, 2):false) return true
        else return false
    }

    const handleSearchUpdate = () => {
        console.log(inputEl.current.value)
        setSearch(inputEl.current.value)
    }

    return(
        <div>
            <p className="list-title">{userData?userData.userFirst:null} {userData?userData.userLast:null}  ({userData?userData.username:null})</p>
            <CircleLoader loading={loading}></CircleLoader>
            
            <div className={loading?"loading-table":"editionTop"} >
                <p style={{marginTop:"15px"}}><strong>{props.messages.header_editions}:</strong> {userData?mapPublicEditionList():null}</p>
                
                {userData?userData.games?
                    <div style={{marginTop:"10px"}}>
                        <strong>{props.messages.general_participant}:</strong>
                        <span>{getGameList()}</span>
                        <p style={{marginTop:"10px"}}><strong>{props.messages.general_points}:</strong> {userData.score}</p>
                        {userData?userData.position!==-1?
                            <p style={{marginTop:"10px"}}><strong>{props.messages.general_position}:</strong> {userData.position}</p>:null:null
                        }
                    </div>:null:null}

                <p style={{marginTop:"10px"}}><strong>{userData?userData.fragInterSize:null} {props.messages.fragments}</strong></p>
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
                        <th>{props.messages.general_edition}</th>
                        <th>{props.messages.general_category}</th>
                        <th>{props.messages.tableofcontents_usesEditions}</th>
                    </tr>
                </thead>
                <tbody>
                    {userData?mapEditionToTable():null}
                </tbody>
            </table>
        </div>
        </div>
    )
}

export default UserContributions

