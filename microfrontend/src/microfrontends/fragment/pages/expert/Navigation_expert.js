import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import ReactTooltip from 'react-tooltip';
import info from '../../../../resources/assets/information.svg'
import left from '../../../../resources/assets/left-arrow-blue.png'
import right from '../../../../resources/assets/right-arrow-blue.png'


const Navigation_expert = (props) => {

    const [selectedInters, setSelectedInters] = useState([])

    useEffect(() => {
        let aux = []
        if(props.data && props.data.inters){
            for(let el of props.data.inters){
                aux.push(el.externalId)
            }
            setSelectedInters(aux)
        }
        
    }, [props.data])

    const handleNextClick = (xml, url, nextOrPrev) => {
        props.callbackNavigationHandler(xml, url, nextOrPrev)
    }


    const checkboxSelectedHandler = (fragmentExternalId, externalId) => {
        var aux = []
        for(let el of props.data.inters){
            if(el.type!=="VIRTUAL")
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

    const mapSourceIntersToView = () => {
        return props.data.fragment.sourceInterDtoList.map((sourceInter, i) => {
            return (
                <div key={i} className="navigation-row">
                    <input className="navigation-checkbox" type="checkbox" name={sourceInter.externalId}
                        onChange={() => {checkboxSelectedHandler(props.data.fragment.externalId, sourceInter.externalId)}} 
                        checked={selectedInters.includes(sourceInter.externalId)}></input>
                    <Link onClick={() => handleNextClick(props.data.fragment.fragmentXmlId, sourceInter.urlId, false)} className="navigation-name" to={`/fragments/fragment/${props.data.fragment.fragmentXmlId}/inter/${sourceInter.urlId}`}>{sourceInter.shortName}</Link>
                </div>
                
            )
        })
    }

    const mapExpertIntersToRow = (inters) => {
        return inters.map((inter, i) => {
            return (
                <div key={i} className="navigation-row">
                    <input className="navigation-checkbox" type="checkbox" name={inter.externalId} 
                        onChange={() => {checkboxSelectedHandler(props.data.fragment.externalId, inter.externalID)}} 
                        checked={selectedInters.includes(inter.externalID)}></input>
                    <div className="navigation-row-inter">
                        <img alt="arrow" style={{height:"15px", width:"15px", cursor:"pointer"}} src={left} onClick={() => handleNextClick(inter.fragmentXmlId, inter.urlId, "prev")}></img>
                        <Link to={`/fragments/fragment/${inter.fragmentXmlId}/inter/${inter.urlId}`} onClick={() => handleNextClick(inter.fragmentXmlId, inter.urlId, false)}>{inter.number}</Link>
                        <img alt="arrow" style={{height:"15px", width:"15px", cursor:"pointer"}} src={right} onClick={() => handleNextClick(inter.fragmentXmlId, inter.urlId, "next")}></img>
                    </div>
                </div>
            )
        })
    }

    const mapExpertToView = () => {
        return props.data.ldoD.sortedExpert.map((expert, i) => {
            if(expert.inter.length > 0){
                return (
                    <div key={i} style={{marginTop:"10px"}}>
                        <Link to={`/edition/acronym/${expert.acronym}`}>{expert.editor}</Link>
                        <div style={{marginTop:"10px"}}>
                            {mapExpertIntersToRow(expert.inter)}
                        </div>
                        
                    </div>
                    
                )
            }
            else return null
        })
    }
    return (
        <div className="navigation-view">
            <div>
                <p className="navigation-view-title" style={{marginBottom:"10px"}}>{props.messages.authorial_source}</p>
                {props.data && props.data.fragment?
                    mapSourceIntersToView()
                :null
                }
            </div>
            <div style={{marginTop:"40px"}}>
                <div className="navigation-view-title-div">
                    <p className="navigation-view-title">{props.messages.edition_experts}</p>
                    <img alt="info" src={info} data-tip={props.messages.info_experts}
                        className="reading-info" style={{width:"20px", padding:"0", marginLeft:"5px"}}></img>
                    <ReactTooltip backgroundColor="#fff" textColor="#333" border={true} borderColor="#000" className="reading-tooltip" place="bottom" effect="solid"/>
                </div>
                <div style={{marginTop:"30px"}}>
                    {props.data && props.data.ldoD?
                        mapExpertToView()
                    :null
                    }
                </div>
            </div>
        </div>
    )
}

export default Navigation_expert