import React, { useEffect, useState } from 'react'
import { Link, useHistory, useLocation } from 'react-router-dom'
import {ReactComponent as LeftArrow} from '../../../resources/assets/caret-left-fill.svg'
import {ReactComponent as Check} from '../../../resources/assets/check.svg'
import {ReactComponent as Save} from '../../../resources/assets/floppy-disk.svg'
import CircleLoader from "react-spinners/RotateLoader";

import { getRecommendationPage, setCriteriaChange, saveNewInters } from '../../../util/API/VirtualAPI'

const Recommendation = (props) => {

    const history = useHistory()
    const location = useLocation()
    const [data, setData] = useState(null)
    const [heteronymWeight, setHeteronymWeight] = useState(0.0)
    const [dateWeight, setDateWeight] = useState(0.0)
    const [textWeight, setTextWeight] = useState(0.0)
    const [taxonomyWeight, setTaxonomyWeight] = useState(0.0)
    const [edition, setEdition] = useState(null)
    const [currentEdition, setCurrentEdition] = useState(null)
    const [interId, setInterId] = useState(null)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        var path = location.pathname.split('/')
        let mounted = true
        getRecommendationPage(path[5])
            .then(res => {
                if (mounted) {
                    setData(res.data.virtualEdition)
                    if(res.data.recommendedEditionInter){
                        setEdition(res.data.recommendedEditionInter)
                        setCurrentEdition(res.data.recommendedEditionInter)
                    } 
                    if(res.data.interId) setInterId(res.data.interId)
                    setLoading(false)
                }
                
            })
            .catch((err) => {
                console.log(err);
                alert("Acesso Negado")
                history.push("/")
            })
            return function cleanup() {
                mounted = false
            }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])


    const setCriteriaChangeHandler = () => {
        var json = []
        json.push(
            {
                type: "heteronym",
                acronym: data.acronym,
                weight: heteronymWeight
            }
        )
        json.push(
            {
                type: "date",
                acronym: data.acronym,
                weight: dateWeight
            }
        )
        json.push(
            {
                type: "text",
                acronym: data.acronym,
                weight: textWeight
            }
        )
        json.push(
            {
                type: "taxonomy",
                acronym: data.acronym,
                weight: taxonomyWeight
            }
        )
        var data1 = {
            acronym : data.acronym,
            id : interId,
            properties : json
        }
        setCriteriaChange(data1, data.externalId)
            .then(res => {
                setCurrentEdition(res.data.recommendedEditionInter)
            })
    }

    const mapUsesToInter = (inter) => {
        return inter.listUsed.map((used,i) => {
            return <Link key={i} className="virtual-link" to={`/fragments/fragment/${used.xmlId}/inter/${used.urlId}`}>{used.shortName}</Link>
        })
    }

    const mapEditionToTable = () => {
        return currentEdition.map((inter, i) => {
            return(
                <tr key={i}>
                    <td>{inter.number!==0?inter.number:null}</td>
                    <td><Link className="virtual-link" to={`/fragments/fragment/${inter.fragmentXmlId}/inter/${inter.urlId}`}>{inter.title}</Link></td>
                    <td>
                        {
                            inter.externalId === interId?
                                null
                            :
                                <span className="virtual-submit-button" style={{maxHeight:"20px"}} onClick={() => setNewInitialHandler(inter.externalId)}>
                                    <Check style={{marginRight:"3px", height:"15px"}}></Check>
                                    <span style={{fontSize:"13px"}}>{props.messages.recommendation_setinitial}</span>
                                </span>
                        }
                    </td>
                    <td>
                        {"->"}{mapUsesToInter(inter)}
                    </td>
                </tr>
            )
        })
    }

    const saveNewIntersHandler = () => {
        var inters = []
        for(let el of currentEdition){
            inters.push(el.externalId)
        }
        saveNewInters(data.acronym, inters, data.externalId)
    }

    const setNewInitialHandler = (id) => {
        var aux = [...edition]
        for(let i = 0; i<aux.length; i++){
            if(aux[i].externalId === id){
                let val = aux[i]
                aux.splice(i, 1)
                aux.unshift(val)
                setInterId(val.externalId)
                break
            }
        }
        setCurrentEdition(aux)
    }
    return (
        <div className="virtual-editions">
            <button className="virtual-back-button" onClick={() => history.goBack()}>
                <LeftArrow></LeftArrow>
                <p>{props.messages.general_back}</p>
            </button>
            <span className="virtual-body-title">
                <p>{data?data.title:null}</p>
            </span>

            <div className="virtual-criteria">
                <p className="virtual-criteria-title"><strong>{props.messages.recommendation_criteria}</strong></p>
                <div className="virtual-criteria-zone">
                    <div className={heteronymWeight>0?"virtual-criteria-input":"virtual-criteria-input-fade"}>
                        <p className="virtual-criteria-input-title">{props.messages.general_heteronym}</p>
                        <input max="1" min="0" step="0.2" type="range" className="virtual-criteria-range" onChange={e => {
                            setHeteronymWeight(parseFloat(e.target.value))
                            }} onMouseUp={(e) => setCriteriaChangeHandler()} value={heteronymWeight}></input>
                    </div>
                    <div className={dateWeight>0?"virtual-criteria-input":"virtual-criteria-input-fade"}>
                        <p className="virtual-criteria-input-title">{props.messages.general_date}</p>
                        <input max="1" min="0" step="0.2" type="range" className="virtual-criteria-range" onChange={e => {
                            setDateWeight(parseFloat(e.target.value))
                            }} onMouseUp={(e) => setCriteriaChangeHandler()} value={dateWeight}></input>
                    </div>
                    <div className={textWeight>0?"virtual-criteria-input":"virtual-criteria-input-fade"}>
                        <p className="virtual-criteria-input-title">{props.messages.general_text}</p>
                        <input max="1" min="0" step="0.2" type="range" className="virtual-criteria-range" onChange={e => {
                            setTextWeight(parseFloat(e.target.value))
                            }} onMouseUp={(e) => setCriteriaChangeHandler()} value={textWeight}></input>
                    </div>
                    <div className={taxonomyWeight>0?"virtual-criteria-input":"virtual-criteria-input-fade"}>
                        <p className="virtual-criteria-input-title">{props.messages.general_taxonomy}</p>
                        <input max="1" min="0" step="0.2" type="range" className="virtual-criteria-range" onChange={e => {
                            setTaxonomyWeight(parseFloat(e.target.value))
                            }} onMouseUp={(e) => setCriteriaChangeHandler()} value={taxonomyWeight}></input>
                    </div>
                </div>
                <span className="virtual-submit-button virtual-absolute-button" style={{maxHeight:"20px"}} onClick={() => saveNewIntersHandler()}>
                    <Save fill="#fff" style={{marginRight:"3px", height:"15px", width:"15px"}}></Save>
                    <span style={{fontSize:"13px"}}>{props.messages.general_save}</span>
                </span>
            </div>
            {
                loading?
                <div style={{marginTop:"100px"}}>
                    <CircleLoader loading={true}></CircleLoader>
                </div>:
                <table className="virtual-participation-table">
                    <thead>
                        <tr>
                            <th>{props.messages.tableofcontents_number}</th>
                            <th>{props.messages.tableofcontents_title}</th>
                            <th></th>
                            <th>{props.messages.tableofcontents_usesEditions}</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            currentEdition?
                                mapEditionToTable()
                            :null
                        }
                    </tbody>
                </table>
            }
            

        </div>
    )
}

export default Recommendation