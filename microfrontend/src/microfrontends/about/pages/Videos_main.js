import React from 'react'
import Videos_en from './Videos_en'
import Videos_es from './Videos_es'
import Videos_pt from './Videos_pt'


const Videos_main = (props) => {
    return(
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_videos}</p>
                {props.language==="pt"?<Videos_pt/>:props.language==="en"?<Videos_en/>:<Videos_es/>}
            </div>
        </div>
        
    )
}

export default Videos_main