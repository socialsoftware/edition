import React from 'react'
import FragmentInterMetaInfo from './FragmentInterMetaInfo'

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
                    <FragmentInterMetaInfo
                        sourceList={props.data.fragment.sourceInterDtoList[0]}
                        messages={props.messages}
                    />:
                    <FragmentInterMetaInfo
                        expertEditionMap={props.inter}
                        messages={props.messages}
                    />
                }
                
            </div>
        </div>
    )
}

export default InterTranscript