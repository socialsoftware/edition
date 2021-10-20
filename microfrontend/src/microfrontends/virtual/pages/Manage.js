import React, { useEffect, useRef, useState } from 'react'
import { useHistory, useLocation } from 'react-router-dom'
import {ReactComponent as Pencil} from '../../../resources/assets/pencil-fill.svg'
import {ReactComponent as Play} from '../../../resources/assets/play-circle.svg'
import {ReactComponent as Tags} from '../../../resources/assets/tags-fill.svg'
import {ReactComponent as Square} from '../../../resources/assets/pencil-square.svg'
import {ReactComponent as Wrench} from '../../../resources/assets/wrench.svg'
import {ReactComponent as Person} from '../../../resources/assets/person-fill.svg'
import {ReactComponent as Check} from '../../../resources/assets/check.svg'
import {ReactComponent as X} from '../../../resources/assets/x.svg'
import trash from '../../../resources/assets/trash-fill.svg'
import plus from '../../../resources/assets/plus_white.png'
import { submitParticipation, cancelParticipation, getEditInfo, getManagePage, deleteVirtualEdition } from '../../../util/API/VirtualAPI';
import Select from 'react-select'
import ReactTooltip from 'react-tooltip'
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import {Collapse} from 'react-collapse';
import he from 'he'
import { connect } from 'react-redux';

const Manage = (props) => {

    const [collapse, setCollapse] = useState(false)
    const location = useLocation()
    const history = useHistory()
    const [data, setData] = useState(null)
    const [sigla, setSigla] = useState("")
    const [title, setTitle] = useState("")
    const [date, setDate] = useState("")
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [allCountries, setAllCountries] = useState(false)
    const [countries, updateCountries] = useState({
        "Portugal":false,
        "Brazil":false,
        "Spain":false,
        "United Kingdom":false,
        "United States":false,
        "Lebanon":false,
        "Angola":false,
        "Mozambique":false
    }  
    )
    const [type, setType] = useState("true")
    const [owner, setOwner] = useState("false")
    const [taxonomy, setTaxonomy] = useState("false")
    const [vocabulary, setVocabulary] = useState("true")
    const [media, setMedia] = useState("noMediaSource")
    const [synopsis, setSynopsis] = useState("")
    const [frequency, setFrequency] = useState("")
    const textSigla = useRef(null);
    const textTitle = useRef(null);

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
    
    const customStylesLong = {
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

    const ownersOptions = [
        { value: 'true', label: props.messages.taxonomy_manage_members },
        { value: 'false', label: props.messages.taxonomy_manage_owners },
    ]

    const taxonomyOptions = [
        { value: 'true', label: props.messages.taxonomy_annotation_all },
        { value: 'false', label: props.messages.taxonomy_annotation_members },
    ]

    const vocabularyOptions = [
        { value: 'true', label: props.messages.taxonomy_vocabulary_open },
        { value: 'false', label: props.messages.taxonomy_vocabulary_closed },
    ]

    const mediaOptions = [
        { value: 'Twitter', label: props.messages.criteira_mediasource_twitter },
        { value: 'noMediaSource', label: props.messages.criteira_mediasource_nomediasource },
    ]

    useEffect(() => {
        var path = location.pathname.split('/')
        let mounted = true
        getManagePage(path[5])
            .then(res => {
                if(mounted){
                    console.log(res.data);
                    updateData(res.data)
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

    const handleSubmit = (externalId) => {
        submitParticipation(externalId)
    }

    const handleCancel = (externalId) => {
        cancelParticipation(externalId)
    }

    const handleDelete = () => {
        deleteVirtualEdition(data.externalId)
            .then(() => {
                if(props.selectedVEAcr.includes(data.acronym)){
                    props.updateSelected(data.acronym)
                }
                history.push("/virtual/virtualeditions")
            })
            .catch(() => {
                alert("Error deleting edition! Please try login out and in")
                history.push("/")
            })
    }

    const countrySelectedHandler = (country) => {
        let aux = {...countries}
        aux[country] = !aux[country]
        updateCountries(aux)
        setAllCountries(false)
    }

    const mapCountriesToEdit = () => {
        return Object.keys(countries).map((country, i) => {
            return (
                    <span key={i} style={{display:"flex", alignItems:"center"}}>
                        <input checked={countries[country]} onChange={() => countrySelectedHandler(country)} type="checkbox"></input>
                        <p style={{fontSize:"14px"}}>{country}</p>
                    </span>
            )
        })
    }

    const getEditInfoHandler = () => {
        var aux = ""
        for(let key of Object.keys(countries)){
            if(countries[key]){
                aux += `,${key}`
            }
        }
        let obj = {
            acronym: sigla,
            title: title,
            synopsis: synopsis?synopsis:"",
            pub: type,
            vocabulary: vocabulary,
            management: owner,
            annotation: taxonomy,
            mediasource: media,
            begindate: startDate?formatDate(startDate):"",
            enddate: endDate?formatDate(endDate):"",
            geolocation: aux,
            frequency: frequency
        }
        getEditInfo(data.externalId, obj).then(res => {
            updateData(res.data)
            setCollapse(false)
        })
            
    }

    const updateData = (data) => {
        setData(data)
        if(data.beginDate) {
            setStartDate(new Date(`${data.beginDate.year}-${data.beginDate.monthOfYear}-${data.beginDate.dayOfMonth}`))
        }
        if(data.endDate) {
            setEndDate(new Date(`${data.endDate.year}-${data.endDate.monthOfYear}-${data.endDate.dayOfMonth}`))
        }
        setTitle(data.title)
        let aux = data.acronym
        setSigla(aux.slice(5, aux.length))
        setOwner(data.openManagement.toString())
        setTaxonomy(data.openAnnotation.toString())
        setVocabulary(data.openVocabulary.toString())
        setType(data.public.toString())
        setDate(data.date)
        setMedia(data.mediaSource)
        setSynopsis(data.synopsis)
        setFrequency(data.frequency<=0?1:data.frequency)
        var auxCountries = {...countries}
        if(data.containsEveryCountry){
            for(let country of Object.keys(countries)){
                auxCountries[country] = true
            }
            updateCountries(auxCountries)
        }
        else if(data.countryContains){
            for(let el of data.countryContains){
                auxCountries[el.country] = el.contains
            }
            updateCountries(auxCountries)
        }
    }

    const updateAllCountries = () => {
        var aux = {...countries}
        if(!allCountries){
            for(let country of Object.keys(countries)){
                aux[country] = true
            }
        }
        else{
            for(let country of Object.keys(countries)){
                aux[country] = false
            }
        }
        setAllCountries(!allCountries)
        updateCountries(aux)
    }

    function formatDate(date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();
    
        if (month.length < 2) 
            month = '0' + month;
        if (day.length < 2) 
            day = '0' + day;
    
        return [year, month, day].join('-');
    }

    return (
        <div className="virtual-editions">
            
            <span className="virtual-body-title" style={{display:"flex"}}>
                <p >{data?he.decode(data.title):null}</p>
                {
                    data?data.admin?
                    <Pencil onClick={() => setCollapse(!collapse)} className="virtual-pencil"></Pencil>
                    :null:null
                }
            </span>
            <Collapse isOpened={collapse}>
                <div style={{display:"flex", justifyContent:"flex-end", marginBottom:"-20px", marginTop:"20px"}}>
                    <span onClick={() => getEditInfoHandler()} className="virtual-edit-submit">
                        <Check fill="#fff"></Check>
                    </span>
                    <span onClick={() => setCollapse(false)} className="virtual-edit-cancel">
                        <X></X>
                    </span>
                </div>
            <div className="virtual-edit">
                
                <div className="virtual-edit-row">
                    <div className="virtual-input-group">
                        <p style={{marginRight:"5px", fontSize:"14px"}}><strong>{props.messages.virtualeditionlist_acronym}</strong></p>
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

                    <div className="virtual-input-group">
                        <p style={{marginRight:"5px", fontSize:"14px"}}><strong>{props.messages.virtualeditionlist_name}</strong></p>
                        <input
                            ref={textTitle}
                            data-tip={props.messages.virtualedition_tt_title}
                            className="virtual-input-input"
                            placeholder={props.messages.virtualeditionlist_name}
                            value={title}
                            onChange={e => setTitle(e.target.value)}/>
                    </div>
                    <div className="virtual-input-group">
                    <p style={{marginRight:"5px", fontSize:"14px"}}><strong>{props.messages.general_date}</strong></p>
                        <input
                            ref={textSigla}
                            data-tip={props.messages.virtualedition_tt_date}
                            className="virtual-input-input"
                            value={date}
                            onChange={e => setSigla(e.target.value)}
                            disabled={true}
                            style={{backgroundColor:"#eee", cursor:"not-allowed"}}/>
                    </div>
                    <div className="virtual-input-group">
                        <p style={{marginRight:"5px", fontSize:"14px"}}><strong>{props.messages.general_access}</strong></p>
                        <Select 
                            styles={customStyles}
                            value={createType.filter(option => option.value === type)}
                            onChange={value => setType(value.value)}
                            options={createType} className="virtual-form-control virtual-select"/>
                    </div>
                </div>
                <div className="virtual-edit-row" style={{marginTop:"30px"}}>
                    <div className="virtual-input-group">
                        <p style={{marginRight:"5px", fontSize:"14px"}}><strong>{props.messages.taxonomy_manage}</strong></p>
                        <Select 
                            styles={customStylesLong}
                            value={ownersOptions.filter(option => option.value === owner)}
                            onChange={value => setOwner(value.value)}
                            options={ownersOptions} className="virtual-form-control virtual-select"/>
                    </div>
                    <div className="virtual-input-group">
                        <p style={{marginRight:"5px", fontSize:"14px"}}><strong>{props.messages.taxonomy_annotation}</strong></p>
                        <Select 
                            styles={customStylesLong}
                            value={taxonomyOptions.filter(option => option.value === taxonomy)}
                            onChange={value => setTaxonomy(value.value)}
                            options={taxonomyOptions} className="virtual-form-control virtual-select"/>
                    </div>
                    <div className="virtual-input-group">
                        <p style={{marginRight:"5px", fontSize:"14px"}}><strong>{props.messages.taxonomy_vocabulary}</strong></p>
                        <Select 
                            styles={customStyles}
                            value={vocabularyOptions.filter(option => option.value === vocabulary)}
                            onChange={value => setVocabulary(value.value)}
                            options={vocabularyOptions} className="virtual-form-control virtual-select"/>
                    </div>
                </div>
                <div className="virtual-edit-row" style={{marginTop:"30px"}}>
                    <div className="virtual-input-group">
                        <p style={{marginRight:"5px", fontSize:"14px"}}><strong>{props.messages.criteria_mediasource}</strong></p>
                        <Select 
                            styles={customStyles}
                            value={mediaOptions.filter(option => option.value === media)}
                            onChange={value => setMedia(value.value)}
                            options={mediaOptions} className="virtual-form-control virtual-select"/>
                    </div>
                    <div className="virtual-input-group">
                        <p style={{marginRight:"5px", fontSize:"14px"}}><strong>{props.messages.criteira_timewindow_begindate}</strong></p>
                        <DatePicker minDate={new Date("2018-01-01")} selected={startDate} onChange={(date) => setStartDate(date)} />
                    </div>
                    <div className="virtual-input-group">
                        <p style={{marginRight:"5px", fontSize:"14px"}}><strong>{props.messages.criteira_timewindow_enddate}</strong></p>
                        <DatePicker minDate={new Date("2018-08-30")} selected={endDate} onChange={(date) => setEndDate(date)} />
                    </div>
                </div>
    
                <div className="virtual-edit-row" style={{marginTop:"30px"}}>
                    <div>
                        <p style={{fontSize:"14px"}}><strong>{props.messages.criteria_geolocation}</strong></p>
                        <span style={{display:"flex", alignItems:"center"}}>
                            <input checked={allCountries} onChange={() => updateAllCountries()} type="checkbox"></input>
                            <p style={{fontSize:"14px"}}>{props.messages.criteria_geolocation_everycountry}</p>
                        </span>
                        {mapCountriesToEdit()}
                    </div>
                    <div style={{display:"flex", alignItems:"center"}}>
                        <p style={{marginRight:"5px"}}>{props.messages.virtualedition_synopsis}</p>
                        <textarea className="virtual-textarea" rows={9} cols={110} maxLength={1500} value={synopsis} onChange={e => setSynopsis(e.target.value)}>
                        </textarea>
                    </div>
                </div>
                <div style={{marginTop:"20px"}}>
                    <p style={{marginRight:"5px", fontSize:"14px"}}><strong>{props.messages.criteria_frequency}</strong></p>
                    <input
                        type="number"
                        data-tip={props.messages.criteria_frequency_manage}
                        className="virtual-input-input"
                        value={frequency}
                        onChange={e => setFrequency(e.target.value)}
                        min="1"
                        />
                </div>
            </div>
            </Collapse>
            <div className="virtual-manage-flex">
                {data?data.member?
                    <div className="virtual-manage-flex-option">
                        <Play className="virtual-manage-flex-svg"></Play>
                        <p className="virtual-manage-flex-title">{props.messages.general_classificationGame}</p>
                    </div>
                    :null:null
                }
                {data?data.member?
                    <div className="virtual-manage-flex-option" onClick={() => history.push(`/virtual/virtualeditions/restricted/taxonomy/${data.externalId}`)}>
                        <Tags className="virtual-manage-flex-svg"></Tags>
                        <p className="virtual-manage-flex-title">{props.messages.general_taxonomy}</p>
                    </div>
                    :null:null
                }
                {data?data.member?
                    <div className="virtual-manage-flex-option" onClick={() => history.push(`/virtual/virtualeditions/restricted/manual/${data.externalId}`)}>
                        <Square className="virtual-manage-flex-svg"></Square>
                        <p className="virtual-manage-flex-title">{props.messages.general_sort_manual}</p>
                    </div>
                    :null:null
                }
                {data?data.member?
                    <div className="virtual-manage-flex-option" onClick={() => history.push(`/virtual/virtualeditions/restricted/recommendation/${data.externalId}`)}>
                        <Wrench className="virtual-manage-flex-svg"></Wrench>
                        <p className="virtual-manage-flex-title">{props.messages.general_sort_automatic}</p>
                    </div>
                    :null:null
                }
                {data?data.member?
                    <div className="virtual-manage-flex-option" onClick={() => history.push(`/virtual/virtualeditions/restricted/participants/${data.externalId}`)}>
                        <Person className="virtual-manage-flex-svg"></Person>
                        <p className="virtual-manage-flex-title">{props.messages.general_editors}</p>
                    </div>
                    :props.isAuthenticated && !data.pending?
                        <div className="virtual-submit-button" onClick={() => handleSubmit(data.externalId)}>
                            <img alt="imgPlus" src={plus} style={{height:"13px", width:"13px", marginRight:"2px"}}></img>
                            <span>{props.messages.general_submit}</span>
                        </div>
                    :props.isAuthenticated && data.pending?
                        <span className="virtual-cancel-button" onClick={() => handleCancel(data.externalId)}>
                            <span style={{color:"white", fontWeight:800, fontSize:"15px", marginRight:"4px"}}>x</span>
                            <span>{props.messages.general_cancel}</span>
                        </span>
                    :null
                    :null
                }
                {data?data.admin && !data.ldoDEdition?
                    <div className="virtual-manage-flex-option">
                        <img alt="imgTrash" src={trash} className="virtual-manage-flex-svg" onClick={() => handleDelete()}></img>
                    </div>
                :null:null
                }
                
                <ReactTooltip backgroundColor="#000" textColor="#fff" border={true} borderColor="#000" className="virtual-tooltip" place="bottom" effect="solid"/>

            </div>
            
        </div>
    )
}


const mapStateToProps = (state) => {
    return {
        selectedVEAcr: state.selectedVEAcr,
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        updateSelected: (acronym) => dispatch({ type: 'REMOVE_FROM_SELECTED', payload: acronym }),
    }
}

export default connect(mapStateToProps,mapDispatchToProps)(Manage)