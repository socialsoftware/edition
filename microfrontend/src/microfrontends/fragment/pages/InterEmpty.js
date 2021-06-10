import React from 'react'

const InterEmpty = (props) => {
    return (
            props.data?props.data.inters?props.data.inters.length===0?
            <div>
                <p className="body-title">{props.data.fragment.title}</p>
            </div>
            :null:null:null
    )
}

export default InterEmpty