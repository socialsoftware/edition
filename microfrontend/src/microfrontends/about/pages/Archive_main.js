import React from 'react'
import ArchiveEn from './Archive_en'
import ArchiveEs from './Archive_es'
import ArchivePt from './Archive_pt'


const Archive_main = (props) => {
    return(
        <div>
            <div className="about-container" style={{marginTop:"100px"}}>
                {props.language==="pt"?<ArchivePt/>:props.language==="en"?<ArchiveEn/>:<ArchiveEs/>}
            </div>
        </div>
        
    )
}

export default Archive_main
