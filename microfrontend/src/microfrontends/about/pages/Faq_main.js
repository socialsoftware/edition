import React, { useEffect } from 'react'
import FaqEn from './Faq_en'
import FaqEs from './Faq_es'
import FaqPt from './Faq_pt'

const Faq_main = (props) => {

    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [])

    
    return(
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_faq}</p>
                {props.language==="pt"?<FaqPt/>:props.language==="en"?<FaqEn/>:<FaqEs/>}
            </div>
        </div>
        
    )
}

export default Faq_main
