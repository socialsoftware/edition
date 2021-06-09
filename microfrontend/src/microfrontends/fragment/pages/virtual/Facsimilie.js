import React, { useEffect, useState } from 'react';
import ImageGallery from 'react-image-gallery';
import InterMetaInfo from '../../../documents/pages/InterMetaInfo'
// @ts-ignore
import rightArrow from '../../../../resources/assets/right_arrow.svg'
// @ts-ignore
import leftArrow from '../../../../resources/assets/left_arrow.svg'
// @ts-ignore
import noImage from '../../../../resources/assets/no_image.png'
const Facsimilie = (props) => {

    const [images, setImages] = useState([])
    
    useEffect(() => {
        var imgsAux = [...images]
        imgsAux = []
        console.log(props.data);
        if(props.data.surface!==null){
            try{
                imgsAux.push(
                    {
                        original: require(`../../../../resources/assets/facsimilies/${props.data.surface.graphic}`),
                    }
                )
            }
            catch (err){
                console.log("cannot find image");
                imgsAux.push(
                    {
                        original: noImage,
                    }
                )
            }
            setImages(imgsAux)
        }
        
    }, [props.data])

    const getSurfaceHandler = (side) => {
        if(side === "left") props.callbackFacsimilieInputSelect(props.data.prevPb)
        if(side === "right") props.callbackFacsimilieInputSelect(props.data.nextPb)
    }

    const customControls = () => {
        return (
            <div>
                {
                    props.data.prevSurface?
                    <img src={leftArrow} style={{width:"50px", height:"50px"}} className="fragment-facsimilie-arrow-left" onClick={() => getSurfaceHandler("left")}></img>
                    :null
                }
                {
                    props.data.nextSurface?
                    <img src={rightArrow} style={{width:"50px", height:"50px"}} className="fragment-facsimilie-arrow-right" onClick={() => getSurfaceHandler("right")}></img>
                    :null
                }
            </div>
            
        )
    }

    return (
        <div>
            <div className="fragment-facsimilie-div">
                <div className="fragment-facsimilie-img">
                    <ImageGallery items={images} showPlayButton={false} renderCustomControls={() => customControls()}/>
                </div>                    
                <div className="well well-fac" dangerouslySetInnerHTML={{ __html: props.data.transcript }} />
            </div>
            <div className="inter-meta-info">
                <InterMetaInfo
                        sourceList={props.data.fragment.sourceInterDtoList[0]}
                        messages={props.messages}
                    />
            </div>
            
        </div>
    )
}

export default Facsimilie