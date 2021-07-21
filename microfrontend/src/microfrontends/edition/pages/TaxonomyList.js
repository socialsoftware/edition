import React, { useEffect, useState, useRef } from 'react'
import { Link } from 'react-router-dom';
import CircleLoader from "react-spinners/RotateLoader";
import { getTaxonomyList } from '../../../util/API/EditionAPI';
import lupaIcon from '../../../resources/assets/lupa.svg'

const TaxonomyList = (props) => {

    const [taxonomyData, setTaxonomyData] = useState(null)
    const [loading, setLoading] = useState(true)
    const [search, setSearch] = useState("")
    const inputEl = useRef(null)

    useEffect(() => {
        getTaxonomyList(props.acronym)
            .then(res => {
                console.log(res);
                setTaxonomyData(res.data)
                setLoading(false)
            })
    }, [props.acronym])

    const mapSortedInters = (category) => {
        return category.sortedIntersList.map((inter, i) => {
            return (
                <p className="edition-inter-list-p" key={i}>
                    <Link className="table-body-title"
                        to={`/fragments/fragment/${inter.xmlId}/inter/${inter.urlId}`}> {inter.title}</Link>
                </p>
            )
        })
    }

    const mapSortedUser = (category) => {
        return category.sortedUsersList.map((user, i) => {
            return (
                <p className="edition-inter-list-p" key={i}>
                    <Link className="table-body-title"
                        to={`/edition/user/${user.userName}`}>{user.firstName} {user.lastName} ({user.userName})</Link>
                </p>
                 
            )
        })
    }

    const mapSortedEditions = (category) => {
        return category.sortedEditionsList.map((edition, i) => {
            return (
                <p className="edition-inter-list-p" key={i}>
                    <Link className="table-body-title"
                        to={`/edition/acronym/${taxonomyData.acronym}`}> {edition.title}</Link>
                </p>
                 
            )
        })
    }
    const mapEditionToTable = () => {
        return taxonomyData.categorySet.map((category,i) => {
            if(search==="" || handleSearchFiltering(category)){
                return(
                    <tr className="table-body-row-row" key={i}>
                        <td className="table-body-row">
                            <Link className="table-body-title"
                            to={`/edition/acronym/${taxonomyData.acronym}/category/${category.urlId}`}>{category.name}</Link></td>

                        <td className="table-body-row">
                            {mapSortedUser(category)}
                        </td>   
                        <td className="table-body-row">
                            {mapSortedEditions(category)}
                        </td>   
                        <td className="table-body-row">
                            {mapSortedInters(category)}
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
                if(el["title"].toLowerCase().includes(search.toLowerCase())) return true
            }
        }

        return false
    }

    const handleSearchFiltering = (interp) => {
        if(interp.name?interp.name.toLowerCase().includes(search.toLowerCase()):false) return true
        else if(interp.sortedUsersList?getSearchInList(interp.sortedUsersList, 1):false) return true
        else if(interp.sortedIntersList?getSearchInList(interp.sortedIntersList, 2):false) return true
        else if(interp.sortedEditionsList?getSearchInList(interp.sortedEditionsList, 2):false) return true
        else return false
    }

    const handleSearchUpdate = () => {
        console.log(inputEl.current.value)
        setSearch(inputEl.current.value)
    }
    
    return (
        <div>
            <p className="edition-list-title">{props.messages.general_taxonomy}: {taxonomyData?taxonomyData.title:null}</p>
            <CircleLoader loading={loading}></CircleLoader>
            <div className={loading?"loading-table":"edition-editionTop"} >
                <p style={{marginTop:"15px"}}><strong>{props.messages.virtualedition}:</strong> <Link className="table-body-title" style={{color:"#337ab7"}}
                            to={`/edition/acronym/${taxonomyData?taxonomyData.acronym:null}`}>{taxonomyData?taxonomyData.title:null}</Link> </p>

                <p style={{marginTop:"10px"}}><strong>{taxonomyData?taxonomyData.categorySetSize:null} {props.messages.general_categories}:</strong></p>
            </div>
            <div style={{marginTop:"40px"}} className={loading?"loading-table":"search-container"}>
                    <input ref={inputEl} className="search" placeholder="Search"></input>
                    <img src={lupaIcon} alt="lupa" className="search-icon" onClick={() => handleSearchUpdate()}></img>
            </div>
            <div style={{marginTop:"10px"}} className={loading?"loading-table":"table-div"}>
            <table className={loading?"loading-table":"table"} data-pagination="false">
                <thead>
                    <tr>
                        <th>{props.messages.general_category}</th>
                        <th>Users</th>
                        <th>Editions</th>
                        <th>{props.messages.interpretations}</th>
                    </tr>
                </thead>
                <tbody>
                    {taxonomyData?mapEditionToTable():null}
                </tbody>
            </table>
        </div>
        </div>
    )
}

export default TaxonomyList