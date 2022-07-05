import React, { useState } from 'react'
import { useHistory } from 'react-router-dom'
import icon from '../../../../resources/assets/eye.svg'
import InterTranscript from './interTranscript'

const InterEditorial = (props) => {

    const history = useHistory()

    const [selectedDiff, setSelectedDiff] = useState(false)

    const callbackDiffHandler = () => {
        props.callbackDiffHandler(!selectedDiff)
        setSelectedDiff(!selectedDiff)
        
    }

    return (
        <div>
            <div className="inter-editorial-flex">
                <input type="checkbox" checked={selectedDiff} onChange={() => callbackDiffHandler()}></input>
                <p>{props.messages.fragment_highlightdifferences}</p>
            </div>
            <span><span className="body-title">{props.data.inters[0].title}</span><img alt="icon" onClick={() => history.push(`/reading/fragment/${props.data.fragment.fragmentXmlId}/inter/${props.data.inters[0].urlId}`)} className="fragment-icon" src={icon}></img></span>

            
            <InterTranscript data={props.data} messages={props.messages} inter={props.data.inters[0]} type="EDITORIAL"/>
        </div>
    )
}

export default InterEditorial
