import React from 'react'
import Acknowledgements_en from './Acknowledgements_en'
import Acknowledgements_es from './Acknowledgements_es'
import Acknowledgements_pt from './Acknowledgements_pt'


const Acknowledgements_main = (props) => {
    return (
        <div>
            <div className="about-container">
                <p className="about-conduct-title">{props.messages.header_acknowledgements}</p>
                {props.language==="pt"?<Acknowledgements_pt/>:props.language==="en"?<Acknowledgements_en/>:<Acknowledgements_es/>}
            </div>
        </div>
    )
}

export default Acknowledgements_main