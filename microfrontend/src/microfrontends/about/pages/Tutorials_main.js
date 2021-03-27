import React from 'react'
import Tutorials_en from './Tutorials_en'
import Tutorials_es from './Tutorials_es'
import Tutorials_pt from './Tutorials_pt'


const Tutorials_main = (props) => {
    return(
        <div>
            <div className="container">
                <p className="conduct-title">{props.messages.header_tutorials}</p>
                {props.language==="pt"?<Tutorials_pt/>:props.language==="en"?<Tutorials_en/>:<Tutorials_es/>}
            </div>
        </div>
        
    )
}

export default Tutorials_main