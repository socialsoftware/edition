import React, { useEffect, useRef, useState } from 'react'
import { getFragmentList } from '../../../util/utilsAPI';
import '../../../resources/css/documents/Documents.css'
import { Link } from 'react-router-dom';
import InterMetaInfo from './InterMetaInfo';
import CircleLoader from "react-spinners/RotateLoader";
import lupaIcon from '../../../resources/assets/lupa.svg'
import Table from 'react-bootstrap/Table'
const FragmentList = (props) => {

    const [fragmentsData, setFragmentsData] = useState([])
    const [loading, setLoading] = useState(true)
    const [search, setSearch] = useState("");
    const inputEl = useRef(null)

    useEffect(() => {
        getFragmentList()
            .then(res => {
                console.log(res);
                setFragmentsData(res.data)
                setTimeout(() => setLoading(false), 500)
            })
            .catch(err => {
                console.log(err);
            })
    }, [])

    const mapFragmentToTable = () => {
        return fragmentsData.map((frag,i) => {
            if(search==="" || handleSearchFiltering(frag)){
                return(
                    <tr className="table-body-row-row" key={i}>
                        <td className="table-body-row"><Link className="table-body-title"
                            to={`/fragments/fragment/${frag.xmlId}`}>{frag.title}</Link>
                        </td>
                        <td className="table-body-row">
                            <InterMetaInfo
                                expertEditionMap={frag.expertEditionInterDtoMap}
                                author="JPC"
                                messages={props.messages}
                        /></td>
                        <td  className="table-body-row">
                            <InterMetaInfo
                                expertEditionMap={frag.expertEditionInterDtoMap}
                                author="TSC"
                                messages={props.messages}
                        /></td>
                        <td className="table-body-row">
                            <InterMetaInfo
                                expertEditionMap={frag.expertEditionInterDtoMap}
                                author="RZ"
                                messages={props.messages}
                        /></td>
                        <td className="table-body-row">
                            <InterMetaInfo
                                expertEditionMap={frag.expertEditionInterDtoMap}
                                author="JP"
                                messages={props.messages}
                        /></td>
                        <td className="table-body-row">
                            <InterMetaInfo
                                sourceList={frag.sourceInterDtoList[0]}
                                messages={props.messages}
                        /></td> 
                        {frag.sourceInterDtoList[1]?
                            <td className="table-body-row">
                                <InterMetaInfo
                                    sourceList={frag.sourceInterDtoList[1]}
                                    messages={props.messages}
                                />
                            </td>:<td className="table-body-row"><p>-</p></td>
                        }
                        {frag.sourceInterDtoList[2]?
                            <td className="table-body-row">
                                <InterMetaInfo
                                    sourceList={frag.sourceInterDtoList[2]}
                                    messages={props.messages}
                                />
                            </td>:<td className="table-body-row"><p>-</p></td>
                        }
                        
                    </tr>
                    )
            }
            
        })
    }

    const handleSearchFiltering = (frag) => {
        if(frag.expertEditionInterDtoMap["JPC"]){
            let exp = frag.expertEditionInterDtoMap["JPC"]
            if(exp.title?exp.title.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.date?exp.date.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.heteronymName?exp.heteronymName.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.notes?exp.notes.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.startPage?exp.startPage.toString().includes(search.toLowerCase()):false) return true
            else if(exp.endPage?exp.endPage.toString().includes(search.toLowerCase()):false) return true
            else return false
        }
        if(frag.expertEditionInterDtoMap["TSC"]){
            let exp = frag.expertEditionInterDtoMap["TSC"]
            if(exp.title?exp.title.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.date?exp.date.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.heteronymName?exp.heteronymName.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.notes?exp.notes.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.startPage?exp.startPage.toString().includes(search.toLowerCase()):false) return true
            else if(exp.endPage?exp.endPage.toString().includes(search.toLowerCase()):false) return true
            else return false
        }
        if(frag.expertEditionInterDtoMap["RZ"]){
            let exp = frag.expertEditionInterDtoMap["RZ"]
            if(exp.title?exp.title.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.date?exp.date.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.heteronymName?exp.heteronymName.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.notes?exp.notes.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.startPage?exp.startPage.toString().includes(search.toLowerCase()):false) return true
            else if(exp.endPage?exp.endPage.toString().includes(search.toLowerCase()):false) return true
            else return false
        }
        if(frag.expertEditionInterDtoMap["JP"]){
            let exp = frag.expertEditionInterDtoMap["JP"]
            if(exp.title?exp.title.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.date?exp.date.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.heteronymName?exp.heteronymName.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.notes?exp.notes.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(exp.startPage?exp.startPage.toString().includes(search.toLowerCase()):false) return true
            else if(exp.endPage?exp.endPage.toString().includes(search.toLowerCase()):false) return true
            else return false
        }
        return false
        
    }

    const handleSearchUpdate = () => {
        console.log(inputEl.current.value)
        setSearch(inputEl.current.value)
    }

    return (
    <div className="frag-list">
        <p className="list-title">{props.messages.fragment_codified} <span>{`(${fragmentsData.length})`}</span></p>
        <CircleLoader loading={loading}></CircleLoader>
        <div className={loading?"loading-table":"search-container"}>
            <input ref={inputEl} className="frag-search" placeholder="Search"></input>
            <img src={lupaIcon} alt="lupa" className="frag-search-icon" onClick={() => handleSearchUpdate()}></img>
        </div>
        
        <div className={loading?"loading-table":"table-div"}>
            <Table className={loading?"loading-table":"frag-table"} data-pagination="false">
                <thead>
                    <tr>
                        <th style={{maxWidth:"100px"}}>{props.messages.tableofcontents_title}</th>
                        <th >Jacinto do Prado Coelho</th>
                        <th >Teresa Sobral Cunha</th>
                        <th >Richard Zenith</th>
                        <th >Jerónimo Pizarro</th>
                        <th >{props.messages.authorial}</th>
                        <th >{props.messages.authorial}</th>
                        <th >{props.messages.authorial}</th>
                    </tr>
                </thead>
                <tbody>
                    {mapFragmentToTable()}
                </tbody>
            </Table>
        </div>
        
    </div>
    )
}


export default FragmentList