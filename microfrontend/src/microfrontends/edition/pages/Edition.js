import React from 'react'
import { useHistory } from 'react-router'

const Edition = (props) => {

    const history = useHistory()

    return(
        <div className="edition-editionTop">
            <p className="edition-intro-title">{props.messages.header_editions}</p>
            <div className="edition-edition-div">
                <div onClick={() => {history.push("/edition/acronym/JPC")}} className="edition-div-link">
                    <img className="edition-img" src={require(`../../../resources/assets/graphics/JPC.png`).default} alt="img1"></img>
                    <img className="edition-img-hover" src={require(`../../../resources/assets/graphics/JPCH.png`).default} alt="img1"></img>
                </div>
                <div onClick={() => {history.push("/edition/acronym/TSC")}} className="edition-div-link">
                    <img className="edition-img" src={require(`../../../resources/assets/graphics/TSC.png`).default} alt="img1"></img>
                    <img className="edition-img-hover" src={require(`../../../resources/assets/graphics/TSCH.png`).default} alt="img1"></img>
                </div>
                <div onClick={() => {history.push("/edition/acronym/RZ")}} className="edition-div-link">
                    <img className="edition-img" src={require(`../../../resources/assets/graphics/RZ.png`).default} alt="img1"></img>
                    <img className="edition-img-hover" src={require(`../../../resources/assets/graphics/RZH.png`).default} alt="img1"></img>
                </div>
                <div onClick={() => {history.push("/edition/acronym/JP")}} className="edition-div-link">
                    <img className="edition-img" src={require(`../../../resources/assets/graphics/JP.png`).default} alt="img1"></img>
                    <img className="edition-img-hover" src={require(`../../../resources/assets/graphics/JPH.png`).default} alt="img1"></img>
                </div>
                <div onClick={() => {history.push("/edition/acronym/LdoD-Arquivo")}} className="edition-div-link">
                    <img className="edition-img" src={require(`../../../resources/assets/graphics/ALdod.png`).default} alt="img1"></img>
                    <img className="edition-img-hover" src={require(`../../../resources/assets/graphics/ALdodH.png`).default} alt="img1"></img>
                </div>
            </div>
            
        </div>
    )
}

export default Edition