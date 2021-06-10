import React, { useState, useEffect } from 'react'
import { Link, useHistory } from 'react-router-dom';
import ReactTooltip from 'react-tooltip';
import info from '../../../../resources/assets/information.svg'
import left from '../../../../resources/assets/left-arrow-blue.png'
import right from '../../../../resources/assets/right-arrow-blue.png'

const Navigation_virtual = (props) => {


    const [selectedInters, setSelectedInters] = useState([])
    const [virtualEditionsDto, setVirtualEditionsDto] = useState([])

    useEffect(() => {
        let aux = []
        if(props.data && props.data.inters){
            for(let el of props.data.inters){
                aux.push(el.acronym)
            }
            setSelectedInters(aux)
            if(props.data.virtualEditionsDto){
                setVirtualEditionsDto(props.data.virtualEditionsDto)
            }
        }
        
    }, [props.data])
    
    const handleNextClick = (xml, url, nextOrPrev) => {
        props.callbackNavigationHandler(xml, url, nextOrPrev)
    }

    const mapVirtualToView = () => {
        return virtualEditionsDto.map((virtual, i) => {
            return (
                <div key={i} style={{marginBottom:"10px"}}>
                    <Link to={`/edition/acronym/${virtual.acronym}`}>{virtual.acronym}</Link>
                    <div style={{marginTop:"10px"}}>
                        {mapVirtualInterpsToView(virtual.sortedInter4Frag, virtual.acronym)}
                    </div>
                    
                </div>
            )
        })
    }

    const checkboxSelectedHandler = (fragmentExternalId, externalId) => {
        var aux = []
        for(let el of props.data.inters){
            if(el.type=="VIRTUAL")
                aux.push(el.externalId)
        }
        if(!aux.includes(externalId)){
            aux.push(externalId)
        }
        else{
            var index = aux.indexOf(externalId);
            if (index !== -1) {
                aux.splice(index, 1);
            }
        }
        props.callbackCheckboxHandler(fragmentExternalId, aux)
    }

    const mapVirtualInterpsToView = (sortedInters, acronym) => {
        return sortedInters.map((inter, i) => {
            return (
                <div key={i} className="navigation-row">
                    <input className="navigation-checkbox" type="checkbox" name={inter.externalId} 
                        onChange={() => {checkboxSelectedHandler(props.data.fragment.externalId, inter.externalId)}} 
                        checked={selectedInters.includes(acronym)}>
                    </input>
                    <div className="navigation-row-inter">
                        <img style={{height:"15px", width:"15px", cursor:"pointer"}} src={left} onClick={() => {
                            handleNextClick(inter.fragmentXmlId, inter.urlId, "prev")
                            }}></img>
                        <Link to={`/fragments/fragment/${inter.fragmentXmlId}/inter/${inter.urlId}`} onClick={() => handleNextClick(inter.fragmentXmlId, inter.urlId, false)}>{inter.number}</Link>
                        <img style={{height:"15px", width:"15px", cursor:"pointer"}} src={right} onClick={() => {
                            handleNextClick(inter.fragmentXmlId, inter.urlId, "next")
                            }}></img>
                    </div>
                </div>
            )
        })
    }
    const mapArchiveToView = () => {
        return props.data.ldoD.archiveEdition.sortedInter4Frag.map((inter, i) => {
            return (
                <div key={i} className="navigation-row">
                    <input className="navigation-checkbox" type="checkbox" name={inter.externalId} 
                        onChange={() => {checkboxSelectedHandler(props.data.fragment.externalId, inter.externalId)}}
                        checked={selectedInters.includes(props.data.ldoD.archiveEdition.acronym)}></input>
                    <div className="navigation-row-inter">
                        <img style={{height:"15px", width:"15px", cursor:"pointer"}} src={left} onClick={() => {
                            handleNextClick(inter.fragmentXmlId, inter.urlId, "prev")
                            }}></img>
                        <Link to={`/fragments/fragment/${inter.fragmentXmlId}/inter/${inter.urlId}`} onClick={() => handleNextClick(inter.fragmentXmlId, inter.urlId, false)}>{inter.number}</Link>
                        <img style={{height:"15px", width:"15px", cursor:"pointer"}} src={right} onClick={() => {
                            handleNextClick(inter.fragmentXmlId, inter.urlId, "next")
                            }}></img>
                    </div>
                </div>
            )
        })
    }
    return (
        <div className="navigation-view" style={{marginTop:"20px"}}>
            <div className="navigation-view-title-div">
                <p className="navigation-view-title">{props.messages.virtual_editions}</p>
                <img src={info} data-tip={props.messages.info_virtualeditions}
                    className="reading-info" style={{width:"20px", padding:"0", marginLeft:"5px"}}></img>
                <ReactTooltip backgroundColor="#fff" textColor="#333" border={true} borderColor="#000" className="reading-tooltip" place="bottom"/>
            </div>
            <div style={{marginTop:"10px"}}>
                <Link to={`/edition/acronym/${props.data && props.data.ldoD?props.data.ldoD.archiveEdition.acronym:null}`}>Arquivo LdoD</Link>
                {props.data && props.data.ldoD?
                <div style={{marginTop:"10px"}}>
                    {mapArchiveToView()}
                </div>
                    
                :null
                }
            </div>
            <div style={{marginTop:"10px"}}>
                {props.data?
                    mapVirtualToView()
                :null
                }
            </div>
        </div>
    )
}


export default Navigation_virtual