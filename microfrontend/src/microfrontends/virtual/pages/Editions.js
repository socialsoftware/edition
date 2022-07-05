import React, { useEffect, useRef, useState } from 'react'
import ReactTooltip from 'react-tooltip';
import info from '../../../resources/assets/information.svg'
import plus from '../../../resources/assets/plus_white.png'
import edit from '../../../resources/assets/edit.svg'
import eye from '../../../resources/assets/eye-fill.svg'
import Select from 'react-select'
import { getAllEditions, submitParticipation, cancelParticipation,
        createEdition, getPublicAllEditions } from '../../../util/API/VirtualAPI';
import {Collapse} from 'react-collapse';
import { Link, useHistory } from 'react-router-dom';
import { connect } from 'react-redux';
import CircleLoader from "react-spinners/RotateLoader";
import he from 'he'


const Editions = (props) => {

    const [collapse, setCollapse] = useState(false)
    const [type, setType] = useState("true")
    const [access, setAccess] = useState("no")
    const [editionOptions, setEditionOptions] = useState([{ value: 'no', label: props.messages.tableofcontents_usesEdition }])
    const [virtualEditions, setVirtualEditions] = useState([])
    const [sigla, setSigla] = useState("")
    const [title, setTitle] = useState("")
    const history = useHistory()
    const textSigla = useRef(null);
    const textTitle = useRef(null);
    const inputNo = useRef(null);
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        var mounted = true

        if(props.isAuthenticated){
            getAllEditions()
                .then(res => {
                    if(mounted){
                        let arrayAux = [...editionOptions]
                        for(let el of res.data.expertEditions){
                            let aux = {
                                "value" : el.externalId,
                                "label" : el.editor 
                            }
                            arrayAux.push(aux)
                        }
                        let aux = {
                            "value" : res.data.archiveEditionExternalId,
                            "label" : "Arquivo do LdoD"
                        }
                        arrayAux.push(aux)
                        for(let el of res.data.virtualEditions){
                            let aux = {
                                "value" : el.externalId,
                                "label" : el.acronym 
                            }
                            if(!el.ldoDEdition) arrayAux.push(aux)
                        }
                        setLoading(false)
                        setEditionOptions(arrayAux)
                        organizeList(res.data.virtualEditions)
                    }
                    
            })
        }
        else{
            getPublicAllEditions()
                .then(res => {
                    if(mounted){
                        let arrayAux = [...editionOptions]
                        for(let el of res.data.expertEditions){
                            let aux = {
                                "value" : el.externalId,
                                "label" : el.editor 
                            }
                            arrayAux.push(aux)
                        }
                        let aux = {
                            "value" : res.data.archiveEditionExternalId,
                            "label" : "Arquivo do LdoD"
                        }
                        arrayAux.push(aux)
                        for(let el of res.data.virtualEditions){
                            let aux = {
                                "value" : el.externalId,
                                "label" : el.acronym 
                            }
                            if(!el.ldoDEdition) arrayAux.push(aux)
                        }
                        setLoading(false)
                        setEditionOptions(arrayAux)
                        organizeList(res.data.virtualEditions)
                    }
                    
                })
            }
        return function cleanup() {
            mounted = false
            }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [props.selectedVEAcr])

    const organizeList = (original) => {
        let sorted = []
        for(let el of original){
            if(props.selectedVEAcr.includes(el.acronym) || el.member){
                sorted.push(el)
            }
        }
        for(let el of sorted){
            original.splice(original.indexOf(el), 1)
        }
        for(let el of sorted){
            if(props.selectedVEAcr.includes(el.acronym)){
                sorted.splice(sorted.indexOf(el), 1)
                sorted.unshift(el)
            }
        }
        setVirtualEditions(sorted.concat(original))
    }

    const customStyles = {
        option: (provided, state) => ({
          ...provided,
          color: state.isSelected ? '#fff' : '#555',
          textAlign:"left",
          padding:"5px"
        }),
        singleValue: (provided, state) => ({
            ...provided,
            color: "#555"
        })
    }

    const customStylesVirtual = {
        option: (provided, state) => ({
          ...provided,
          color: state.isSelected ? '#fff' : '#555',
          textAlign:"left",
          width:"200px",
          padding:"5px"
        }),
        singleValue: (provided, state) => ({
            ...provided,
            color: "#555"
        }),
        menu: (provided, state) => ({
            ...provided,
            width:"200px"
          }),
        control: (provided, state) => ({
            ...provided,
            width:"200px"
          }),
    }

    const createType = [
        { value: 'true', label: props.messages.general_public },
        { value: 'false', label: props.messages.general_private },
    ]

    const handleSubmit = (externalId) => {
        submitParticipation(externalId)
            .then(res => {
                organizeList(res.data.virtualEditions)
            })
    }

    
    const handleCancel = (externalId) => {
        cancelParticipation(externalId)
            .then(res => {
                organizeList(res.data.virtualEditions)
            })
    }

    const createVirtualEdition = () => {
        if(props.isAuthenticated){
            if(sigla === "") textSigla.current.focus()
            else if(title === "") textTitle.current.focus()
            else if(access === "no") inputNo.current.focus()
            else createEdition(sigla, title, type, access)
                    .then(res => {
                        organizeList(res.data.virtualEditions)
                    })
                    .catch(res => {
                        alert("Esta sigla já existe")
                        textSigla.current.focus()
                    })
        }
        else{
            history.push("/auth/signin")
        }
        
    }

    const mapTableToView = () => {
        return virtualEditions.map((edition, i) => {
            if((edition.public && !edition.ldoDEdition) || edition.member){
                return(
                    <tr key={i}>
                        <td>
                            <input type="checkbox" checked={props.selectedVEAcr.includes(edition.acronym)} onChange={() => {
                                    props.handleSelected(edition.acronym)
                                }}>
                            </input>
                                
                        </td>
                        <td style={{textAlign:"left"}} >
                            <p>{edition.acronym}</p>
                        </td>
                        <td style={{textAlign:"left"}} >
                            <Link className="virtual-link" to={`/edition/acronym/${edition.acronym}`}>{he.decode(edition.title)}</Link>
                        </td>
                        <td style={{textAlign:"center"}}>
                            <p>{edition.date}</p>
                        </td>
                        <td>
                            {edition.public?
                                <p>{props.messages.general_public}</p>    
                                :<p>{props.messages.general_private}</p>    
                            }
                        </td>
                        <td>
                            {edition.member?
                                <Link className="virtual-link" to={`/virtual/virtualeditions/restricted/manage/${edition.externalId}`}>
                                    <span className="virtual-gerir-button">
                                        <img alt="img" src={edit} style={{height:"13px", width:"13px", marginRight:"2px"}}></img>
                                        <span>{props.messages.general_manage}</span>
                                    </span>
                                </Link>
                                :null
                            }
                        </td>
                        <td>
                            {edition.member || !props.isAuthenticated?
                                null
                                :!edition.pending?
                                    <div className="virtual-submit-button" onClick={() => handleSubmit(edition.externalId)}>
                                        <img alt="img" src={plus} style={{height:"13px", width:"13px", marginRight:"2px"}}></img>
                                        <span>{props.messages.general_submit}</span>
                                    </div>
                                :edition.pending?
                                    <span className="virtual-cancel-button" onClick={() => handleCancel(edition.externalId)}>
                                        <span style={{color:"white", fontWeight:800, fontSize:"15px", marginRight:"4px"}}>x</span>
                                        <span>{props.messages.general_cancel}</span>
                                    </span>
                                :null
                            }
                        </td>
                    </tr>
                )
            }
            else return null
        })
    }


    return (
        <div className="virtual-editions">
        {
            loading?
                <div style={{marginTop:"250px"}}>
                    <CircleLoader loading={loading}></CircleLoader>
                </div>
            :   
            <div>
                <div className="virtual-body-title">
                    <p>{props.messages.virtual_editions}</p>
                    <img alt="img" src={info} data-tip={props.messages.info_virtualeditions_authenticate}
                        className="virtual-info" style={{width:"20px", padding:"0", marginLeft:"5px"}}></img>
                </div>
                <div style={{display:"flex", justifyContent:"flex-end"}}>
                    <span className="virtual-create-button" data-tip={props.messages.virtualedition_tt_create} onClick={() => setCollapse(!collapse)}>
                        <img alt="img" src={plus} style={{width:"15px", height:"15px"}}></img>
                        <p style={{marginLeft:"5px"}}>{props.messages.virtualeditionlist_createtitle}</p>
                    </span>
                </div>
                
                <div style={{marginTop:"40px"}}>
                    <Collapse isOpened={collapse}>
                        <div className="virtual-collapse">
                            <div className="virtual-input-group">
                                <p className="virtual-input-addon">LdoD-</p>
                                <input
                                    ref={textSigla}
                                    data-tip={props.messages.virtualedition_tt_acronym}
                                    className="virtual-input-input"
                                    placeholder={props.messages.virtualeditionlist_acronym}
                                    style={{borderBottomLeftRadius:"0", borderTopLeftRadius:"0", borderLeft:"none"}}
                                    value={sigla}
                                    onChange={e => setSigla(e.target.value)}/>
                            </div>
                            <input
                                ref={textTitle}
                                data-tip={props.messages.virtualedition_tt_title}
                                className="virtual-input-input"
                                placeholder={props.messages.virtualeditionlist_name}
                                style={{marginLeft:"10px"}}
                                value={title}
                                onChange={e => setTitle(e.target.value)}
                                />
                            
                            <Select 
                                styles={customStyles}
                                value={createType.filter(option => option.value === type)}
                                onChange={value => setType(value.value)}
                                options={createType} className="virtual-form-control virtual-select"/>
                            <Select 
                                ref={inputNo}
                                styles={customStylesVirtual}
                                value={editionOptions.filter(option => option.value === access)}
                                onChange={value => setAccess(value.value)}
                                options={editionOptions} className="virtual-form-control virtual-select"/>

                            <span className="virtual-create-submit-button" onClick={() => createVirtualEdition()}>
                                <img alt="img" src={edit} style={{width:"20px", height:"20px", marginRight:"5px"}}></img>
                                <p>{props.messages.general_create}</p>
                            </span>
                        </div>
                    </Collapse>

                    <table className="virtual-editions-table">
                        <thead>
                            <tr>
                                <th>
                                    <span>
                                        <img alt="img" data-tip={props.messages.virtualedition_tt_select} src={eye} style={{height:"15px", width:"15px", padding:"0 10px"}}></img>
                                    </span>
                                </th>
                                <th>
                                    <span style={{textAlign:"left"}} data-tip={props.messages.virtualedition_tt_acronym}>
                                        <p>{props.messages.virtualeditionlist_acronym}</p>
                                    </span>
                                    </th>
                                <th>
                                    <span style={{textAlign:"left"}} data-tip={props.messages.virtualedition_tt_title}>
                                        <p>{props.messages.virtualeditionlist_name}</p>
                                    </span></th>
                                <th>
                                    <span data-tip={props.messages.virtualedition_tt_date}>
                                        {props.messages.general_date}
                                    </span>
                                </th>
                                <th>
                                    <span data-tip={props.messages.virtualedition_tt_access}>
                                        {props.messages.general_access}
                                    </span>
                                </th>
                                <th>
                                    <span data-tip={props.messages.virtualedition_tt_manage}>
                                        {props.messages.general_manage}
                                    </span>
                                </th>
                                <th>
                                    <span data-tip={props.messages.virtualedition_tt_join}>
                                        {props.messages.general_join}
                                    </span>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                        {
                            mapTableToView()
                        }
                    </tbody>
                </table>
                </div>
                
                <ReactTooltip backgroundColor="#000" textColor="#fff" border={true} borderColor="#000" className="virtual-tooltip" place="bottom" effect="solid"/>
            </div>
            
        }
        
            
        </div>
    )
}

const mapStateToProps = (state) => {
    return {
        selectedVEAcr: state.selectedVEAcr,
        user: state.user
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        handleSelected: (acronym) => dispatch({ type: 'UPDATE_SELECTED', payload: acronym }),
    }
}

export default connect(mapStateToProps,mapDispatchToProps)(Editions)
