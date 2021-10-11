import React, { useEffect } from 'react'
import CopyrightEn from './Copyright_en'
import CopyrightEs from './Copyright_es'
import CopyrightPt from './Copyright_pt'


const Copyright_main = (props) => {

    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [])

    
    return(
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_copyright}</p>
                {props.language==="pt"?<CopyrightPt/>:props.language==="en"?<CopyrightEn/>:<CopyrightEs/>}
            </div>
        </div>
        
    )
}

export default Copyright_main


