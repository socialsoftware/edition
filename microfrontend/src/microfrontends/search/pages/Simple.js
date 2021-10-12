import React, { useState } from 'react'
import Select from 'react-select'
import lupa from '../../../resources/assets/lupa.svg'
import { getSimpleSearchList } from '../../../util/API/SearchAPI';
import SimpleResultTable from './SimpleResultTable';

const Simple = (props) => {

    const [query, setQuery] = useState("")
    const [search, setSearch] = useState("")
    const [source, setSource] = useState("")
    const [data, setData] = useState(null)
    const [loading, setLoading] = useState(true)

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
            setLoading(false)
            console.log(data);
            getSimpleSearchList(data)
                .then(res => {
                    console.log(res.data);
                    setData(res.data)
                })
                .catch(err => {
                    console.log(err);
                })
        }
        else setLoading(true)
    }
    return (
        <div className="search-page">
            <p className="search-title">{props.messages.header_search_simple}</p>
            <div className="form">
                <input className="form-control input-search" style={{padding:"0 0 0 12px"}}
                    placeholder={props.messages.general_searching_for}
                    value={query}
                    onChange={e => setQuery(e.target.value)}>
                </input>
                <Select 
                    styles={customStyles}
                    value={searchType.filter(option => option.value === search)}
                    onChange={value => setSearch(value.value)}
                    options={searchType} className="form-control select-search"/>
                <Select 
                    styles={customStyles}
                    value={sourceType.filter(option => option.value === source)}
                    onChange={value => setSource(value.value)}
                    options={sourceType} className="form-control select-search"/>
                <div onClick={() => searchHandler()} className="form-control search-button">
                    <img src={lupa} alt="img" style={{height:"15px", width:"15px", marginRight:"5px"}}></img>
                    <p>{props.messages.search}</p>
                </div>
            </div>
            {
                loading?null:
                    <SimpleResultTable data={data} messages={props.messages}/>
                    
            }
            
        </div>
    )
}

export default Simple