import React, { useEffect } from 'react'
import PrivacyEn from './Privacy_en'
import PrivacyEs from './Privacy_es'
import PrivacyPt from './Privacy_pt'


const Privacy_main = (props) => {

    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [])

    
    return(
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_privacy}</p>
                {props.language==="pt"?<PrivacyPt/>:props.language==="en"?<PrivacyEn/>:<PrivacyEs/>}
            </div>
        </div>
        
    )
}

export default Privacy_main
