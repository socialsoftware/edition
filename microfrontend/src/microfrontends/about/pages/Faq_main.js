import React from 'react'
import Faq_en from './Faq_en'
import Faq_es from './Faq_es'
import Faq_pt from './Faq_pt'



const Faq_main = (props) => {
    return(
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_faq}</p>
                {props.language==="pt"?<Faq_pt/>:props.language==="en"?<Faq_en/>:<Faq_es/>}
            </div>
        </div>
        
    )
}

export default Faq_main
