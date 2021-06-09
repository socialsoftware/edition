import React from 'react'
import InterMetaInfo from '../../../documents/pages/InterMetaInfo'

const InterTranscript = (props) => {
    
    return (
        <div>
            
            {props.type==="AUTHORIAL"?
                <div className="well" style={{fontFamily:"courier"}} dangerouslySetInnerHTML={{ __html: props.data.transcript }} />
                :<div className="well" style={{fontFamily:"georgia"}} dangerouslySetInnerHTML={{ __html: props.data.transcript }} />
            }

            <div className="inter-meta-info">
                {
                    props.type==="AUTHORIAL"?
                    <InterMetaInfo
                        sourceList={props.data.fragment.sourceInterDtoList[0]}
                        messages={props.messages}
                    />:
                    <InterMetaInfo
                        expertEditionMap={props.inter}
                        messages={props.messages}
                    />
                }
                
            </div>
        </div>
    )
}

export default InterTranscript