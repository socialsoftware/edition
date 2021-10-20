import React, { useEffect, useState } from 'react'
import ReactTooltip from 'react-tooltip'
import {ReactComponent as Plus} from '../../../resources/assets/plus-lg.svg'
import {ReactComponent as Trash} from '../../../resources/assets/trash-fill.svg'
import {ReactComponent as Up} from '../../../resources/assets/caret-up-fill.svg'
import {ReactComponent as Down} from '../../../resources/assets/caret-down-fill.svg'
import {ReactComponent as ArrowUp} from '../../../resources/assets/arrow-up.svg'
import {ReactComponent as ArrowDown} from '../../../resources/assets/arrow-down.svg'
import {ReactComponent as ArrowRight} from '../../../resources/assets/arrow-right.svg'
import {ReactComponent as Save} from '../../../resources/assets/floppy-disk.svg'
import {ReactComponent as LeftArrow} from '../../../resources/assets/caret-left-fill.svg'
import {ReactComponent as LinkImg} from '../../../resources/assets/link-45deg.svg'
import { reorderCurrentList, getManualData } from '../../../util/API/VirtualAPI';
import Select from 'react-select'
import lupa from '../../../resources/assets/lupa.svg'
import { getSimpleSearchList } from '../../../util/API/SearchAPI';
import { useHistory, useLocation } from 'react-router-dom'
import VirtualSearchTable from './VirtualSearchTable'
import CircleLoader from "react-spinners/RotateLoader";

const Manual = (props) => {
    
    const history = useHistory()
    const location = useLocation()
    const [data, setData] = useState(null)
    const [currentList, setCurrentList] = useState([])
    const [selected, setSelected] = useState(null)
    const [selectedPosition, setSelectedPosition] = useState(null)
    const [showMove, setShowMove] = useState(false)
    const [searchLoading, setSearchLoading] = useState(false)
    const [query, setQuery] = useState("")
    const [search, setSearch] = useState("")
    const [source, setSource] = useState("")
    const [tableData, setTableData] = useState(null)
    const [showSearch, setShowSearch] = useState(false)
    const [extId, setExtId] = useState(null)
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        var path = location.pathname.split('/')
        let mounted = true
        getManualData(path[5])
            .then(res => {
                if(mounted){
                    setExtId(path[5])
                    setData(res.data.virtualEdition)
                    setCurrentList(res.data.recommendedEditionInter)
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

    useEffect(() => {
        if(showSearch){
            window.scrollTo(0, 0);
            document.body.style.overflow = 'hidden';
        } 
        else document.body.style.overflow = 'scroll';
    }, [showSearch])

    const searchType = [
        { value: '', label: props.messages.search_complete },
        { value: 'title', label: props.messages.search_title },
    ]

    const sourceType = [
        { value: '', label: props.messages.search_source },
        { value: 'Coelho', label: 'Jacinto Prado Coelho' },
        { value: 'Cunha', label: 'Teresa Sobral Cunha' },
        { value: 'Zenith', label: 'Richard Zenith' },
        { value: 'Pizarro', label: 'Jerónimo Pizarro' },
        { value: 'BNP', label: props.messages.search_authorial },
    ]

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

    const searchHandler = () => {

    var data = query + "&" + search + "&" + source
    if(query !== "" && query.replace(/\s/g, '').length){
        setSearchLoading(true)
        getSimpleSearchList(data)
            .then(res => {
                setTableData(res.data)
                setSearchLoading(false)
            })
            .catch(err => {
                console.log(err);
            })
    }
}
    const mapUsesToInter = (inter) => {
        return inter.listUsed.map((used,i) => {
            return <span key={i}>{used.shortName} </span>
        })
    }

    const mapEditionToTable = () => {
        return currentList.map((inter, i) => {
            return(
                <tr key={i} className={inter===selected?"virtual-row-selected":""} style={{cursor:"pointer"}} onClick={() => {
                    if(selected === inter) setSelected(null)
                    else setSelected(inter)
                    setSelectedPosition(i+1)
                    }}>
                    <td>{i+1}</td>
                    <td>{inter.title}</td>
                    <td>
                    {
                        inter.listUsed?
                        <span>
                            {"> "}{mapUsesToInter(inter)}
                        </span>
                        :null
                    }
                    </td>
                    <td className="virtual-link-link" onClick={() => history.push(`/fragments/fragment/${inter.fragmentXmlId}/inter/${inter.urlId}`)}>
                        <LinkImg fill="#337ab7" ></LinkImg>
                    </td>
                    
                </tr>
            )
        })
    }

    const moveDownHandler = () => {
        var auxArray = [...currentList]
        if(selected){
            let index = currentList.indexOf(selected)
            if(index < currentList.length-1){
                auxArray[index] = currentList[index+1]
                auxArray[index+1] = currentList[index]
                setCurrentList(auxArray)
                setSelectedPosition(index+2)
            }
            
        }
    }
    const moveUpHandler = () => {
        var auxArray = [...currentList]
        if(selected){
            let index = currentList.indexOf(selected)
            if(index > 0){
                auxArray[index] = currentList[index-1]
                auxArray[index-1] = currentList[index]
                setCurrentList(auxArray)
                setSelectedPosition(index)
            }
            
        }
    }
    const moveToBottomHandler = () => {
        var auxArray = [...currentList]
        if(selected){
            let index = currentList.indexOf(selected)
            if(index < currentList.length-1){
                auxArray.splice(index,1)
                auxArray.push(currentList[index]) 
                setCurrentList(auxArray)
                setSelectedPosition(currentList.length)
            }
            
        }
    }
    const moveToTopHandler = () => {
        var auxArray = [...currentList]
        if(selected){
            let index = currentList.indexOf(selected)
            if(index > 0){
                auxArray.splice(index,1)
                auxArray.unshift(currentList[index])
                setCurrentList(auxArray)
                setSelectedPosition(1)
            }
            
        }
    }
    const deleteHandler = () => {
        var auxArray = [...currentList]
        if(selected){
            auxArray.splice(currentList.indexOf(selected), 1)
            setCurrentList(auxArray)
            setSelectedPosition(null)
        }
    }

    const handleRightArrowChange = () => {
        var auxArray = [...currentList]
        if(selected){
            auxArray.splice(currentList.indexOf(selected), 1)
            if(selectedPosition > auxArray.length) auxArray.push(selected)
            else{
                auxArray.splice(selectedPosition-1, 0, selected)
            }
            setCurrentList(auxArray)
            setSelectedPosition(null)
            setShowMove(false)
        }
    }

    const addInterpsHandler = (selected) => {
        setShowSearch(false)
        var flag = true
        var auxCurrent = [...currentList]
        for(let sel of selected){
            flag = true
            for(let curr of currentList){
                if(sel.original.title === curr.title){
                    flag = false
                    alert(`Interpretação ${curr.title} já existe na lista!`)
                    break
                }
            }
            if(flag){
                let obj = {
                    externalId:sel.original.externalId,
                    fragmentXmlId:sel.original.fragmentXmlId,
                    listUsed:sel.original.listUsed,
                    title:sel.original.title,
                    urlId:sel.original.urlId
                }
                auxCurrent.unshift(obj)
            }
        }
        setCurrentList(auxCurrent)
        setSelected(auxCurrent[0])
        setSelectedPosition(1)

    }

    const saveCurrentList = () => {
        let auxString = ""
        for(let el of currentList){
            auxString += `${el.externalId};`
        }
        reorderCurrentList(extId, auxString)
    
    }

    return (
        <div>
            {showSearch?
                <div className="virtual-backdrop" >
                </div>
                :null
            }
            {showSearch?
                <div className="virtual-search-div">
                    <div className="virtual-search-title">
                        <p className="virtual-search-title-text">{props.messages.general_add}</p>
                        <p className="virtual-search-title-x" onClick={() => setShowSearch(false)}>x</p>
                    </div>
                    <div className="virtual-search">
                        <input className="virtual-input-input" style={{padding:"0 12px"}}
                            placeholder={props.messages.general_searching_for}
                            value={query}
                            onChange={e => setQuery(e.target.value)}>
                        </input>
                        <Select 
                            styles={customStyles}
                            value={searchType.filter(option => option.value === search)}
                            onChange={value => setSearch(value.value)}
                            options={searchType} className="virtual-select-search"/>
                        <Select 
                            styles={customStyles}
                            value={sourceType.filter(option => option.value === source)}
                            onChange={value => setSource(value.value)}
                            options={sourceType} className="virtual-select-search"/>
                        <div onClick={() => searchHandler()} className="form-control virtual-search-button">
                            <img src={lupa} alt="img" style={{height:"15px", width:"15px", marginRight:"5px"}}></img>
                            <p>{props.messages.search}</p>
                        </div>
                    </div>
                    {
                        searchLoading?
                            <div className="virtual-search-loading">
                                <CircleLoader loading={searchLoading}></CircleLoader>
                            </div>
                        :tableData?
                        <div>
                            <VirtualSearchTable messages={props.messages} tableData={tableData}
                                addInterpsCallback={selected => addInterpsHandler(selected)}
                                />
                            
                        </div>
                        :null
                    }
                    
                </div>
                :null
            }
            
            
            <div style={{borderBottom:"1px solid #ddd"}}>
                <div className="virtual-controls">
                    <div className="virtual-controls-flex">
                        <Plus data-tip={props.messages.virtualedition_tt_addFragment} className="virtual-control-wrap virtual-control-button" style={{marginRight:"5px"}} onClick={() => setShowSearch(true)}></Plus>
                        <Trash data-tip={props.messages.virtualedition_tt_deleteFragment} className="virtual-control-wrap virtual-control-button" style={{marginRight:"5px"}} onClick={() => deleteHandler()}></Trash>
                        <div style={{display:"flex", marginRight:"5px"}}>
                            <Up data-tip={props.messages.virtualedition_tt_moveup} className="virtual-control-wrap virtual-control-button"
                                    style={{borderTopRightRadius:0, borderBottomRightRadius:0}} onClick={() => moveUpHandler()}
                            ></Up>
                            <Down data-tip={props.messages.virtualedition_tt_movedown} className="virtual-control-wrap virtual-control-button"
                                    style={{borderRadius:0}} onClick={() => moveDownHandler()}
                            ></Down>
                            <ArrowUp data-tip={props.messages.virtualedition_tt_movetop} className="virtual-control-wrap virtual-control-button"
                                    style={{borderRadius:0}} onClick={() => moveToTopHandler()}
                            ></ArrowUp>
                            <ArrowDown data-tip={props.messages.virtualedition_tt_movebottom} className="virtual-control-wrap virtual-control-button"
                                    style={{borderTopLeftRadius:0, borderBottomLeftRadius:0}} onClick={() => moveToBottomHandler()}
                            ></ArrowDown> 
                        </div>
                        <div style={{maxHeight:"35px", position:"relative"}}>
                            <ArrowRight onClick={() => setShowMove(!showMove)} style={{marginRight:"5px"}} data-tip={props.messages.virtualedition_tt_moveposition} className="virtual-control-wrap virtual-control-button"
                            ></ArrowRight>
                            {
                                showMove?
                                    <div className="virtual-arrow-right">
                                        <p className="virtual-arrow-right-text">{props.messages.virtualedition_tt_moveposition}</p>
                                        <div style={{display:"flex", justifyContent:"space-around", alignItems:"center"}}>
                                            <input disabled={!selected} style={{cursor:selected?"default":"not-allowed"}} value={selectedPosition} type="number" className="virtual-input-input virtual-input-number" onChange={(e) => setSelectedPosition(e.target.value)} min="1"></input>
                                            <span className="virtual-arrow-right-ok" onClick={() => handleRightArrowChange()}>OK</span>
                                        </div>
                                    </div>
                                :null
                            }
                            
                        </div>
                        
                        <Save 
                            fill="#fff" 
                            style={{height:"100%", width:"16px"}} 
                            data-tip={props.messages.virtualedition_tt_save} 
                            className="virtual-control-wrap virtual-control-button virtual-save-button" 
                            onClick={() => saveCurrentList()}
                        ></Save>
                    </div>
                    
                </div>
            </div>
            <ReactTooltip backgroundColor="#000" textColor="#fff" border={true} borderColor="#000" className="virtual-tooltip" place="bottom" effect="solid"/>
            {
                loading?
                <div style={{marginTop:"250px"}}>
                    <CircleLoader loading={true}></CircleLoader>
                </div>
                :
                <div className="virtual-editions" style={{marginTop:"120px"}}>
                    <button className="virtual-back-button" onClick={() => history.goBack()}>
                        <LeftArrow></LeftArrow>
                        <p>{props.messages.general_back}</p>
                    </button>
                    <span className="virtual-body-title">
                        <p>{data?data.title:null}</p>
                    </span>
                    <table className="virtual-participation-table" style={{marginBottom:"30px"}}>
                        <thead>
                            <tr>
                                <th></th>
                                <th>{props.messages.tableofcontents_title}</th>
                                <th>{props.messages.general_edition}</th>
                                <th>Link</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                currentList?
                                    mapEditionToTable()
                                :null
                            }
                        </tbody>
                    </table>

                </div>
            }
            
            
        </div>
    )   
}

export default Manual