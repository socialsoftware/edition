import React, { useState } from 'react'
import Select from 'react-select';

const Advanced_expert = (props) => {

    const [inclusion, setInclusion] = useState("in")
    const [editor, setEditor] = useState("all")
    const [editionHeteronym, setEditionHeteronym] = useState("all")
    const [editionDate, setEditionDate] = useState("all")
    const [editionBeginDate, setEditionBeginDate] = useState(1913)
    const [editionEndDate, setEditionEndDate] = useState(1935)
    const [manuscriptLabel, setManuscriptLabel] = useState("all")
    const [manuscriptDate, setManuscriptDate] = useState("all")
    const [beginDate, setBeginDate] = useState(1913)
    const [endDate, setEndDate] = useState(1935)
    const [manuscriptDatePub, setManuscriptDatePub] = useState("all")
    const [beginDatePub, setBeginDatePub] = useState(1913)
    const [endDatePub, setEndDatePub] = useState(1935)
    const [heteronym, setHeteronym] = useState("all")
    const [onlyDate, setOnlyDate] = useState("all")
    const [onlyBeginDate, setOnlyBeginDate] = useState(1913)
    const [onlyEndDate, setOnlyEndDate] = useState(1935)
    const [text, setText] = useState("")

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

    const inclusionOptions = [
        { value: 'in', label: props.messages.search_inclusion_includedIn },
        { value: 'out', label: props.messages.search_inclusion_excludedFrom },
    ]
    const editorOptions = [
        { value: 'all', label: props.messages.search_option_all },
        { value: 'JPC', label: "Jacinto do Prado Coelho" },
        { value: 'TSC', label: "Teresa Sobral Cunha" },
        { value: 'RZ', label: "Richard Zenith" },
        { value: 'JP', label: "Jerónimo Pizarro" },
    ]
    const manuscriptLabelOptions = [
        { value: 'all', label: props.messages.search_option_all },
        { value: 'true', label: props.messages.search_ldod_with },
        { value: 'false', label: props.messages.search_ldod_without }
    ]
    const manuscriptDateOptions = [
        { value: 'all', label: props.messages.search_option_all },
        { value: 'dated', label: props.messages.search_date_dated },
        { value: 'undated', label: props.messages.search_date_unDated }
    ]
    const heteronymOptions = [
        { value: 'all', label: props.messages.search_option_all },
        { value: 'null', label: props.messages.search_option_noAttribution },
        { value: 'HT.VG', label: "Vicente Guedes"},
        { value: 'HT.BS', label: "Bernardo Soares"}
    ]
    return (
        <div>
            {
            props.domain === 'Edition'?
                <div className="form-advanced-options-edition">
                    <div style={{display:"flex"}}>
                    <Select 
                        styles={customStyles}
                        value={inclusionOptions.filter(option => option.value === inclusion)}
                        onChange={value => {
                            props.onChangeCallback(value.value, "inclusion")
                            setInclusion(value.value)
                        }}
                        options={inclusionOptions} className="form-control select-search-edition" />
                        <div style={{marginLeft:"20px"}}>
                            <Select
                                styles={customStyles}
                                value={editorOptions.filter(option => option.value === editor)}
                                onChange={value => {
                                    props.onChangeCallback(value.value, "editor")
                                    setEditor(value.value)
                                }}
                                options={editorOptions} className="form-control select-search-edition"/>
                        </div>
                    </div>
                        <div>
                        {
                            editor !== "all"?
                            <div className="edition-special">
                                <div style={{width:"200px"}}>
                                    <p style={{textAlign:"left"}}><strong>{props.messages.general_heteronym}</strong></p>
                                    <Select
                                        styles={customStyles}
                                        value={heteronymOptions.filter(option => option.value === editionHeteronym)}
                                        onChange={value => {
                                            setEditionHeteronym(value.value)
                                            props.onChangeCallback(value.value, "editionHeteronym")
                                        }}
                                        options={heteronymOptions} className="form-control select-search-manuscript" />
                                </div>
                                <div style={{width:"200px"}}>
                                    <p style={{textAlign:"left"}}><strong>{props.messages.general_date}</strong></p>
                                    <Select 
                                        styles={customStyles}
                                        value={manuscriptDateOptions.filter(option => option.value === editionDate)}
                                        onChange={value => {
                                            setEditionDate(value.value)
                                            props.onChangeCallback(value.value, "editionDate")
                                        }}
                                        options={manuscriptDateOptions} className="form-control select-search-manuscript"/>
                                    {
                                        editionDate==="dated"?
                                        <div>
                                            <div>
                                                <p style={{textAlign:"left", marginTop:"10px"}}><strong>{props.messages.search_date_begin}</strong></p>
                                                <input className="form-control input-search" style={{padding:"0 12px"}}
                                                    value={editionBeginDate}
                                                    onChange={e => {
                                                        setEditionBeginDate(parseInt(e.target.value))
                                                        props.onChangeCallback(parseInt(e.target.value), "editionBeginDate")
                                                    }}>
                                                </input>
                                            </div>
                                            <div>
                                                <p style={{textAlign:"left", marginTop:"10px"}}><strong>{props.messages.search_date_end}</strong></p>
                                                <input className="form-control input-search" style={{padding:"0 12px"}}
                                                    value={editionEndDate}
                                                    onChange={e => {
                                                        setEditionEndDate(parseInt(e.target.value))
                                                        props.onChangeCallback(parseInt(e.target.value), "editionEndDate")
                                                    }}>
                                                </input>
                                            </div>
                                        </div>
                                            
                                            :null

                                    }
                                    
                                    </div>
                                </div>
                        :null

                        }
                        </div>
                </div>
            :props.domain === 'Manuscript' || props.domain === 'Dactiloscript'?
                <div className="form-advanced-options" style={{marginLeft:"20px"}}>
                    <div style={{width:"500px"}}>
                        <p style={{textAlign:"left"}}><strong>{props.messages.general_LdoDLabel}</strong></p>
                        <Select
                            styles={customStyles}
                            value={manuscriptLabelOptions.filter(option => option.value === manuscriptLabel)}
                            onChange={value => {
                                setManuscriptLabel(value.value)
                                props.onChangeCallback(value.value, "manuscriptLabel")

                            }}
                            options={manuscriptLabelOptions} className="form-control select-search-manuscript" />
                    </div>
                    <div style={{width:"500px"}}>
                        <p style={{textAlign:"left"}}><strong>{props.messages.general_date}</strong></p>
                        <Select 
                            styles={customStyles}
                            value={manuscriptDateOptions.filter(option => option.value === manuscriptDate)}
                            onChange={value => {
                                setManuscriptDate(value.value)
                                props.onChangeCallback(value.value, "manuscriptDate")
                            }}
                            options={manuscriptDateOptions} className="form-control select-search-manuscript"/>
                        {
                            manuscriptDate==="dated"?
                            <div>
                                <div>
                                    <p style={{textAlign:"left", marginTop:"10px"}}><strong>{props.messages.search_date_begin}</strong></p>
                                    <input className="form-control input-search" style={{padding:"0 12px"}}
                                        value={beginDate}
                                        onChange={e => {
                                            setBeginDate(parseInt(e.target.value))
                                            props.onChangeCallback(parseInt(e.target.value), "beginDate")
                                        }}>
                                    </input>
                                </div>
                                <div>
                                    <p style={{textAlign:"left", marginTop:"10px"}}><strong>{props.messages.search_date_end}</strong></p>
                                    <input className="form-control input-search" style={{padding:"0 12px"}}
                                        value={endDate}
                                        onChange={e => {
                                            setEndDate(parseInt(e.target.value))
                                            props.onChangeCallback(parseInt(e.target.value), "endDate")
                                        }}>
                                    </input>
                                </div>
                            </div>
                                
                                :null

                        }
                        
                    </div>
                    
                </div>

                :props.domain === 'Publication'?
                    <div style={{width:"200px", marginLeft:"20px"}}>
                                <p style={{textAlign:"left"}}><strong>{props.messages.general_date}</strong></p>
                                <Select 
                                    styles={customStyles}
                                    value={manuscriptDateOptions.filter(option => option.value === manuscriptDatePub)}
                                    onChange={value => {
                                        props.onChangeCallback(value.value, "manuscriptDatePub")
                                        setManuscriptDatePub(value.value)
                                    }}
                                    options={manuscriptDateOptions} className="form-control select-search-manuscript"/>
                                {
                                    manuscriptDatePub==="dated"?
                                    <div>
                                        <div>
                                            <p style={{textAlign:"left", marginTop:"10px"}}><strong>{props.messages.search_date_begin}</strong></p>
                                            <input className="form-control input-search" style={{padding:"0 12px"}}
                                                value={beginDatePub}
                                                onChange={e => {
                                                    setBeginDatePub(parseInt(e.target.value))
                                                    props.onChangeCallback(parseInt(e.target.value), "beginDatePub")
                                                }}>
                                            </input>
                                        </div>
                                        <div>
                                            <p style={{textAlign:"left", marginTop:"10px"}}><strong>{props.messages.search_date_end}</strong></p>
                                            <input className="form-control input-search" style={{padding:"0 12px"}}
                                                value={endDatePub}
                                                onChange={e => {
                                                    setEndDatePub(parseInt(e.target.value))
                                                    props.onChangeCallback(parseInt(e.target.value), "endDatePub")
                                                }}>
                                            </input>
                                        </div>
                                    </div>
                                
                                        :null

                                }
                        
                    </div>
                :props.domain === 'Heteronym'?
                    <div style={{width:"200px", marginLeft:"20px"}}>
                            <p style={{textAlign:"left"}}><strong>{props.messages.general_heteronym}</strong></p>
                            <Select 
                                styles={customStyles}
                                value={heteronymOptions.filter(option => option.value === heteronym)}
                                onChange={value => {
                                    props.onChangeCallback(value.value, "heteronym")
                                    setHeteronym(value.value)
                                }}
                                options={heteronymOptions} className="form-control select-search-manuscript"/>
                        
                    </div>
                :props.domain === 'MyDate'?
                <div style={{width:"200px", marginLeft:"20px"}}>
                    <p style={{textAlign:"left"}}><strong>{props.messages.general_date}</strong></p>
                            <Select 
                                styles={customStyles}
                                value={manuscriptDateOptions.filter(option => option.value === onlyDate)}
                                onChange={value => {
                                    props.onChangeCallback(value.value, "onlyDate")
                                    setOnlyDate(value.value)
                                }}
                                options={manuscriptDateOptions} className="form-control select-search-manuscript"/>
                            {
                                onlyDate==="dated"?
                                <div>
                                    <div>
                                        <p style={{textAlign:"left", marginTop:"10px"}}><strong>{props.messages.search_date_begin}</strong></p>
                                        <input className="form-control input-search" style={{padding:"0 12px"}}
                                            value={onlyBeginDate}
                                            onChange={e => {
                                                setOnlyBeginDate(parseInt(e.target.value))
                                                props.onChangeCallback(parseInt(e.target.value), "endDatePub")
                                            }}>
                                        </input>
                                    </div>
                                    <div>
                                        <p style={{textAlign:"left", marginTop:"10px"}}><strong>{props.messages.search_date_end}</strong></p>
                                        <input className="form-control input-search" style={{padding:"0 12px"}}
                                            value={onlyEndDate}
                                            onChange={e => {
                                                setOnlyEndDate(parseInt(e.target.value))
                                                props.onChangeCallback(parseInt(e.target.value), "onlyEndDate")
                                            }}>
                                        </input>
                                    </div>
                                </div>
                            
                                    :null

                            }

                </div>   
                
                :props.domain === 'MyText'?
                    <div style={{width:"600px", marginLeft:"20px"}}>
                        <p style={{textAlign:"left"}}><strong>{props.messages.search_keyword}</strong></p>
                        <input className="form-control input-search" style={{padding:"0 12px", width:"300px"}}
                            value={text}
                            onChange={e => {
                                setText(e.target.value)
                                props.onChangeCallback(e.target.value, "text")
                            }}>
                        </input>
                    
                </div>   
                :null
            }
        </div>
    )
}

export default Advanced_expert