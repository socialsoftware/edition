import React, { useEffect, useState } from 'react'
import { Link, useHistory } from 'react-router-dom'
import ReactTooltip from 'react-tooltip';
import info from '../../../../resources/assets/information.svg'
import left from '../../../../resources/assets/left-arrow-blue.png'
import right from '../../../../resources/assets/right-arrow-blue.png'
import { getFragmentWithXml, getFragmentWithXmlNoUser,
    getNextFragmentWithXmlAndUrl, getNextFragmentWithXmlAndUrlNoUSER, 
    getPrevFragmentWithXmlAndUrl, getPrevFragmentWithXmlAndUrlNoUSER,
    getFragmentWithXmlAndUrl, getFragmentWithXmlAndUrlNoUser,
    getIntersByArrayExternalId} from '../../../../util/API/FragmentAPI'

const Navigation_expert = (props) => {

    const [selectedInters, setSelectedInters] = useState([])
    const history = useHistory()
    const [data, setData] = useState(null)

    useEffect(() => {
        if(props.isAuthenticated){
            if(props.urlId)
            getFragmentWithXmlAndUrl(props.xmlId, props.urlId, props.selectedVEAcr)
                    .then(res => {
                        dataHandler(res.data)
                        handleFragmentChangeForBodyData(res.data)
                    })
                    
            else if(props.xmlId){
                getFragmentWithXml(props.xmlId, props.selectedVEAcr)
                    .then(res => {
                        dataHandler(res.data)
                    })
            }
        }
        else{
            if(props.urlId)
            getFragmentWithXmlAndUrlNoUser(props.xmlId, props.urlId, props.selectedVEAcr)
                .then(res => {
                    dataHandler(res.data)
                    handleFragmentChangeForBodyData(res.data)
                })
            else if(props.xmlId)
            getFragmentWithXmlNoUser(props.xmlId, props.selectedVEAcr)
                    .then(res => {
                        dataHandler(res.data)
                    })
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [props.xmlId, props.urlId])

    useEffect(() => {
        if(props.isAuthenticated){
            if(props.urlIdVirtualToExpert)
            getFragmentWithXmlAndUrl(props.xmlIdVirtualToExpert, props.urlIdVirtualToExpert, props.selectedVEAcr)
                    .then(res => {
                        dataHandler(res.data)
                    })
                    
            else if(props.xmlIdVirtualToExpert){
                getFragmentWithXml(props.xmlIdVirtualToExpert, props.selectedVEAcr)
                    .then(res => {
                        dataHandler(res.data)
                    })
            }
        }
        else{
            if(props.urlIdVirtualToExpert)
            getFragmentWithXmlAndUrlNoUser(props.xmlIdVirtualToExpert, props.urlIdVirtualToExpert, props.selectedVEAcr)
                .then(res => {
                    dataHandler(res.data)
                })
            else if(props.xmlIdVirtualToExpert)
            getFragmentWithXmlNoUser(props.xmlIdVirtualToExpert, props.selectedVEAcr)
                    .then(res => {
                        dataHandler(res.data)
                    })
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [props.xmlIdVirtualToExpert, props.urlIdVirtualToExpert])

    const handleFragmentChangeForBodyData = (data1) => {
        var aux = []
        for(let el of data1.inters){
            if(el.type!=="VIRTUAL")
                aux.push(el.externalId)
        }
        props.callbackSetSelected(aux)
        props.callbackSetExternalId(data1.fragment.externalId)
    }


    useEffect(() => {
        if(props.currentType !== "expert"){
            setSelectedInters([])
            let aux = {...data}
            aux["inters"] = []
            setData(aux)
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [props.currentType])



    const handleNextClick = (xml, url, nextOrPrev) => {
        if(nextOrPrev === "next"){
            if(props.isAuthenticated){
                getNextFragmentWithXmlAndUrl(xml, url, props.selectedVEAcr)
                .then(res => {
                    history.replace(`/fragments/fragment/${res.data.fragment.fragmentXmlId}/inter/${res.data.inters[0]?res.data.inters[0].urlId:null}`)
                    dataHandler(res.data)
                    handleFragmentChangeForBodyData(res.data)
                    props.callbackNavigationHandler(res.data.fragment.fragmentXmlId, res.data.inters[0]?res.data.inters[0].urlId:null)
                })
            }
            else{
                getNextFragmentWithXmlAndUrlNoUSER(xml, url, props.selectedVEAcr)
                .then(res => {
                    history.replace(`/fragments/fragment/${res.data.fragment.fragmentXmlId}/inter/${res.data.inters[0]?res.data.inters[0].urlId:null}`)
                    dataHandler(res.data)
                    handleFragmentChangeForBodyData(res.data)
                    props.callbackNavigationHandler(res.data.fragment.fragmentXmlId, res.data.inters[0]?res.data.inters[0].urlId:null)
            })
            }
            
        }
        else if(nextOrPrev === "prev"){
            if(props.isAuthenticated){
                getPrevFragmentWithXmlAndUrl(xml, url, props.selectedVEAcr)
                .then(res => {
                    history.replace(`/fragments/fragment/${res.data.fragment.fragmentXmlId}/inter/${res.data.inters[0]?res.data.inters[0].urlId:null}`)
                    dataHandler(res.data)
                    handleFragmentChangeForBodyData(res.data)
                    props.callbackNavigationHandler(res.data.fragment.fragmentXmlId, res.data.inters[0]?res.data.inters[0].urlId:null)
                }) 
            }
            else{
                getPrevFragmentWithXmlAndUrlNoUSER(xml, url, props.selectedVEAcr)
                .then(res => {
                    history.replace(`/fragments/fragment/${res.data.fragment.fragmentXmlId}/inter/${res.data.inters[0]?res.data.inters[0].urlId:null}`)
                    dataHandler(res.data)
                    handleFragmentChangeForBodyData(res.data)
                    props.callbackNavigationHandler(res.data.fragment.fragmentXmlId, res.data.inters[0]?res.data.inters[0].urlId:null)
                }) 
            }
            
        }
        else{
            if(props.isAuthenticated){
                getFragmentWithXmlAndUrl(xml, url, props.selectedVEAcr)
                .then(res => {
                    dataHandler(res.data)
                    handleFragmentChangeForBodyData(res.data)
                    props.callbackNavigationHandler(res.data.fragment.fragmentXmlId, res.data.inters[0]?res.data.inters[0].urlId:null)
                })
            }
            else{
                getFragmentWithXmlAndUrlNoUser(xml, url, props.selectedVEAcr)
                    .then(res => {
                        dataHandler(res.data)
                        handleFragmentChangeForBodyData(res.data)
                        props.callbackNavigationHandler(res.data.fragment.fragmentXmlId, res.data.inters[0]?res.data.inters[0].urlId:null)
                    })
            }
            
        }
        props.callbackSetCurrentType("expert")
    }

    const dataHandler = (obj) => {
        if(data === null){
            setData(obj)
            let aux1 = []
            for(let el of obj.inters){
                aux1.push(el.externalId)
            }
            setSelectedInters(aux1)
            props.callbackSetCurrentType("expert")
            props.callbackSetSelected(aux1)
        } 
        else{
            let aux = {...data}
            if(obj["fragment"]!==null) aux["fragment"] = obj["fragment"]
            if(obj["ldoD"] !== undefined && obj["ldoD"]!==null){
                aux["ldoD"] = obj["ldoD"]
            }
            if(obj["ldoDuser"]!==null) aux["ldoDuser"] = obj["ldoDuser"]
            if(obj["inters"]!==undefined && obj["inters"]!==null) aux["inters"] = obj["inters"]
            if(obj["hasAccess"]!==null) aux["hasAccess"] = obj["hasAccess"]
            if(obj["transcript"]!==null) aux["transcript"] = obj["transcript"]
            if(obj["setTranscriptionSideBySide"]!==null) aux["setTranscriptionSideBySide"] = obj["setTranscriptionSideBySide"]
            if(obj["variations"]!==null) aux["variations"] = obj["variations"]
            if(obj["writerLineByLine"]!==null) aux["writerLineByLine"] = obj["writerLineByLine"]
            if(obj["surface"]!==null) aux["surface"] = obj["surface"]
            aux["nextPb"] = obj["nextPb"]
            aux["nextSurface"] = obj["nextSurface"]
            aux["prevPb"] = obj["prevPb"]
            aux["prevSurface"] = obj["prevSurface"]
            setData(aux)
            let aux1 = []
            if(obj.inters !== null && obj.inters !== undefined){
                for(let el of obj.inters){
                    aux1.push(el.externalId)
                }
                setSelectedInters(aux1)
            }
        }
    }


    const getIntersByExternalId = (fragmentExternalId, arrayInterId) => {
        getIntersByArrayExternalId(fragmentExternalId, arrayInterId)
            .then(res => {
                dataHandler(res.data)
                if(!res.data.inters) history.push(`/fragments/fragment/${data.fragment?data.fragment.fragmentXmlId:null}`)
            })
    }

    const checkboxSelectedHandler = (fragmentExternalId, externalId) => {
        var aux = []
        for(let el of data.inters){
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
        setSelectedInters(aux)
        props.callbackSetExternalId(fragmentExternalId)
        props.callbackSetSelected(aux)
        props.callbackSetCurrentType("expert")
        getIntersByExternalId(fragmentExternalId, aux)
        

    }

    const mapSourceIntersToView = () => {
        return data.fragment.sourceInterDtoList.map((sourceInter, i) => {
            return (
                <div key={i} className="navigation-row">
                    <input className="navigation-checkbox" type="checkbox" name={sourceInter.externalId}
                        onChange={() => {checkboxSelectedHandler(data.fragment.externalId, sourceInter.externalId)}} 
                        checked={selectedInters.includes(sourceInter.externalId)}></input>
                    <Link onClick={() => handleNextClick(data.fragment.fragmentXmlId, sourceInter.urlId, false)} className="navigation-name" to={`/fragments/fragment/${data.fragment.fragmentXmlId}/inter/${sourceInter.urlId}`}>{sourceInter.shortName}</Link>
                </div>
                
            )
        })
    }

    const mapExpertIntersToRow = (inters) => {
        return inters.map((inter, i) => {
            return (
                <div key={i} className="navigation-row">
                    <input className="navigation-checkbox" type="checkbox" name={inter.externalID} 
                        onChange={() => {checkboxSelectedHandler(data.fragment?
                                data.fragment.externalId:null, inter.externalID)}} 
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
        return data.ldoD.sortedExpert.map((expert, i) => {
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
                {data && data.fragment?
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
                    {data && data.ldoD?
                        mapExpertToView()
                    :null
                    }
                </div>
            </div>
        </div>
    )
}

export default Navigation_expert