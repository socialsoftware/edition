import React, {useEffect, useRef, useState} from 'react'
import { getExpertEditionList } from '../../../util/utilsAPI';
import CircleLoader from "react-spinners/RotateLoader";
import { Link } from 'react-router-dom';
import eyeIcon from '../../../resources/assets/eye.svg'
import lupaIcon from '../../../resources/assets/lupa.svg'
import ReactTooltip from 'react-tooltip';

const ExpertEditionList = (props) => {

    const [editionData, setEditionData] = useState([])
    const [editor, setEditor] = useState(null)
    const [listSize, setListSize] = useState(0)
    const [loading, setLoading] = useState(true)
    const [search, setSearch] = useState("")
    const inputEl = useRef(null)

    useEffect(() => {
        getExpertEditionList(props.acronym)
            .then(res => {
                console.log(res.data);
                setEditor(res.data.editor);
                setEditionData(res.data.sortedInterpsList)
                setListSize(res.data.sortedSize)
                setTimeout(() => setLoading(false), 500)
            })
    }, [props.acronym])


    const mapEditionToTable = () => {
        return editionData.map((interp,i) => {
            if(search==="" || handleSearchFiltering(interp)){
                return(
                    <tr className="table-body-row-row" key={i}>
                        <td style={{textAlign:"center"}} className="table-body-row">
                            {interp.number!==0?interp.number:null}
                        </td>
                        <td className="table-body-row">
                            <Link className="table-body-title"
                            to={`/fragments/fragment/${interp.xmlId}/inter/${interp.urlId}`}>{interp.title}</Link></td>
                        <td className="table-body-row">
                        <Link className="table-body-title"
                            to={`/reading/fragment/${interp.xmlId}/inter/${interp.urlId}/start`}><img className="icon" src={eyeIcon}></img></Link>
                        </td>
                        
                        {editor === "Jacinto do Prado Coelho"?
                        <td style={{textAlign:"center"}} className="table-body-row">
                            {interp.volume}
                            </td>:null
                        }   
                        
                        <td style={{textAlign:"center"}} className="table-body-row">
                            {interp.startPage}
                        </td>
                        <td style={{textAlign:"center"}} className="table-body-row">
                            {interp.date}
                        </td>
                        <td className="table-body-row">
                            {interp.heteronym}
                        </td>
                    </tr>
                    )
            }
            
        })
    }

    const handleSearchFiltering = (interp) => {
        if(interp.title?interp.title.toLowerCase().includes(search.toLowerCase()):false) return true
        else if(interp.date?interp.date.toLowerCase().includes(search.toLowerCase()):false) return true
        else if(interp.heteronym?interp.heteronym.toLowerCase().includes(search.toLowerCase()):false) return true
        else if(interp.startPage?interp.startPage.toString().includes(search.toLowerCase()):false) return true
        else return false
    }

    const handleSearchUpdate = () => {
        console.log(inputEl.current.value)
        setSearch(inputEl.current.value)
    }

    return (
        <div>
            <p className="list-title">{props.messages.tableofcontents_editionof} {editor}<span> {`(${listSize})`}</span></p>
            <CircleLoader loading={loading}></CircleLoader>
            <div className={loading?"loading-table":"search-container"}>
                    <input ref={inputEl} className="search" placeholder="Search"></input>
                    <img src={lupaIcon} alt="lupa" className="search-icon" onClick={() => handleSearchUpdate()}></img>
            </div>
            <div className={loading?"loading-table":"table-div"}>
                <table className={loading?"loading-table":"table"} data-pagination="false">
                    <thead>
                        <tr>
                            <th style={{textAlign:"center"}} data-tip={props.messages.tableofcontents_tt_number}> {props.messages.tableofcontents_number}</th>
                            <th data-tip={props.messages.tableofcontents_title}> {props.messages.tableofcontents_title}</th>
                            <th style={{textAlign:"center"}} data-tip={props.messages.tableofcontents_tt_reading}> {props.messages.general_reading}</th>
                            {editor === "Jacinto do Prado Coelho"?
                            <th style={{textAlign:"center"}} data-tip={props.messages.tableofcontents_tt_volume}> {props.messages.tableofcontents_volume}</th>
                            :null
                            }
                            <th style={{textAlign:"center"}} data-tip={props.messages.tableofcontents_tt_page}> {props.messages.tableofcontents_page}</th>
                            <th style={{textAlign:"center"}} data-tip={props.messages.tableofcontents_tt_date}> {props.messages.general_date}</th>
                            <th style={{textAlign:"center"}} data-tip={props.messages.tableofcontents_tt_heteronym}> {props.messages.general_heteronym}</th>
                        </tr>
                    </thead>
                    <tbody>
                        {mapEditionToTable()}
                    </tbody>
                    <ReactTooltip backgroundColor="#000" textColor="#fff" border={true} borderColor="#000" className="virtual-tooltip" place="bottom" effect="solid"/>

                </table>
            </div>
        </div>
    )
}

export default ExpertEditionList