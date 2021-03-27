import React from 'react'
import Conduct_en from './Conduct_en'
import Conduct_es from './Conduct_es'
import Conduct_pt from './Conduct_pt'


const Conduct_main = (props) => {
    return(
        <div>
            <div className="container">
                <p className="conduct-title">{props.messages.header_conduct}</p>
                {props.language==="pt"?<Conduct_pt/>:props.language==="en"?<Conduct_en/>:<Conduct_es/>}
            </div>
        </div>
        
    )
}

export default Conduct_main
