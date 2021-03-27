import React from 'react'
import Team_en from './Team_en'
import Team_es from './Team_es'
import Team_pt from './Team_pt'



const Team_main = (props) => {
    return(
        <div>
            <div className="container">
                <p className="conduct-title">{props.messages.header_faq}</p>
                {props.language==="pt"?<Team_pt/>:props.language==="en"?<Team_en/>:<Team_es/>}
            </div>
        </div>
        
    )
}

export default Team_main