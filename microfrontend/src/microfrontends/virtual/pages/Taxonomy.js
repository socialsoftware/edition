import React, { useEffect, useState } from 'react'
import { Link, useHistory, useLocation } from 'react-router-dom'
import { getTaxonomyData, createVirtualCategory, mergeOrDeleteVirtualCategory, 
    generateTopics, addGeneratedTopics } from '../../../util/API/VirtualAPI';
import {ReactComponent as LeftArrow} from '../../../resources/assets/caret-left-fill.svg'
import {ReactComponent as List} from '../../../resources/assets/card-list.svg'
import {ReactComponent as Wrench} from '../../../resources/assets/wrench.svg'
import plus from '../../../resources/assets/plus_white.png'
import Select from 'react-select'
import CircleLoader from "react-spinners/RotateLoader";
import he from 'he'

const Taxonomy = (props) => {

    const history = useHistory()
    const location = useLocation()
    const [data, setData] = useState(null)
    const [categoryName, setCategoryName] = useState("")
    const [selected, setSelected] = useState([])
    const [errorCategory, setErrorCategory] = useState(false)
    const [showGerar, setShowGerar] = useState(false)
    const [searchLoading, setSearchLoading] = useState(false)
    const [topics, setTopics] = useState(null)
    const [words, setWords] = useState(null)
    const [threshold , setThreshold] = useState(null)
    const [iterations, setIterations] = useState(null)
    const [newTopicDto, setNewTopicDto] = useState(null)
    const [errors, setErrors] = useState(false)
    const [addRepeated, setAddRepeated] = useState(false)


    useEffect(() => {
        var path = location.pathname.split('/')
        let mounted = true
        getTaxonomyData(path[5])
            .then(res => {
                if(mounted) setData(res.data)
            })
            .catch((err) => {
                alert("Acesso Negado")
                history.push("/")
            })

        return function cleanup() {
            mounted = false
        }
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

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
        }),
    }

    const options = [
        { value: 'juntar', label: props.messages.general_merge },
        { value: 'apagar', label: props.messages.general_delete },
    ]

    const handleAddition = () => {
        setErrorCategory(false)
        createVirtualCategory(data.externalId, categoryName)
            .then(res => {
                if(res.data!=="") setData(res.data)
                else setErrorCategory(true)
                setCategoryName("")
            })
            .catch((err) => {
                console.log(err);
            })
    }

    const triggerOnChange = (option) => {
        if(selected !== []){
            if(option==="apagar"){
                mergeOrDeleteVirtualCategory(data.taxonomy.externalId, "delete", data.externalId, selected).then(res => {
                    setData(res.data)
                    setSelected([])
                })
            }
            else if(option==="juntar"){
                mergeOrDeleteVirtualCategory(data.taxonomy.externalId, "merge", data.externalId, selected).then(res => {
                    setData(res.data)
                    setSelected([])
                })
            }
        }
    }

    const addSelected = (val) => {
        let aux = [...selected]

        if(!aux.includes(val)) aux.push(val)
        else{
            aux.splice(aux.indexOf(val), 1)
        }
        setSelected(aux)
    }

    const mapIntersToTable = (category) => {
        return category.sortedIntersShortList.map((inter, i) => {
            return (
                <p key={i} style={{minWidth:"250px"}}>
                    <Link className="virtual-link" to={`/virtual/virtualeditions/restricted/fraginter/${inter.externalId}`}>{inter.title}</Link>

                </p>
            )
        })
    }

    const mapCategoriesToTable = () => {
        return data.taxonomy.categorySet.map((cat,i) => {
            return (
                <tr key={i}>
                    <td className="virtual-td-top">
                        <Link className="virtual-link" to={`/virtual/virtualeditions/restricted/category/${cat.externalId}`}>{cat.normalName}</Link>
                    </td>
                    <td >
                        {mapIntersToTable(cat)}
                    </td>
                    <td className="virtual-td-top">
                        {
                            data?data.taxonomy.canManipulate?
                                <input type="checkbox" checked={selected.includes(cat.externalId)} onChange={() => addSelected(cat.externalId)}> 
                                    
                                </input>
                            :null:null
                        }
                    </td>
                </tr>
            )
            
        })
    }

    useEffect(() => {
        if(showGerar){
            document.body.style.overflow = 'hidden';
        }
        else{
            document.body.style.overflow = 'scroll';
        }
    }, [showGerar])

    const gerarHandler = () => {
        setErrors(false)
        let flag = true
        if (topics === null || topics === "") {
    		setErrors(true)
            flag = false
        } else if (isNaN(topics) || topics < 1 || topics > 100) {
            setErrors(true)
            flag = false
        }
        if (words === null || words === "") {
            setErrors(true)
            flag = false
        } else if (isNaN(words) || words < 1 || words > 10) {
            setErrors(true)
            flag = false
        }
        if (threshold === null || threshold === "") {
            setErrors(true)
            flag = false
        } else if (isNaN(threshold) || threshold < 0 || threshold > 100) {
            setErrors(true)
            flag = false
        }
        if (iterations === null || iterations === "") {
            setErrors(true)
            flag = false
        } else if (isNaN(iterations) || iterations < 1) {
            setErrors(true)
            flag = false
        }
        if(flag){
            setSearchLoading(true)
            generateTopics(data.externalId, topics, words, threshold, iterations)
                .then(res => {
                    setSearchLoading(false)
                    setNewTopicDto(res.data)
                })
                .catch(err => {
                    setSearchLoading(false)
                })
        }
        
        
    }

    const mapIntersToParagraph = (topic) => {
        let text = ""
        for(let el of topic.inters){
            text += `${el.title} (${el.percentage}) `
        }
        return <span className="virtual-helvetica">{text}</span>
    }

    const mapTopicsToArea = () => {
        return newTopicDto.topics.map((topic, i) => {
            return (
                <tr key={i}>
                    <td><strong>{topic.name}</strong></td>
                    <td>
                        {mapIntersToParagraph(topic)}
                    </td>
                </tr>
            )
        })
    }

    const addTopicsToEdition = () => {
        var flag = true
        for(let el of newTopicDto.topics){
            for(let el1 of data.taxonomy.categorySet){
                if(el.name === el1.name){
                    setAddRepeated(true)
                    flag = false
                    break
                }
            }
        }
        if(flag){
            addGeneratedTopics(data.externalId, newTopicDto)
                .then(res => {
                    if(res.data !== ""){
                        setData(res.data)
                    }
                    else{
                        setAddRepeated(true)
                    }
                })
        }
        setShowGerar(false)
    }
    
    return (
        <div>
            {addRepeated?
                <p className="virtual-error">Erro (Tópicos repetidos)</p>
                :null
            }
            {showGerar?
                <div className="virtual-backdrop" onClick={() => setShowGerar(false)}>
                </div>
                :null
            }
            {
            showGerar?
            <div className="virtual-search-div" style={{top:"50px"}}>
                <div className="virtual-search-title">
                    <p className="virtual-search-title-text">{props.messages.topics_generate_long}</p>
                    <p className="virtual-search-title-x" onClick={() => setShowGerar(false)}>x</p>
                </div>
                {errors?
                    <p style={{color:"red"}}>Porfavor verifique as entradas</p>
                    :null
                    }
                <div className="virtual-search" style={{width:"80%", marginLeft:"10%"}}>
                    <input className="virtual-input-input" style={{padding:"0 12px", width:"100px"}}
                        placeholder={props.messages.general_taxonomies_number_topics}
                        value={topics}
                        onChange={e => setTopics(e.target.value)}>
                    </input>
                    <input className="virtual-input-input" style={{padding:"0 12px", width:"100px"}}
                        placeholder={props.messages.general_taxonomies_number_words}
                        value={words}
                        onChange={e => setWords(e.target.value)}>
                    </input>
                    <input className="virtual-input-input" style={{padding:"0 12px", width:"100px"}}
                        placeholder={props.messages.general_taxonomies_threshold_categories}
                        value={threshold}
                        onChange={e => setThreshold(e.target.value)}>
                    </input>
                    <input className="virtual-input-input" style={{padding:"0 12px", width:"100px"}}
                        placeholder={props.messages.general_taxonomies_number_iterations}
                        value={iterations}
                        onChange={e => setIterations(e.target.value)}>
                    </input>
                    <div onClick={() => gerarHandler()} style={{height:"15px"}} className="form-control virtual-add-category-button">
                        <Wrench style={{height:"15px", width:"15px", marginRight:"5px"}}></Wrench>
                        <p>{props.messages.general_generate}</p>
                    </div>
                    
                </div>
                {
                        searchLoading?
                        <div className="virtual-search-loading">
                            <CircleLoader loading={searchLoading}></CircleLoader>
                        </div>
                        :null
                    }
                {
                    newTopicDto?
                    <div style={{padding:"20px 100px"}}>
                        <table className="virtual-participation-table">
                            <thead>
                                <tr>
                                    <th>{props.messages.general_taxonomies_number_topics}</th>
                                    <th>{props.messages.fragments}</th>
                                </tr>
                            </thead>
                            <tbody>
                                {mapTopicsToArea()}
                            </tbody>
                        </table>
                        <div  onClick={() => addTopicsToEdition()} style={{height:"15px", margin:"20px auto"}} className="form-control virtual-add-category-button">
                            <p>{props.messages.general_add}</p>
                        </div>
                    </div>
                    
                    :null
                }
                
            </div>
            :null
            }
             <div className="virtual-editions">
             <button className="virtual-back-button" onClick={() => history.goBack()}>
                <LeftArrow></LeftArrow>
                <p>{props.messages.general_back}</p>
            </button>
            <span className="virtual-body-title">
                <p>{data?he.decode(data.title):null} - {props.messages.general_taxonomy}</p>
            </span>
            <div style={{display:"flex", width:"100%", justifyContent:"flex-end"}}>
                <div className="virtual-taxonomy-public">
                    <p style={{color:"#333"}}>{props.messages.general_public_pages}:</p>
                    <span className="virtual-taxonomy-span-flex" onClick={() => history.push(`/edition/acronym/${data?data.acronym:null}`)}>
                        <List className="virtual-list-icon"></List>
                        <p className="virtual-taxonomy-edition-text">{props.messages.general_edition}</p>
                    </span>
                </div>
            </div>
            {
                data?data.taxonomy.canManipulate?
                <div style={{display:"flex", justifyContent:"space-between", marginTop:"20px"}}>
                    <div>
                        {
                            errorCategory?
                                <p style={{color:"red"}}>Erro a adicionar categoria (Repetido)</p>
                            :null
                        }
                        <input className="virtual-input-input" style={{padding:"0 12px", width:"145px"}}
                                placeholder={props.messages.general_name}
                                value={categoryName}
                                onChange={e => setCategoryName(e.target.value)}>
                        </input>
                        <span className="virtual-add-category-button" style={{marginTop:"5px"}} onClick={() => handleAddition()}>
                            <img alt="imgPlus" src={plus} style={{height:"13px", width:"13px", marginRight:"2px"}}></img>
                            <p>{props.messages.category_add}</p>
                        </span>
                    </div>
                    <span className="virtual-add-category-button" style={{maxHeight:"20px"}} onClick={() => {
                        window.scrollTo({ top: 0, behavior: 'smooth' })
                        setNewTopicDto(null)
                        setAddRepeated(false)
                        setShowGerar(true)}}
                        >
                        <img alt="imgPlus" src={plus} style={{height:"13px", width:"13px", marginRight:"2px",}}></img>
                        <p>{props.messages.topics_generate_short}</p>
                    </span>
                </div>
                :null:null
            }
            <table className="virtual-participation-table">
                <thead>
                    <tr>
                        <th>{props.messages.general_category}</th>
                        <th>{props.messages.fragments}</th>
                        <th style={{padding:"10px 0"}}>
                            {
                                data?data.taxonomy.canManipulate?
                                <Select 
                                    isSearchable={false}
                                    placeholder={props.messages.general_operation}
                                    styles={customStyles}
                                    onChange={value => triggerOnChange(value.value)}
                                    options={options} className="virtual-form-control virtual-select"/>
                                :null:null
                            }
                        </th>
                    </tr>
                </thead>
                <tbody>
                    {
                        data?
                            mapCategoriesToTable()
                        :null
                    }
                </tbody>
            </table>

        </div>
        </div>
       
    )
}

export default Taxonomy