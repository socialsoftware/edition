import React, {useState, useRef, useEffect} from 'react'
import { Link, useLocation } from 'react-router-dom';
import CircleLoader from "react-spinners/RotateLoader";
import { getVirtualEditionList } from '../../../util/utilsAPI';
import lupaIcon from '../../../resources/assets/lupa.svg'


const VirtualEditionList = (props) => {

    const [editionData, setEditionData] = useState([])
    const [participant, setParticipant] = useState(null)
    const [listSize, setListSize] = useState(0)
    const [title, setTitle] = useState("")
    const [synopsis, setSynopsis] = useState("")
    const [acronym, setAcronym] = useState("")
    const [loading, setLoading] = useState(true)
    const [search, setSearch] = useState("")
    const inputEl = useRef(null)

    useEffect(() => {
        getVirtualEditionList(props.acronym)
            .then(res => {
                console.log(res);
                setEditionData(res.data.sortedInterpsList)
                if(props.acronym === "LdoD-Arquivo")setTitle("Edição do Arquivo LdoD")
                else setTitle(res.data.title)
                setListSize(res.data.interpsSize)
                setParticipant(res.data.participantList)
                setSynopsis(res.data.synopsis)
                setAcronym(res.data.acronym)
                setLoading(false)
            })
    }, [props.acronym])

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
        return editionData.map((interp,i) => {
            if(search==="" || handleSearchFiltering(interp)){
                return(
                    <tr className="table-body-row-row" key={i}>
                        <td className="table-body-row">
                            {interp.number>0?interp.number:null}
                        </td>
                        <td className="table-body-row">
                            <Link className="table-body-title"
                            to={`/fragments/fragment/${interp.xmlId}/inter/${interp.urlId}`}>{interp.title}</Link></td>                      
                        
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


    const getParticipanList = () => {
        return participant.map((val, i) => {
            if(i===participant.length-1){
                return (
                    <span><Link key={i} to={`/edition/user/${val.userName}`} className="participant">{val.firstName} {val.lastName}</Link></span>
                )
            }
            else{
                return (
                    <span><Link key={i} to={`/edition/user/${val.userName}`} className="participant">{val.firstName} {val.lastName}</Link>, </span>
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
        else if(interp.number?interp.number.toString().toLowerCase().includes(search.toLowerCase()):false) return true
        else if(interp.categoryList?getSearchInList(interp.categoryList, 1):false) return true
        else if(interp.usedList?getSearchInList(interp.usedList, 2):false) return true
        else return false
    }

    const handleSearchUpdate = () => {
        console.log(inputEl.current.value)
        setSearch(inputEl.current.value)
    }


    return (
        <div>
            <p className="list-title">{props.messages.virtualedition}: {title}<span> {`(${listSize})`}</span></p>
            <CircleLoader loading={loading}></CircleLoader>
            
            <div className={loading?"loading-table":"editionTop"} >
                {participant?<div><strong>{props.messages.general_editors}:</strong> <span>{getParticipanList()}</span></div>:null}
                <p style={{marginTop:"15px"}}><strong>{props.messages.virtualedition_synopsis}:</strong> {synopsis}</p>
                <p style={{marginTop:"15px"}}><strong>{props.messages.general_taxonomy}:</strong> <Link className="table-body-title" style={{color:"#337ab7"}}
                            to={`/edition/acronym/${acronym}/taxonomy/`}>{title}</Link></p>
                <p style={{marginTop:"15px"}}><strong>{listSize} {props.messages.fragments}</strong></p>
            </div>
            <div className={loading?"loading-table":"search-container"}>
                    <input ref={inputEl} className="search" placeholder="Search"></input>
                    <img src={lupaIcon} alt="lupa" className="search-icon" onClick={() => handleSearchUpdate()}></img>
            </div>
            <div style={{marginTop:"50px"}} className={loading?"loading-table":"table-div"}>
            <table className={loading?"loading-table":"table"} data-pagination="false">
                <thead>
                    <tr>
                        <th> <span className="tip">{props.messages.tableofcontents_tt_number}</span>{props.messages.tableofcontents_number}</th>
                        <th> <span className="tip">{props.messages.tableofcontents_tt_title}</span>{props.messages.tableofcontents_title}</th>
                        <th> <span className="tip">{props.messages.tableofcontents_tt_taxonomy}</span>{props.messages.general_category}</th>
                        <th> <span className="tip">{props.messages.tableofcontents_tt_usesEditions}</span>{props.messages.tableofcontents_usesEditions}</th>
                    </tr>
                </thead>
                <tbody>
                    {mapEditionToTable()}
                </tbody>
            </table>
        </div>
        </div>
    )
}

export default VirtualEditionList