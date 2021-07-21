import React from 'react'
import Articles_en from './Articles_en'
import Articles_es from './Articles_es'
import Articles_pt from './Articles_pt'



const Articles_main = (props) => {
    return(
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_bibliography}</p>
                {props.language==="pt"?<Articles_pt/>:props.language==="en"?<Articles_en/>:<Articles_es/>}
            </div>
        </div>
        
    )
}

export default Articles_main