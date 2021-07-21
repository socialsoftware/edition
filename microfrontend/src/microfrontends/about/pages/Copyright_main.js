import React from 'react'
import Copyright_en from './Copyright_en'
import Copyright_es from './Copyright_es'
import Copyright_pt from './Copyright_pt'


const Copyright_main = (props) => {
    return(
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_copyright}</p>
                {props.language==="pt"?<Copyright_pt/>:props.language==="en"?<Copyright_en/>:<Copyright_es/>}
            </div>
        </div>
        
    )
}

export default Copyright_main


