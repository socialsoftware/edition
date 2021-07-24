import React from 'react'
import FragmentInterMetaInfo from './FragmentInterMetaInfo'

const Inter2CompareSideBySide = (props) => {

    const mapIntersToView = () => {
        return props.data.inters.map((inter, i) => {
            return (
                <div key={i} className="fragment-side-interp">
                    <p className="body-title" style={{textAlign:"left", fontSize:"17px", margin:"0"}}>{inter.title}</p>
                    <div className="well" style={{fontFamily:props.spaces?"monospace":"georgia", marginTop:"10px"}} dangerouslySetInnerHTML={{ __html: props.data.setTranscriptionSideBySide[i] }} />
                </div>
                
            )
        })
    }

    const mapMetaInfoToView = () => {
        return props.data.inters.map((inter, i) => {
            return (
                <div key={i} className="fragment-side-interp">
                    <div className="inter-meta-info">
                        {
                            inter.type==="AUTHORIAL"?
                            <FragmentInterMetaInfo
                                sourceList={props.data.fragment.sourceInterDtoList[0]}
                                messages={props.messages}
                            />:
                            <FragmentInterMetaInfo
                                expertEditionMap={inter}
                                messages={props.messages}
                            />
                        }
                        
                    </div>
                </div>
                
                
            )
        })
    }

    return (
        <div>
            <div className="fragment-side">
                {
                    props.data?
                    mapIntersToView()
                    :null
                }
            </div>
            <div className="fragment-side">
            {
                props.data?
                mapMetaInfoToView()
                :null
            }
            </div>
        </div>
    )
}

export default Inter2CompareSideBySide