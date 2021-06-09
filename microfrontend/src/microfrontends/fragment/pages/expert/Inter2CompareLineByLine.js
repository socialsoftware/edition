import React from 'react'

const Inter2CompareLineByLine = (props) => {
    return (
        <div className="fragment-side">
            <div className="well" style={{fontSize:"14px", lineHeight:"1.42857", fontFamily:props.spaces?"monospace":"georgia"}} dangerouslySetInnerHTML={{ __html: props.data.writerLineByLine }}></div>
        </div>
    )
}

export default Inter2CompareLineByLine