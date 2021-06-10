import React, { useEffect, useState } from 'react'
import noImage from '../../../../resources/assets/no_image.png'

const FacDisplay = (props) => {

    const [source, setSource] = useState(null)

    const getImage = (url) => {
        try{
            setSource(require(`../../../../resources/assets/facsimilies/${url}`).default)
        }
        catch (err){
            setSource(noImage)
        }
    }

    useEffect(() => {
        console.log("oioi");
        getImage(props.url)
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
                    <img className="fac-display-image" src={source}></img>
                    <p className="fac-display-x" onClick={() => removeHandler()}>X</p>
                </div>
            :null
            
    )
}

export default FacDisplay