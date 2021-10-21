import React, { useState, useEffect } from 'react'
import { Link, useHistory, useLocation } from 'react-router-dom';
import ReactTooltip from 'react-tooltip';
import info from '../../../../resources/assets/information.svg'
import left from '../../../../resources/assets/left-arrow-blue.png'
import right from '../../../../resources/assets/right-arrow-blue.png'
import {ReactComponent as Plus} from '../../../../resources/assets/plus-lg.svg'
import {getVirtualFragmentWithXmlAndUrl, getVirtualFragmentWithXml,
    getVirtualFragmentWithXmlNoUser, getVirtualFragmentWithXmlAndUrlNoUser,
    getNextVirtualFragmentWithXmlAndUrl, getNextVirtualFragmentWithXmlAndUrlNoUSER, 
    getPrevVirtualFragmentWithXmlAndUrl, getPrevVirtualFragmentWithXmlAndUrlNoUSER, 
    getIntersByArrayExternalId, addToEdition} from '../../../../util/API/FragmentAPI'

const Navigation_virtual = (props) => {

    const [selectedInters, setSelectedInters] = useState([])
    const [virtualEditionsDto, setVirtualEditionsDto] = useState([])
    const [data, setData] = useState(null)
    const history = useHistory()
    const location = useLocation()

    useEffect(() => {
        if(props.isAuthenticated){
            if(props.urlId)
                getVirtualFragmentWithXmlAndUrl(props.xmlId, props.urlId, props.selectedVEAcr)
                    .then(res => {
                        dataHandler(res.data)
                        handleFragmentChangeForBodyData(res.data)
                    })
                    
            else if(props.xmlId){
                getVirtualFragmentWithXml(props.xmlId, props.selectedVEAcr)
                    .then(res => {
                        dataHandler(res.data)
                    })
            }
        }
        else{
            if(props.urlId)
                getVirtualFragmentWithXmlAndUrlNoUser(props.xmlId, props.urlId, props.selectedVEAcr)
                    .then(res => {
                        dataHandler(res.data)
                        handleFragmentChangeForBodyData(res.data)           
                    })
            else if(props.xmlId)
                getVirtualFragmentWithXmlNoUser(props.xmlId, props.selectedVEAcr)
                    .then(res => {
                        dataHandler(res.data)
                    })
        }        
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [props.xmlId, props.urlId])

    useEffect(() => {
        if(props.isAuthenticated){
            if(props.urlIdExpertToVirtual)
                getVirtualFragmentWithXmlAndUrl(props.xmlIdExpertToVirtual, props.urlIdExpertToVirtual, props.selectedVEAcr)
                    .then(res => {
                        dataHandler(res.data)
                    })
                    
            else if(props.xmlIdExpertToVirtual){
                getVirtualFragmentWithXml(props.xmlIdExpertToVirtual, props.selectedVEAcr)
                    .then(res => {
                        dataHandler(res.data)
                    })
            }
        }
        else{
            if(props.urlIdExpertToVirtual)
                getVirtualFragmentWithXmlAndUrlNoUser(props.xmlIdExpertToVirtual, props.urlIdExpertToVirtual, props.selectedVEAcr)
                .then(res => {
                    dataHandler(res.data)                    
                })
            else if(props.xmlIdExpertToVirtual)
                getVirtualFragmentWithXmlNoUser(props.xmlIdExpertToVirtual, props.selectedVEAcr)
                    .then(res => {
                        dataHandler(res.data)
                    })
        }        
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [props.xmlIdExpertToVirtual, props.urlIdExpertToVirtual])

    useEffect(() => {
        if(props.currentType !== "virtual"){
            setSelectedInters([])
            let aux = {...data}
            aux["inters"] = []
            setData(aux)
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [props.currentType])

    const handleFragmentChangeForBodyData = (data1) => {
        var aux = []
        for(let el of data1.inters){
            if(el.type!=="EXPERT")
                aux.push(el.externalId)
        }
        props.callbackSetSelected(aux)
        props.callbackSetExternalId(data1.fragment.externalId)
    }

    const dataHandler = (obj) => {
        if(data === null){
            setData(obj)
            let aux1 = []
            for(let el of obj.inters){
                aux1.push(el.externalId)
            }
            setSelectedInters(aux1)
            props.callbackSetCurrentType("virtual")
            if(obj.virtualEditionsDto){
                setVirtualEditionsDto(obj.virtualEditionsDto)
            }
        } 
        else{
            let aux = {...data}
            if(obj["fragment"]!==null && obj["fragment"]!==undefined) aux["fragment"] = obj["fragment"]
            if(obj["ldoD"] !== undefined)
                if(obj["ldoD"]["archiveEdition"]!==null) aux["ldoD"] = obj["ldoD"]
            
            if(obj["ldoDuser"]!==null) aux["ldoDuser"] = obj["ldoDuser"]
            if(obj["inters"]!==undefined && obj["inters"]!==null) aux["inters"] = obj["inters"]
            if(obj["hasAccess"]!==null) aux["hasAccess"] = obj["hasAccess"]
            if(obj["transcript"]!==null) aux["transcript"] = obj["transcript"]
            if(obj["virtualEditionsDto"]!==null) aux["virtualEditionsDto"] = obj["virtualEditionsDto"]
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
            if(aux.virtualEditionsDto){
                setVirtualEditionsDto(aux.virtualEditionsDto)
            }
        }
    }

    const addToEditionHandler = (externalId) => {
        addToEdition(externalId, data.inters[0].externalId)
            .then((res) => {
                console.log(res);
                var path = location.pathname.split('/')
                getVirtualFragmentWithXmlAndUrl(path[3], path[5], props.selectedVEAcr)
                    .then(res => {
                        dataHandler(res.data)
                    })
            })
            .catch(() => {
                alert("Error adding to edition!")
            })
    }
    
    const handleNextClick = (xml, url, nextOrPrev) => {
        if(nextOrPrev === "next"){
            if(props.isAuthenticated){
                getNextVirtualFragmentWithXmlAndUrl(xml, url, props.selectedVEAcr)
                .then(res => {
                    history.replace(`/fragments/fragment/${res.data.fragment.fragmentXmlId}/inter/${res.data.inters[0]?res.data.inters[0].urlId:null}`)
                    dataHandler(res.data)
                    handleFragmentChangeForBodyData(res.data)
                    props.callbackNavigationHandler(res.data.fragment.fragmentXmlId, res.data.inters[0]?res.data.inters[0].urlId:null)
                })
            }
            else{
                getNextVirtualFragmentWithXmlAndUrlNoUSER(xml, url, props.selectedVEAcr)
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
                getPrevVirtualFragmentWithXmlAndUrl(xml, url, props.selectedVEAcr)
                .then(res => {
                    history.replace(`/fragments/fragment/${res.data.fragment.fragmentXmlId}/inter/${res.data.inters[0]?res.data.inters[0].urlId:null}`)
                    dataHandler(res.data)
                    handleFragmentChangeForBodyData(res.data)
                    props.callbackNavigationHandler(res.data.fragment.fragmentXmlId, res.data.inters[0]?res.data.inters[0].urlId:null)
                }) 
            }
            else{
                getPrevVirtualFragmentWithXmlAndUrlNoUSER(xml, url, props.selectedVEAcr)
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
                getVirtualFragmentWithXmlAndUrl(xml, url, props.selectedVEAcr)
                .then(res => {
                    dataHandler(res.data)
                    props.callbackNavigationHandler(xml, url)
                    handleFragmentChangeForBodyData(res.data)
                })
            }
            else{
                getVirtualFragmentWithXmlAndUrlNoUser(xml, url, props.selectedVEAcr)
                .then(res => {
                    dataHandler(res.data)
                    props.callbackNavigationHandler(xml, url)
                    handleFragmentChangeForBodyData(res.data)
                })
            }
            
        }
        props.callbackSetCurrentType("virtual")
    }

    const mapVirtualToView = () => {
        return virtualEditionsDto.map((virtual, i) => {
            return (
                <div key={i} style={{marginBottom:"10px"}}>
                    <Link to={`/edition/acronym/${virtual.acronym}`}>{virtual.acronym}</Link>
                    <div style={{marginTop:"10px"}}>
                        {
                            virtual.sortedInter4Frag.length > 0?
                                mapVirtualInterpsToView(virtual.sortedInter4Frag)
                            :
                            virtual.participantSetContains && data.inters.length === 1 && virtual.canAddFragInter?
                                <div className="navigation-row-add">
                                    <span className="navigation-add-button" onClick={() => addToEditionHandler(virtual.externalId)}>
                                        <Plus fill="#fff" style={{width:"10px", height:"10px"}}></Plus>
                                        <p>{props.messages.general_add}</p>
                                    </span>
                                </div>
                            :null
                            }
                    </div>
                    
                </div>
            )
        })
    }

    const getIntersByExternalId = (fragmentExternalId, arrayInterId) => {
        getIntersByArrayExternalId(fragmentExternalId, arrayInterId)
            .then(res => {
                dataHandler(res.data)
                if(!res.data.inters) history.push(`/fragments/fragment/${data.fragment.fragmentXmlId}`)
            })
    }

    const checkboxSelectedHandler = (fragmentExternalId, externalId) => {
        var aux = []
        for(let el of data.inters){
            if(el.type==="VIRTUAL"){
                aux.push(el.externalId)
            }
                
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
        props.callbackSetCurrentType("virtual")
        getIntersByExternalId(fragmentExternalId, aux)
    }

    const mapVirtualInterpsToView = (sortedInters) => {
        return sortedInters.map((inter, i) => {
            return (
                <div key={i} className="navigation-row">
                    <input className="navigation-checkbox" type="checkbox" name={inter.externalId} 
                        onChange={() => {checkboxSelectedHandler(data.fragment.externalId, inter.externalId)}} 
                        checked={selectedInters.includes(inter.externalId)}>
                    </input>
                    <div className="navigation-row-inter">
                        <img alt="arrow" style={{height:"15px", width:"15px", cursor:"pointer"}} src={left} onClick={() => {
                            handleNextClick(inter.fragmentXmlId, inter.urlId, "prev")
                            }}></img>
                        <Link to={`/fragments/fragment/${inter.fragmentXmlId}/inter/${inter.urlId}`} onClick={() => handleNextClick(inter.fragmentXmlId, inter.urlId, false)}>{inter.number}</Link>
                        <img alt="arrow" style={{height:"15px", width:"15px", cursor:"pointer"}} src={right} onClick={() => {
                            handleNextClick(inter.fragmentXmlId, inter.urlId, "next")
                            }}></img>
                    </div>
                </div>
            )
        })
    }
    const mapArchiveToView = () => {
        return data.ldoD.archiveEdition.sortedInter4Frag.map((inter, i) => {
            return (
                <div key={i} className="navigation-row">
                    <input className="navigation-checkbox" type="checkbox" name={inter.externalId} 
                        onChange={() => {checkboxSelectedHandler(data.fragment.externalId, inter.externalId)}}
                        checked={selectedInters.includes(inter.externalId)}></input>
                    <div className="navigation-row-inter">
                        <img alt="arrow" style={{height:"15px", width:"15px", cursor:"pointer"}} src={left} onClick={() => {
                            handleNextClick(inter.fragmentXmlId, inter.urlId, "prev")
                            }}></img>
                        <Link to={`/fragments/fragment/${inter.fragmentXmlId}/inter/${inter.urlId}`} onClick={() => handleNextClick(inter.fragmentXmlId, inter.urlId, false)}>{inter.number}</Link>
                        <img alt="arrow" style={{height:"15px", width:"15px", cursor:"pointer"}} src={right} onClick={() => {
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
                <img alt="info" src={info} data-tip={props.messages.info_virtualeditions}
                    className="reading-info" style={{width:"20px", padding:"0", marginLeft:"5px"}}></img>
                <ReactTooltip backgroundColor="#fff" textColor="#333" border={true} borderColor="#000" className="reading-tooltip" place="bottom" effect="solid"/>
            </div>
            <div style={{marginTop:"10px"}}>
                <Link to={`/edition/acronym/${data && data.ldoD?data.ldoD.archiveEdition?data.ldoD.archiveEdition.acronym:null:null}`}>Arquivo LdoD</Link>
                {data && data.ldoD?
                <div style={{marginTop:"10px"}}>
                    {data?data.ldoD.archiveEdition?
                    mapArchiveToView()
                    :null:null
                
                }
                </div>
                    
                :null
                }
            </div>
            <div style={{marginTop:"10px"}}>
                {data?
                    mapVirtualToView()
                :null
                }
            </div>
        </div>
    )
}


export default Navigation_virtual