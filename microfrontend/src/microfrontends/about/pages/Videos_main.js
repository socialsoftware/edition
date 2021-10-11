import React, { useEffect } from 'react'
import VideosEn from './Videos_en'
import VideosEs from './Videos_es'
import VideosPt from './Videos_pt'


const Videos_main = (props) => {

    useEffect(() => {
        window.scrollTo({top: 0, behavior: 'smooth'});
    }, [])

    
    return(
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_videos}</p>
                {props.language==="pt"?<VideosPt/>:props.language==="en"?<VideosEn/>:<VideosEs/>}
            </div>
        </div>
        
    )
}

export default Videos_main