import React from 'react'
import ContactEn from './Contact_en'
import ContactEs from './Contact_es'
import ContactPt from './Contact_pt'


const Contact_main = (props) => {
    return(
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_contact}</p>
                {props.language==="pt"?<ContactPt/>:props.language==="en"?<ContactEn/>:<ContactEs/>}
            </div>
        </div>
        
    )
}

export default Contact_main
