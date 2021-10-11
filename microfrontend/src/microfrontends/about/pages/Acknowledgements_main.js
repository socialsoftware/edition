import React, { useEffect } from 'react'
import AcknowledgementsEn from './Acknowledgements_en'
import AcknowledgementsEs from './Acknowledgements_es'
import AcknowledgementsPt from './Acknowledgements_pt'


const Acknowledgements_main = (props) => {

    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [])

    
    return (
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_acknowledgements}</p>
                {props.language==="pt"?<AcknowledgementsPt/>:props.language==="en"?<AcknowledgementsEn/>:<AcknowledgementsEs/>}
            </div>
        </div>
    )
}

export default Acknowledgements_main