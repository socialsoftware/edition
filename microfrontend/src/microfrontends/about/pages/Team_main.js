import React, { useEffect } from 'react'
import TeamEn from './Team_en'
import TeamEs from './Team_es'
import TeamPt from './Team_pt'

const Team_main = (props) => {

    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [])


    return(
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_team}</p>
                {props.language==="pt"?<TeamPt/>:props.language==="en"?<TeamEn/>:<TeamEs/>}
            </div>
        </div>
        
    )
}

export default Team_main