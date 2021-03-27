import React from 'react'
import Privacy_en from './Privacy_en'
import Privacy_es from './Privacy_es'
import Privacy_pt from './Privacy_pt'


const Privacy_main = (props) => {
    return(
        <div>
            <div className="container">
                <p className="conduct-title">{props.messages.header_privacy}</p>
                {props.language==="pt"?<Privacy_pt/>:props.language==="en"?<Privacy_en/>:<Privacy_es/>}
            </div>
        </div>
        
    )
}

export default Privacy_main
