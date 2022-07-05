import React, { useEffect, useState } from 'react'
import noImage from '../../../resources/assets/no_image.png'
import axios from 'axios'


const FacDisplayDocuments = (props) => {

    const [source, setSource] = useState(null)

    const getImage = (url) => {
        axios.get(url, { responseType:"blob" })
            .then(function (response) {
                var reader = new window.FileReader();
                reader.readAsDataURL(response.data); 
                reader.onload = function() {
                    var imageDataUrl = reader.result;
                    setSource(imageDataUrl)
                    props.loadingHandler()
                }
            })
            .catch(() => {
                setSource(noImage)
                props.loadingHandler()
            })
    }

    useEffect(() => {
        let url = `https://ldod.uc.pt/facs/${props.url}`
        getImage(url)
        document.body.style.overflow = "hidden"
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [props.url])
    
    const removeHandler = () => {
        document.body.style.overflow = "scroll"
        setSource(null)
        props.removeFac()
    }
    return (
            source?
                <div className="document-fac-display">                  
                    <img alt="fac" className="document-fac-display-image" src={source}></img>
                    <p className="document-fac-display-x" onClick={() => removeHandler()}>X</p>
                </div>
            :null
            
    )
}

export default FacDisplayDocuments