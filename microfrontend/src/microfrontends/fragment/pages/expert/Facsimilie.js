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
        console.log(props.data);
        if(props.data.surface!==null){
            try{
                imgsAux.push(
                    {
                        original: require(`../../../../resources/assets/facsimilies/${props.data.surface.graphic}`).default,
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
                    <img src={leftArrow} className="fragment-facsimilie-arrow-left" onClick={() => getSurfaceHandler("left")}></img>
                    :null
                }
                {
                    props.data.nextSurface?
                    <img src={rightArrow} className="fragment-facsimilie-arrow-right" onClick={() => getSurfaceHandler("right")}></img>
                    :null
                }
            </div>
            
        )
    }

    const props2 = {width: 350, height: 400, zoomWidth: 350, img: require(`./../../../../resources/assets/facsimilies/bn-acpc-e-e3-1-1-89_0001_1_t24-C-R0150.jpg`), zoomPosition:"original"};
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