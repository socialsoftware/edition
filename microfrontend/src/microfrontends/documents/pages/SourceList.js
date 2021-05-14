import React, {useEffect, useRef, useState} from 'react'
import CircleLoader from "react-spinners/RotateLoader";
import informationIcon from '../../../resources/assets/information.svg'
import { getSourceList } from '../../../util/utilsAPI';
import { Link } from 'react-router-dom';
import lupaIcon from '../../../resources/assets/lupa.svg'


const SourceList = (props) => {

    const [sourceList, setSourceList] = useState([])
    const [loading, setLoading] = useState(true)
    const [info, setInfo] = useState(false)
    const [search, setSearch] = useState("");
    const inputEl = useRef(null)
    
    useEffect(() => {
        getSourceList()
            .then(res => {
                console.log(res);
                setSourceList(res.data)
                setTimeout(() => setLoading(false), 500)
            })
            .catch(err => {
                console.log(err);
            })
    }, [])

    const getInterSetMap = (val) => {
        return val.map((el, i) => {
            return (
                <p key={i}>
                    <Link className="link" to={`/fragments/fragment/${el.xmlId}/inter/${el.urlId}`}>
                        {el.title}
                    </Link>
                </p>
            )
        })
    }
    const getHandNoteMap = (val) => {
        return val.map((nota, i) => {
            return (
                <p style={{textAlign:"center"}} key={i}>
                    {props.messages.general_manuscript}
                    <span style={{textTransform:"uppercase"}}>({nota.desc?nota.desc:null})</span>
                </p>
            )
        })
    }

    const getTypeNoteMap = (val) => {
        return val.map((nota, i) => {
            return (
                <p style={{textAlign:"center"}} key={i}>
                    {props.messages.general_typescript}
                    <span style={{textTransform:"uppercase"}}>({nota.desc?nota.desc:null})</span>
                </p>
            )
        })
    }

    const getDimensionsMap = (val) => {
        return val.map((elem,key) => {
            return (
                <p style={{textAlign:"center", maxWidth: "100px", minWidth:"100px"}} key={key}>{elem.height}cm X {elem.width}cm</p>
            )
            
        })
    }

    const getSurfacesMap = (val, title) => {
        if(val!==null){
            return val.map((elem,key) => {
                return <p><Link key={key} className="linkFac" to={`/facs/${elem.graphic}`}>({key+1}) {title}</Link></p>
            })
        }
    }


    const masSourceToTable = () => {
        return sourceList.map((frag,i) => {
            if(search==="" || handleSearchFiltering(frag)){
                return(
                    <tr className="table-body-row-row" key={i}>
                        <td className="table-body-row">
                            {frag.title}
                        </td>
                        <td className="table-body-row">
                            {getInterSetMap(frag.sourceInterSet)}
                        </td>
                        <td  className="table-body-row-source">
                           {frag.date}
                        </td>
                        <td className="table-body-row">
                            {frag.sourceType==="MANUSCRIPT"?
                                getHandNoteMap(frag.handNoteDtoSet):null
                            }
                            {frag.sourceType==="MANUSCRIPT"?
                                getTypeNoteMap(frag.typeNoteSet):null
                            }

                        </td>
                        <td className="table-body-row-source" style={{maxWidth: "80px", minWidth:"80px"}}>
                            {frag.hadLdoDLabel?<p style={{textAlign:"center", maxWidth: "80px", minWidth:"80px"}}>{props.messages.general_yes}</p>:<p style={{textAlign:"center", maxWidth: "80px", minWidth:"80px"}}>{props.messages.general_no}</p>}
                            </td>
                        <td className="table-body-row-source-dim">
                            {frag.dimensionSetSize>0?getDimensionsMap(frag.dimensionDtoList):null}
                            </td> 
                        <td className="table-body-row-source">
                            {getSurfacesMap(frag.surfaceDto, frag.title)}
                        </td> 
                        
                    </tr>
                    )
            }
            
        })
    }

    const handleSearchFiltering = (frag) => {
        
            if(frag.title?frag.title.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(frag.sourceInterSet[0].title?frag.sourceInterSet[0].title.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(frag.date?frag.date.toLowerCase().includes(search.toLowerCase()):false) return true
            else if(frag.sourceType?frag.sourceType.toLowerCase().includes(search.toLowerCase()):false) return true
            else return false        
    }

    const handleSearchUpdate = () => {
        console.log(inputEl.current.value)
        setSearch(inputEl.current.value)
    }

    return (
        <div className="sourceList">
            <p className="source-title">
                {props.messages.authorial_source}
                <span> {`(${sourceList.length})`}</span>
                <img src={informationIcon} className="glyphicon" onClick={() => setInfo(!info)}></img>
                <span className={info?"showInfo":"noInfo"}>
                    {props.messages.sourcelist_tt_sources}
                </span>
            </p>

            <CircleLoader loading={loading}></CircleLoader>
            <div className={loading?"loading-table":"search-container"}>
                <input ref={inputEl} className="search" placeholder="Search"></input>
                <img src={lupaIcon} alt="lupa" className="search-icon" onClick={() => handleSearchUpdate()}></img>
            </div>

            <div className={loading?"loading-table":"table-div"}>
            <table className={loading?"loading-table":"table"}>
                <thead>
                    <tr>
                        <th >{props.messages.header_documents}</th>
                        <th >{props.messages.general_transcription}</th>
                        <th >{props.messages.general_date}</th>
                        <th >{props.messages.general_type}</th>
                        <th >{props.messages.general_LdoDLabel}</th>
                        <th >{props.messages.general_dimensions}</th>
                        <th >{props.messages.general_facsimiles}</th>
                    </tr>
                </thead>
                <tbody>
                   {masSourceToTable()}
                </tbody>
            </table>
        </div>
        </div>
    )
}

export default SourceList