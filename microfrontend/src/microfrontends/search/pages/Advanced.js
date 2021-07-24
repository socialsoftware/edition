import React, { useState } from 'react'
import Select from 'react-select'
import lupa from '../../../resources/assets/lupa.svg'
import { getAdvancedSearchList } from '../../../util/API/SearchAPI';
import AdvancedExpert from './expert/Advanced_expert';
import AdvancedVirtual from './virtual/Advanced_virtual';
import ResultTable from './ResultTable';
import CircleLoader from "react-spinners/RotateLoader";

const Advanced = (props) => {

    const [searchM, setSearchM] = useState("and")
    const [loading, setLoading] = useState(false)
    const [selected, setSelected] = useState([{
        domain:"Edition",
        inclusion:"in",
        editor:"all",
        editionHeteronym:"all",
        editionDate:"all",
        editionBeginDate:1913,
        editionEndDate:1935,
        manuscriptLabel:"all",
        manuscriptDate:"all",
        beginDate:1913,
        endDate:1935,
        manuscriptDatePub:"all",
        beginDatePub:1913,
        endDatePub:1935,
        heteronym:"all",
        taxonomy:"",
        inclusionVirtual:"in",
        editorVirtual:"all",
        onlyDate:"all",
        onlyBeginDate:1913,
        onlyEndDate:1935,
        text:""
    }])
    const [data, setData] = useState(null)

    const searchMatch = [
        { value: 'and', label: props.messages.search_rule_matchAll },
        { value: 'or', label: props.messages.search_rule_matchOne },
    ]
    const domain = [
        { value: 'Edition', label: props.messages.navigation_edition },
        { value: 'Manuscript', label: props.messages.general_manuscript },
        { value: 'Dactiloscript', label: props.messages.general_typescript },
        { value: 'Publication', label: props.messages.general_published },
        { value: 'Heteronym', label: props.messages.general_heteronym },
        { value: 'MyTaxonomy', label: props.messages.general_taxonomies },
        { value: 'VirtualEdition', label: props.messages.virtual_editions },
        { value: 'MyDate', label: props.messages.general_date },
        { value: 'MyText', label: props.messages.search_text },
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
        }),

    }

    const onChangeCallback = (val, val2, index) => {
        let auxSelected = [...selected]
        auxSelected[index][val2] = val
        setSelected(auxSelected)
        setData(null)
    }

    const getSelectForDomain = (val, i) => {
            return (
                <div key={i}>
                    <div className="form-advanced"> 
                        <Select 
                            styles={customStyles}
                            value={domain.filter(option => option.value === val.domain)}
                            onChange={value => {
                                let auxArr = [...selected]
                                auxArr[i].domain=value.value
                                setSelected(auxArr)
                                setData(null)
                            }}
                            options={domain} className="form-control select-search"/>
                            <AdvancedExpert domain={val.domain} messages={props.messages} onChangeCallback={(e, e1) => onChangeCallback(e, e1, i)}/>
                            <AdvancedVirtual domain={val.domain} messages={props.messages} onChangeCallback={(e, e1) => onChangeCallback(e, e1, i)}/>
                        <p className="button-criteria-minus" onClick={() => removeCriteriaHandler(i)}>-</p>
                    </div>
                    <div className="divider"></div>
                </div>
            )
    }

    const mapSelected = () => {
        return selected.map((selected, i) => {
            return getSelectForDomain(selected, i)
        })
    }

    const searchHandler = () => {
        setLoading(true)
        var options = []
        for(const el of selected){
            if(el.domain==="Edition"){
                options.push(
                    {
                        type : "edition",
                        inclusion : el.inclusion,
                        edition : el.editor,
                        heteronym : el.editionHeteronym == null ? null : {
                            type : "heteronym",
                            heteronym : el.editionHeteronym
                        },
                        date : el.date === null ? null : {
                            type : "date",
                            option : el.editionDate,
                            begin : el.editionBeginDate,
                            end : el.editionEndDate
                        }
                    }
                ) 
            }
            else if(el.domain==="Manuscript"){
                options.push(
                    {
                        type : "manuscript",
                        hasLdoDMark : el.manuscriptLabel,
                        date : el.date === null ? null : {
                            type : "date",
                            option : el.manuscriptDate,
                            begin : el.beginDate,
                            end : el.endDate
                        }
                    }
                )
            }
            else if(el.domain==="Dactiloscript"){
                
                options.push(
                    {
                        type : "dactiloscript",
                        hasLdoDMark : el.manuscriptLabel,
                        date : el.date === null ? null : {
                            type : "date",
                            option : el.manuscriptDate,
                            begin : el.editionBeginDate,
                            end : el.editionEndDate
                        }
                    }
                )
            }
            else if(el.domain==="Publication"){
                options.push(
                    {
                        type : "publication",
                        date : el.date === null ? null : {
                            type : "date",
                            option : el.manuscriptDatePub,
                            begin : el.beginDatePub,
                            end : el.endDatePub
                        }
                    }
                )
            }
            else if(el.domain==="Heteronym"){
                options.push(
                    {
                        type : "heteronym",
                        heteronym : el.heteronym
                    }
                )
            }
            else if(el.domain==="MyDate"){
                options.push(
                    {
                        type : "date",
                        option : el.onlyDate,
                        begin : el.onlyBeginDate,
                        end : el.onlyEndDate
                    }
                )
            }
            else if(el.domain==="MyText"){
                options.push(
                    {
                        type : "text",
                        text : el.text
                    }
                )
            }
            else if(el.domain==="MyText"){
                options.push(
                    {
                        type : "text",
                        text : el.text
                    }
                )
            }
            else if(el.domain==="MyTaxonomy"){
                options.push(
                    {
                        type : "taxonomy",
			            tags : el.taxonomy
                    }
                )
            }
            else if(el.domain==="VirtualEdition"){
                options.push(
                    {
                        type : "virtualedition",
			            inclusion : el.inclusionVirtual,
			            edition : el.editorVirtual,
                    }
                )
            }
        }
        var json = {
            mode : searchM,
            options : options
        }
        getAdvancedSearchList(json)
            .then(res => {
                setData(res.data)
                setLoading(false)
            })
    }

    const addCriteriaHandler = () => {
        setSelected(prevArray => [...prevArray, {
            domain:"Edition",
            inclusion:"in",
            editor:"all",
            editionHeteronym:"all",
            editionDate:"all",
            editionBeginDate:1913,
            editionEndDate:1934,
            manuscriptLabel:"all",
            manuscriptDate:"all",
            beginDate:1913,
            endDate:1934,
            manuscriptDatePub:"all",
            beginDatePub:1913,
            endDatePub:1934,
            heteronym:"all",
            taxonomy:"",
            inclusionVirtual:"in",
            editorVirtual:"all",
            onlyDate:"all",
            onlyBeginDate:1913,
            onlyEndDate:1934,
            text:""
        }])
        setData(null)
    }

    const removeCriteriaHandler = (index) => {
        let auxArray = [...selected]
        auxArray.splice(index,1)
        setSelected(auxArray)
        setData(null)
    }
    return (
        <div className="search-page">
            <p className="search-title">{props.messages.header_search_advanced}</p>
            <Select 
                    styles={customStyles}
                    value={searchMatch.filter(option => option.value === searchM)}
                    onChange={value => {setSearchM(value.value)}}
                    options={searchMatch} className="form-control select-search-long"/>
            <div className="divider"></div>
                {
                    mapSelected()
                }
            <div style={{display:"flex", justifyContent:"space-between"}}>
                <div onClick={() => searchHandler()} className="form-control search-button">
                    <img src={lupa} alt="img" style={{height:"15px", width:"15px", marginRight:"5px"}}></img>
                    <p>{props.messages.search}</p>
                </div>
                <p className="button-criteria" onClick={() => addCriteriaHandler()}>
                    +
                </p>
            </div>
            {
                !data?
                    <div style={{marginTop:"50px"}}>
                        <CircleLoader loading={loading}></CircleLoader>
                    </div>
                    
                    :<ResultTable data={data} messages={props.messages}/>
                    
            }
            
        </div>
    )
}

export default Advanced