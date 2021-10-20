import React, { useState } from 'react'
import Inter2CompareLineByLine from './Inter2CompareLineByLine'
import Inter2CompareSideBySide from './Inter2CompareSideBySide'

const Inter2Compare = (props) => {


    const [selected, setSelected] = useState({
        line: false,
        spaces: false,
    })

    const callbackInputSelect = (bool) => {
        let aux = {...selected}
        aux[bool] = !aux[bool]
        setSelected(aux)
        props.callbackCompareSelectedHandler(aux)
    }

    const mapShortNamesToTable = () => {
        return props.data.inters.map((inter, i) => {
            return (
                <th key={i}>
                    <p>{inter.shortName}</p>
                    <p>{inter.title}</p>
                </th>
            )
        })
    }

    const mapInterToRows = (interp) => {
        return interp.map((val, i) => {
            return (
                <td key={i} dangerouslySetInnerHTML={{ __html: val }}></td>
            )
        })
    }
    const mapVariationsToTable = () => {
        return props.data.variations.map((interp, i) => {
            return (
                <tr key={i}>
                    {mapInterToRows(interp)}
                </tr>
                
            )
        })
    }

    return (
        <div>
            <div className="inter-editorial-control-div">
                {props.data.inters.length === 2?
                    <div className="inter-editorial-flex">
                        <input style={{cursor:"pointer"}} type="checkbox" checked={selected["line"]} onChange={() => callbackInputSelect("line")}></input>
                        <p style={{cursor:"pointer"}} onClick={() => callbackInputSelect("line")}>{props.messages.fragment_linebyline}</p>
                    </div>
                :null
                }
                <div className="inter-editorial-flex">
                    <input style={{cursor:"pointer"}} type="checkbox" checked={selected["spaces"]} onChange={() => callbackInputSelect("spaces")}></input>
                    <p style={{cursor:"pointer"}} onClick={() => callbackInputSelect("spaces")}>{props.messages.fragment_alignspace}</p>
                </div>
            </div>
            {
                !selected.line && props.data.inters.length === 2?
                    <Inter2CompareSideBySide spaces={selected.spaces} data={props.data} messages={props.messages}/>:
                selected.line && props.data.inters.length === 2?
                    <Inter2CompareLineByLine spaces={selected.spaces} data={props.data} messages={props.messages}/>:
                props.data.inters.length > 2?
                    <Inter2CompareLineByLine spaces={selected.spaces} data={props.data} messages={props.messages}/>
                :null
            }
            <div style={{paddingBottom:"30px", maxWidth:"800px"}}>
                <p className="body-title" style={{textAlign:"left", margin:"0"}}>{props.messages.fragment_variationstable} ({props.data.variations?props.data.variations.length:0})</p>
                <table>
                    <thead>
                        <tr>
                        {
                            props.data?
                            mapShortNamesToTable()
                            :null
                        }
                        </tr>
                    </thead>
                    <tbody>
                        {
                            props.data?
                            mapVariationsToTable()
                            :null
                        }
                    </tbody>
                </table>
            </div>
        </div>
    )
}

export default Inter2Compare