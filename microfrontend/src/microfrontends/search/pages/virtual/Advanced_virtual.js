import React, { useEffect, useState } from 'react'
import Select from 'react-select/'
import { getVirtualEditionMap } from '../../../../util/API/SearchAPI';

const Advanced_virtual = (props) => {

    const [taxonomy, setTaxonomy] = useState("")
    const [inclusionVirtual, setInclusionVirtual] = useState("in")
    const [editorVirtual, setEditorVirtual] = useState("all")
    const [editorVirtualOptions, setEditorVirtualOptions] = useState([{ value: 'all', label: props.messages.search_option_all }])

    useEffect(() => {
        getVirtualEditionMap()
            .then(snap => {
                let arrayAux = [...editorVirtualOptions]
                // eslint-disable-next-line array-callback-return
                Object.entries(snap.data).map(([key, val]) => {
                    let aux = {
                        "value" : key,
                        "label" : val 
                    }
                    arrayAux.push(aux)
                })
                setEditorVirtualOptions(arrayAux)
            })
            // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])


    const inclusionOptions = [
        { value: 'in', label: props.messages.search_inclusion_includedIn },
        { value: 'out', label: props.messages.search_inclusion_excludedFrom },
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

    const customStylesVirtual = {
        option: (provided, state) => ({
          ...provided,
          color: state.isSelected ? '#fff' : '#555',
          textAlign:"left",
          width:"400px",
          padding:"5px"
        }),
        singleValue: (provided, state) => ({
            ...provided,
            color: "#555"
        }),
        menu: (provided, state) => ({
            ...provided,
            width:"400px"
          }),

    }

    return (
        <div>
            {
            props.domain === 'MyTaxonomy'?
                <div style={{width:"600px", marginLeft:"20px"}}>
                    <p style={{textAlign:"left"}}><strong>{props.messages.general_taxonomies}</strong></p>
                    <input className="form-control input-search" style={{padding:"0 12px"}}
                        value={taxonomy}
                        onChange={e => {
                            setTaxonomy(e.target.value)
                            props.onChangeCallback(e.target.value, "taxonomy")
                        }}>
                    </input>
                
                </div>   
                :props.domain === 'VirtualEdition'?
                <div className="form-advanced-options">
                    <Select 
                        styles={customStyles}
                        value={inclusionOptions.filter(option => option.value === inclusionVirtual)}
                        onChange={value => {
                            setInclusionVirtual(value.value)
                            props.onChangeCallback(value.value, "inclusionVirtual")
                        }}
                        options={inclusionOptions} className="form-control select-search-edition" />
                    <Select
                        styles={customStylesVirtual}
                        value={editorVirtualOptions.filter(option => option.value === editorVirtual)}
                        onChange={value => {
                            setEditorVirtual(value.value)
                            props.onChangeCallback(value.value, "editorVirtual")
                        }}
                        options={editorVirtualOptions} className="form-control select-search-virtual"/>
                </div>
                :null
            }
                        
        </div>
    )
}

export default Advanced_virtual