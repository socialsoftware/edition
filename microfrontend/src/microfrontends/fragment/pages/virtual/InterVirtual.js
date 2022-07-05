import React, {useState, useEffect} from 'react'
import Taxonomy from './Taxonomy'
import he from 'he'
import plus from '../../../../resources/assets/plus_white.png'
import CreatableSelect from 'react-select/creatable';

const InterVirtual = (props) => {

    const [add, setAdd] = useState(false)
    const [defaultSelected, setDefaultSelected] = useState([])
    const [finalSelected, setFinalSelected] = useState([])
    const [tagOptions, setTagOptions] = useState([])

    useEffect(() => {
        let aux = []
        for(let el of props.data.inters[0].categoryList){
            aux.push({value: el.externalId, label: el.normalName})
        }
        setDefaultSelected(aux)
        setFinalSelected(aux)
        let aux1 = []
        /* for(let el1 of props.data.inters[0].allCategories){ //allCategories?
            aux1.push(
                {value: el1.normalName, label: el1.normalName} //value devia ser external ID???
            )
        } */
        setTagOptions(aux1)
        
    }, [props.data])
    
    const checkForOwnershipHandler = () => {
        for(let el of props.data.virtualEditionsDto){
            if(props.data.inters[0].virtualExternalId === el.externalId){
                if(el.participantSetContains) return true
            }
        }
        return false
    }

    const addButtonHandler = () => {
        setAdd(true)
        window.scroll({top:0,behavior:'smooth'})
        document.body.style.overflow = "hidden"
    }

    const handleChange = (selectedOptions) => {
        let aux = []
        for(let el of selectedOptions){
            aux.push(el.value)
        }
        setFinalSelected(aux)
    }
    return (
        <div>
            <p className="body-title" style={{margin:0, textAlign:"left"}}>{he.decode(props.data.inters[0].editionTitle)} - {props.messages.general_uses} {props.data.inters[0].usesReference} ({props.data.inters[0].usesSecondReference})</p>
            <p className="body-title" style={{marginTop:"20px"}}>{props.data.inters[0].title}</p>

            <div className="well" style={{fontFamily:"courier"}} dangerouslySetInnerHTML={{ __html: props.data.transcript }} />
            
            {
                checkForOwnershipHandler()?
                <div style={{display:"flex", justifyContent:"flex-end"}}>
                    <span onClick={() => addButtonHandler()} className="fragment-add">
                        <img alt="add" className="fragment-add-plus" src={plus}></img>
                    </span>
                </div>
                
                :null
            }
            <Taxonomy data={props.data}/>

            {
                add?
                <div className="fragment-add-comp">
                    <div className="fragment-add-model-back" onClick={() => {
                        setAdd(false)
                        document.body.style.overflow = "scroll"
                        }} ></div>
                    <div className="fragment-add-model">
                        <div className="fragment-add-model-top">
                            <p className="fragment-add-model-top-title">{props.data.inters[0].title}</p>
                            <p className="fragment-add-model-top-close" onClick={() => {
                                setAdd(false)
                                document.body.style.overflow = "scroll"
                                }}>x</p>
                        </div>
                        <div className="fragment-add-model-middle">
                            <div>
                            <CreatableSelect
                                defaultValue={defaultSelected}
                                isMulti
                                options={tagOptions}
                                className="fragment-add-model-middle-select"
                                onChange={handleChange}
                                />
                            </div>
                            <div style={{marginTop:"30px"}}>
                                <span className="fragment-add-model-middle-button" onClick={() => {}}>
                                    {props.messages.general_associate}
                                </span>
                            </div>
                            
                        </div>
                        <div className="fragment-add-model-bottom">
                            <span className="fragment-add-model-bottom-button" onClick={() => {
                                setAdd(false)
                                document.body.style.overflow = "scroll"
                                }}>{props.messages.general_close}</span>
                        </div>
                    </div>
                </div>
                :null
            }
            
        </div>
    )
}

export default InterVirtual