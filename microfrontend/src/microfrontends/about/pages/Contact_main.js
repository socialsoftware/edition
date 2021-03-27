import React from 'react'
import Contact_en from './Contact_en'
import Contact_es from './Contact_es'
import Contact_pt from './Contact_pt'


const Contact_main = (props) => {
    return(
        <div>
            <div className="container">
                <p className="conduct-title">{props.messages.header_contact}</p>
                {props.language==="pt"?<Contact_pt/>:props.language==="en"?<Contact_en/>:<Contact_es/>}
            </div>
        </div>
        
    )
}

export default Contact_main
