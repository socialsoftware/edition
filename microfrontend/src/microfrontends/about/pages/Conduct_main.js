import React, {useEffect} from 'react'
import ConductEn from './Conduct_en'
import ConductEs from './Conduct_es'
import ConductPt from './Conduct_pt'


const Conduct_main = (props) => {

    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [])

    return(
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_conduct}</p>
                {props.language==="pt"?<ConductPt/>:props.language==="en"?<ConductEn/>:<ConductEs/>}
            </div>
        </div>
        
    )
}

export default Conduct_main
