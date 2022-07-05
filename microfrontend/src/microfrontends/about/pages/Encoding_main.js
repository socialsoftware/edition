import React, { useEffect } from 'react'
import EncodingEn from './Encoding_en'
import EncodingEs from './Encoding_es'
import EncodingPt from './Encoding_pt'

const Encoding_main = (props) => {

    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [])

    
    return(
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_encoding}</p>
                {props.language==="pt"?<EncodingPt/>:props.language==="en"?<EncodingEn/>:<EncodingEs/>}
            </div>
        </div>
        
    )
}

export default Encoding_main


