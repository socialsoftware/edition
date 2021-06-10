import React, { useState } from 'react'
import Facsimilie from './Facsimilie'
import InterTranscript from './interTranscript'


const InterAuthorial = (props) => {


    const [selected, setSelected] = useState({
        diff: false,
        del: false,
        ins: false,
        subst: false,
        notes: false,
        facs: false,
        pb: null
    })

    const callbackInputSelect = (bool) => {
        let aux = {...selected}
        aux[bool] = !aux[bool]
        setSelected(aux)
        props.callbackSelectedHandler(aux)
    }

    const callbackFacsimilieInputSelect = (pbVal) => {
        let aux = {...selected}
        aux["pb"] = pbVal
        props.callbackSelectedHandler(aux)
    }

    return (
        <div>
            <div style={{display:"flex", justifyContent:"space-between"}}>
                <div className="inter-editorial-flex">
                    <input type="checkbox" checked={selected["diff"]} onChange={() => callbackInputSelect("diff")}></input>
                    <p>{props.messages.fragment_highlightdifferences}</p>
                </div>
                <div className="inter-editorial-flex">
                    <input type="checkbox" checked={selected["del"]} onChange={() => callbackInputSelect("del")}></input>
                    <p>{props.messages.fragment_showdeleted}</p>
                </div>
                <div className="inter-editorial-flex">
                    <input type="checkbox" checked={selected["ins"]} onChange={() => callbackInputSelect("ins")}></input>
                    <p>{props.messages.fragment_highlightinserted}</p>
                </div>
                <div className="inter-editorial-flex">
                    <input type="checkbox" checked={selected["subst"]} onChange={() => callbackInputSelect("subst")}></input>
                    <p>{props.messages.fragment_highlightsubstitutions}</p>
                </div>
                <div className="inter-editorial-flex">
                    <input type="checkbox" checked={selected["notes"]} onChange={() => callbackInputSelect("notes")}></input>
                    <p>{props.messages.fragment_shownotes}</p>
                </div>
                <div className="inter-editorial-flex">
                    <input type="checkbox" checked={selected["facs"]} onChange={() => callbackInputSelect("facs")}></input>
                    <p>{props.messages.fragment_showfacs}</p>
                </div>
            </div>
            
            <span className="body-title">{props.data.inters[0].title}</span>

            {selected.facs?
                <Facsimilie data={props.data} messages={props.messages} callbackFacsimilieInputSelect={(pbText) => callbackFacsimilieInputSelect(pbText)}/>
                :<InterTranscript data={props.data} messages={props.messages} type="AUTHORIAL" />
            }
            
            
            
        </div>
    )
}

export default InterAuthorial
