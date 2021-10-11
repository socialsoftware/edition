import React, { useEffect } from 'react'
import ArchiveEn from './Archive_en'
import ArchiveEs from './Archive_es'
import ArchivePt from './Archive_pt'


const Archive_main = (props) => {

    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [])

    
    return(
        <div>
            <div className="about-container" style={{marginTop:"100px"}}>
                {props.language==="pt"?<ArchivePt/>:props.language==="en"?<ArchiveEn/>:<ArchiveEs/>}
            </div>
        </div>
        
    )
}

export default Archive_main
