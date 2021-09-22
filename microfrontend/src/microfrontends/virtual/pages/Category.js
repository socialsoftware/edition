import React, { useEffect, useState } from 'react'
import {ReactComponent as LeftArrow} from '../../../resources/assets/caret-left-fill.svg'
import {ReactComponent as List} from '../../../resources/assets/card-list.svg'
import {ReactComponent as Tag} from '../../../resources/assets/tag_fill.svg'
import {ReactComponent as Edit} from '../../../resources/assets/edit.svg'
import { Link, useHistory, useLocation } from 'react-router-dom'
import { getCategoryData, deleteVirtualCategory, updateVirtualCategory, extractCategories } from '../../../util/API/VirtualAPI';
import ReactTooltip from 'react-tooltip';
import he from 'he'

const Category = (props) => {

    const [data, setData] = useState(null)
    const history = useHistory()
    const location = useLocation()
    const [errorCategory] = useState(false)
    const [newCategoryName, setNewCategoryName] = useState("")
    const [showExtract, setShowExtract] = useState(false)
    const [selectedInters, setSelectedInters] = useState([])

    useEffect(() => {
        var path = location.pathname.split('/')
        let mounted = true
        getCategoryData(path[5])
            .then(res => {
                if(mounted){
                    console.log(res.data);
                    setData(res.data)
                    setNewCategoryName(res.data.name)
                }
            })
            .catch((err) => {
                console.log(err);
                //alert("Acesso Negado")
                //history.push("/")
            })
            return function cleanup() {
                mounted = false
            }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    useEffect(() => {
        if(showExtract){
            document.body.style.overflow = 'hidden';
        }
        else{
            document.body.style.overflow = 'scroll';
        }
    }, [showExtract])

    const handleUpdate = () => {
        if(newCategoryName !== ""){
            updateVirtualCategory(data.externalId, newCategoryName)
                .then(res => {
                    setData(res.data)
                })
        }
        
    }

    const deleteCategory = () => {
        deleteVirtualCategory(data.externalId)
            .then(res => {
                setData(res.data)
            })
    }

    const mapIntersToTable = () => {
        return data.sortedIntersShortList.map((inter, i) => {
            return <tr key={i}>
                <td>
                    <Link className="virtual-link" to={`/virtual/virtualeditions/restricted/fraginter/${inter.externalId}`}>{inter.title}</Link>
                </td>
            </tr>
        })
    }

    const handleSelect = (externalId) => {
        let aux = [...selectedInters]
        if(aux.includes(externalId)){
            aux.splice(aux.indexOf(externalId,1))
        }
        else{
            aux.push(externalId)
        }
        setSelectedInters(aux)
    }

    const mapIntersToExtract = () => {
        return data.sortedIntersShortList.map((inter, i) => {
            return (
                <tr key={i}>
                    <td>{inter.title}</td>
                    <td>
                        <input type="checkbox" checked={selectedInters.includes(inter.externalId)} onChange={() => handleSelect(inter.externalId)}></input>
                    </td>
                </tr>
            )
        })
    }

    const extractHandler = () => {
        if(selectedInters !== []){
            extractCategories(data.externalId, selectedInters)
                .then(res => {
                    history.push(`/virtual/virtualeditions/restricted/category/${res.data}`)
                    setShowExtract(false)
                })
        }
    }

    return (
        <div>
            {showExtract?
                <div className="virtual-backdrop" onClick={() => setShowExtract(false)}>
                </div>
                :null
            }
            {showExtract?
            <div className="virtual-search-div" style={{top:"50px"}}>
                <div className="virtual-search-title">
                    <p className="virtual-search-title-text">{props.messages.general_extract} {props.messages.general_category}: {data?data.name:null}</p>
                    <p className="virtual-search-title-x" onClick={() => setShowExtract(false)}>x</p>
                </div>
                <div style={{padding:"10px 20px"}}>
                    <table className="virtual-participation-table" >
                        <thead>
                            <tr>
                                <th>{props.messages.fragments}</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {data?
                                mapIntersToExtract()
                            :null}
                        </tbody>
                    </table>
                </div>
                
                <div style={{borderBottom:"1px solid #ddd", paddingBottom:"25px"}}>
                    <span className="virtual-add-category-button" style={{margin:"0 auto", marginTop:"45px"}} onClick={() => extractHandler()}>
                        <p>{props.messages.general_extract}</p>
                    </span>
                </div>
                <div style={{display:"flex", justifyContent:"flex-end", paddingBottom:"20px"}}>
                    <span className="virtual-back-button" style={{marginTop:"20px", marginRight:"20px"}} onClick={() => setShowExtract(false)}>
                        <p>{props.messages.general_close}</p>
                    </span>
                </div>
                
            </div>
                
                :null
            }
            <div className="virtual-editions">
                <button className="virtual-back-button" onClick={() => history.goBack()}>
                    <LeftArrow></LeftArrow>
                    <p>{props.messages.general_back}</p>
                </button>
                <span className="virtual-body-title">
                    <p>{data?he.decode(data.title):null} - <span className="virtual-link" onClick={() => history.push(`/virtual/virtualeditions/restricted/taxonomy/${data?data.taxonomyDto.externalId:null}`)}>{props.messages.general_taxonomy} </span>
                    - {data?data.name:null}</p>
                </span>
                <div style={{display:"flex", width:"100%", justifyContent:"flex-end"}}>
                    <div className="virtual-taxonomy-public">
                        <p style={{color:"#333"}}>{props.messages.general_public_pages}:</p>
                        <span className="virtual-taxonomy-span-flex" onClick={() => history.push(`/edition/acronym/${data?data.acronym:null}`)}>
                            <List className="virtual-list-icon"></List>
                            <p className="virtual-taxonomy-edition-text">{props.messages.general_edition} </p>
                        </span>
                        - <span className="virtual-taxonomy-span-flex" onClick={() => history.push(`/edition/acronym/${data?data.acronym:null}/category/${data?data.urlId:null}`)}>
                            <Tag className="virtual-list-icon"></Tag>
                            <p className="virtual-taxonomy-edition-text">{props.messages.general_category}</p>
                        </span>
                    </div>
                </div>
                {
                    data?data.taxonomyDto.taxonomy.canManipulate?
                    <div style={{display:"flex", justifyContent:"space-between", marginTop:"20px"}}>
                        <div>
                            {
                                errorCategory?
                                    <p style={{color:"red"}}>Erro a alterar nome de categoria (Repetido)</p>
                                :null
                            }
                            <input className="virtual-input-input" style={{padding:"0 12px", width:"145px"}}
                                    placeholder={props.messages.general_name}
                                    value={newCategoryName}
                                    onChange={e => setNewCategoryName(e.target.value)}>
                            </input>
                            <span className="virtual-add-category-button" style={{marginTop:"5px"}} onClick={() => handleUpdate()}>
                                <Edit style={{height:"18px", width:"18px", marginRight:"4px"}}></Edit>
                                <p>{props.messages.general_update}</p>
                            </span>
                        </div>
                        <span className="virtual-add-category-button" style={{maxHeight:"20px"}} onClick={() => {deleteCategory()}}>
                            <span style={{marginRight:"5px", color:"white", fontWeight:700}}>X</span>
                            <p>{props.messages.general_delete}</p>
                        </span>
                    </div>
                    :null:null
                }

                <table className="virtual-restricted-table">
                    <thead>
                        <tr>
                            <th>
                                {props.messages.fragments}
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                        data?
                            mapIntersToTable()
                        :null
                        }
                    </tbody>
                </table>
                {
                    data?data.taxonomyDto.taxonomy.canManipulate?
                    <div style={{display:"flex", justifyContent:"flex-end"}}>
                        <span data-tip={props.messages.taxonomies_tt_extractedcategory} className="virtual-add-category-button" style={{marginTop:"5px"}} onClick={() => {
                            window.scrollTo({ top: 0, behavior: 'smooth' })
                            setShowExtract(true)}}>
                            <Edit style={{height:"18px", width:"18px", marginRight:"4px"}}></Edit>
                            <p>{props.messages.general_extract}</p>
                        </span>
                    </div>
                    
                    :null:null
                }
                            <ReactTooltip backgroundColor="#000" textColor="#fff" border={true} borderColor="#000" className="virtual-tooltip" place="bottom" effect="solid"/>

            </div>

        </div>
        
    )
}

export default Category