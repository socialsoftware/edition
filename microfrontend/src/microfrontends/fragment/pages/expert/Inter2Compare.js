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
                <th>
                    <p>{inter.shortName}</p>
                    <p>{inter.title}</p>
                </th>
            )
        })
    }

    const mapInterToRows = (interp) => {
        return interp.map((val, i) => {
            return (
                <td dangerouslySetInnerHTML={{ __html: val }}></td>
            )
        })
    }
    const mapVariationsToTable = () => {
        return props.data.variations.map((interp, i) => {
            return (
                <tr>
                    {mapInterToRows(interp)}
                </tr>
                
            )
        })
    }

    return (
        <div>
            <div style={{display:"flex"}}>
                {props.data.inters.length === 2?
                    <div className="inter-editorial-flex">
                        <input type="checkbox" checked={selected["line"]} onChange={() => callbackInputSelect("line")}></input>
                        <p>{props.messages.fragment_linebyline}</p>
                    </div>
                :null
                }
                <div className="inter-editorial-flex" style={{marginLeft:"10px"}}>
                    <input type="checkbox" checked={selected["spaces"]} onChange={() => callbackInputSelect("spaces")}></input>
                    <p>{props.messages.fragment_alignspace}</p>
                </div>
            </div>
            {
                selected.line || props.data.inters.length>2?
                    <Inter2CompareLineByLine spaces={selected.spaces} data={props.data} messages={props.messages}/>
                :   <Inter2CompareSideBySide spaces={selected.spaces} data={props.data} messages={props.messages}/>
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