import React, { useRef, useState } from 'react'
import { Link } from 'react-router-dom';
import lupaIcon from '../../../resources/assets/lupa.svg'

const SimpleResultTable = (props) => {
    const [search, setSearch] = useState("");
    const inputEl = useRef(null)
    
    const mapFragmentsToTable = () => {
        if(props.data){
            return props.data.listFragments.map((val, i) => {
                if(search==="" || handleSearchFiltering(val)){
                    return (
                        <tr key={i}>
                            <td className="table-body-row">
                            <Link className="table-body-title" to={`/fragments/fragment/${val.fragment_xmlId}`}> {val.fragment_title}
                            </Link></td>
                            {val.simpleName==="ExpertEditionInter"&&val.type==="MANUSCRIPT"?
                                <td className="table-body-row"><Link className="table-body-title" to={`/fragments/fragment/${val.xmlId}/inter/${val.urlId}`}>{val.shortName}</Link></td>
                            :val.simpleName==="ExpertEditionInter"?
                                <td className="table-body-row"><Link className="table-body-title" to={`/fragments/fragment/${val.xmlId}/inter/${val.urlId}`}>{val.title} ({val.editor})</Link></td>
                            :
                                <td className="table-body-row"><Link className="table-body-title" to={`/fragments/fragment/${val.xmlId}/inter/${val.urlId}`}>{val.title}</Link></td>
                            }
                            
                        </tr>
                    )
                }    
                else return null
            })
        }
        
    }

    const handleSearchFiltering = (val) => {
        if(val.fragment_title?val.fragment_title.toLowerCase().includes(search.toLowerCase()):false) return true
        else if(val.simpleName==="ExpertEditionInter"&&val.type==="MANUSCRIPT") {
            if(val.shortName?val.shortName.toLowerCase().includes(search.toLowerCase()):false) return true
        }
        else if(val.simpleName==="ExpertEditionInter") {
            if(val.title?val.title.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(val.editor?val.editor.toLowerCase().includes(search.toLowerCase()):false) return true
        }
        else if(val.title?val.title.toLowerCase().includes(search.toLowerCase()):false) return true
        else return false
    }

    const handleSearchUpdate = () => {
        setSearch(inputEl.current.value)
    }

    return(
        <div className="result">
            <div className={"search-container"}>
                <input ref={inputEl} className="search" placeholder="Search"></input>
                <img src={lupaIcon} alt="lupa" className="search-icon" onClick={() => handleSearchUpdate()}></img>
            </div>
            <div className="table-div">
                <table className="table" data-pagination="false">
                    <thead>
                        <tr>
                            <th>{props.messages.fragment} ({props.data?props.data.fragCount:0})</th>
                            <th>{props.messages.interpretations} ({props.data?props.data.interCount:0})</th>
                        </tr>
                    </thead>
                    <tbody>
                        {mapFragmentsToTable()}
                    </tbody>
                </table>
            </div>
        </div>
        
    )
    
}

export default SimpleResultTable