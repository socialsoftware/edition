import React from 'react'
import Encoding_en from './Encoding_en'
import Encoding_es from './Encoding_es'
import Encoding_pt from './Encoding_pt'



const Encoding_main = (props) => {
    return(
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_encoding}</p>
                {props.language==="pt"?<Encoding_pt/>:props.language==="en"?<Encoding_en/>:<Encoding_es/>}
            </div>
        </div>
        
    )
}

export default Encoding_main


