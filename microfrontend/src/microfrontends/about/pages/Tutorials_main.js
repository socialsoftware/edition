import React, { useEffect } from 'react'
import TutorialsEn from './Tutorials_en'
import TutorialsEs from './Tutorials_es'
import TutorialsPt from './Tutorials_pt'


const Tutorials_main = (props) => {

    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [])

    
    return(
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_tutorials}</p>
                {props.language==="pt"?<TutorialsPt/>:props.language==="en"?<TutorialsEn/>:<TutorialsEs/>}
            </div>
        </div>
        
    )
}

export default Tutorials_main