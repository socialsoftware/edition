import React, { useEffect, useState } from 'react';
import ImageGallery from 'react-image-gallery';
import FragmentInterMetaInfo from './FragmentInterMetaInfo'
import rightArrow from '../../../../resources/assets/right_arrow.svg'
import leftArrow from '../../../../resources/assets/left_arrow.svg'
import noImage from '../../../../resources/assets/no_image.png'

const Facsimilie = (props) => {

    const [images, setImages] = useState([])
    
    useEffect(() => {
        var imgsAux = [...images]
        imgsAux = []
        if(props.data.surface!==null){
            try{
                imgsAux.push(
                    {
                        original: require(`../../../../resources/assets/facsimilies/${props.data.surface.graphic}`).default,
                    }
                )
            }
            catch (err){
                imgsAux.push(
                    {
                        original: noImage,
                    }
                )
            }
            setImages(imgsAux)
        }
        
    }, [props.data, images])


    const getSurfaceHandler = (side) => {
        if(side === "left") props.callbackFacsimilieInputSelect(props.data.prevPb)
        if(side === "right") props.callbackFacsimilieInputSelect(props.data.nextPb)
    }

    const customControls = () => {
        return (
            <div>
                {
                    props.data.prevSurface?
                    <img alt="arrow" src={leftArrow} className="fragment-facsimilie-arrow-left" onClick={() => getSurfaceHandler("left")}></img>
                    :null
                }
                {
                    props.data.nextSurface?
                    <img alt="arrow" src={rightArrow} className="fragment-facsimilie-arrow-right" onClick={() => getSurfaceHandler("right")}></img>
                    :null
                }
            </div>
            
        )
    }

    return (
        <div>
            <div className="fragment-facsimilie-div">
                <div className="fragment-facsimilie-img">
                    <ImageGallery showThumbnails={false} items={images} showPlayButton={false} renderCustomControls={() => customControls()}/>
                </div>                    
                <div className="well well-fac" dangerouslySetInnerHTML={{ __html: props.data.transcript }} />
            </div>
            <div className="inter-meta-info">
                <FragmentInterMetaInfo
                        sourceList={props.data.fragment.sourceInterDtoList[0]}
                        messages={props.messages}
                    />
            </div>
            
        </div>
    )
}

export default Facsimilie