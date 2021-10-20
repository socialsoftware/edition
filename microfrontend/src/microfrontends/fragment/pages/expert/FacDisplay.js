import React, { useEffect, useState } from 'react'
import noImage from '../../../../resources/assets/no_image.png'
import axios from 'axios'

const FacDisplay = (props) => {

    const [source, setSource] = useState(null)

    const getImage = (url) => {
        axios.get(url, { responseType:"blob" })
            .then(function (response) {
                var reader = new window.FileReader();
                reader.readAsDataURL(response.data); 
                reader.onload = function() {
                    var imageDataUrl = reader.result;
                    setSource(imageDataUrl)
                }
            })
            .catch(() => {
                setSource(noImage)
            })
    }

    useEffect(() => {
        let url = `https://ldod.uc.pt/facs/${props.url}`
        getImage(url)
        document.body.style.overflow = "hidden"
    }, [props.url])
    
    const removeHandler = () => {
        document.body.style.overflow = "scroll"
        setSource(null)
        props.removeFac()
    }
    return (
            source?
                <div className="fac-display">
                    <img alt="fac" className="fac-display-image" src={source}></img>
                    <p className="fac-display-x" onClick={() => removeHandler()}>X</p>
                </div>
            :null
            
    )
}

export default FacDisplay