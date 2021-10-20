import React, { useState } from 'react'
import Facsimilie from './Facsimilie'
import InterTranscript from './interTranscript'
import ReactTooltip from 'react-tooltip';


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
            <div className="inter-authorial-control-div">
                <div className="inter-editorial-flex">
                    <input style={{cursor:"pointer"}} type="checkbox" checked={selected["diff"]} onChange={() => callbackInputSelect("diff")}></input>
                    <p style={{cursor:"pointer"}} data-tip={props.messages.fragment_tt_highlights} onClick={() => callbackInputSelect("diff")}>{props.messages.fragment_highlightdifferences}</p>
                </div>
                <div className="inter-editorial-flex">
                    <input style={{cursor:"pointer"}} type="checkbox" checked={selected["del"]} onChange={() => callbackInputSelect("del")}></input>
                    <p style={{cursor:"pointer"}} data-tip={props.messages.fragment_tt_deletions} onClick={() => callbackInputSelect("del")}>{props.messages.fragment_showdeleted}</p>
                </div>
                <div className="inter-editorial-flex">
                    <input style={{cursor:"pointer"}} type="checkbox" checked={selected["ins"]} onChange={() => callbackInputSelect("ins")}></input>
                    <p style={{cursor:"pointer"}} data-tip={props.messages.fragment_tt_additions} onClick={() => callbackInputSelect("ins")}>{props.messages.fragment_highlightinserted}</p>
                </div>
                <div className="inter-editorial-flex">
                    <input style={{cursor:"pointer"}} type="checkbox" checked={selected["subst"]} onChange={() => callbackInputSelect("subst")}></input>
                    <p style={{cursor:"pointer"}} data-tip={props.messages.fragment_tt_substitution} onClick={() => callbackInputSelect("subst")}>{props.messages.fragment_highlightsubstitutions}</p>
                </div>
                <div className="inter-editorial-flex">
                    <input style={{cursor:"pointer"}} type="checkbox" checked={selected["notes"]} onChange={() => callbackInputSelect("notes")}></input>
                    <p style={{cursor:"pointer"}} data-tip={props.messages.fragment_tt_information} onClick={() => callbackInputSelect("notes")}>{props.messages.fragment_shownotes}</p>
                </div>
                <div className="inter-editorial-flex">
                    <input style={{cursor:"pointer"}} type="checkbox" checked={selected["facs"]} onChange={() => callbackInputSelect("facs")}></input>
                    <p style={{cursor:"pointer"}} data-tip={props.messages.fragment_tt_image} onClick={() => callbackInputSelect("facs")}>{props.messages.fragment_showfacs}</p>
                </div>
            </div>
            
            <span className="body-title">{props.data.inters[0].title}</span>

            {selected.facs?
                <Facsimilie data={props.data} messages={props.messages} callbackFacsimilieInputSelect={(pbText) => callbackFacsimilieInputSelect(pbText)}/>
                :<InterTranscript data={props.data} messages={props.messages} type="AUTHORIAL" />
            }
            
            
            <ReactTooltip backgroundColor="#000" textColor="#fff" border={true} borderColor="#000" className="virtual-tooltip" place="bottom" effect="solid"/>

        </div>
    )
}

export default InterAuthorial
