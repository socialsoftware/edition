import React from 'react'
import Archive_en from './Archive_en'
import Archive_es from './Archive_es'
import Archive_pt from './Archive_pt'


const Archive_main = (props) => {
    return(
        <div>
            <div className="container" style={{marginTop:"100px"}}>
                {props.language==="pt"?<Archive_pt/>:props.language==="en"?<Archive_en/>:<Archive_es/>}
            </div>
        </div>
        
    )
}

export default Archive_main
