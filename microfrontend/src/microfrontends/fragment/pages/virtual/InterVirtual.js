import React from 'react'
import Taxonomy from './Taxonomy'
import he from 'he'

const InterVirtual = (props) => {
    return (
        <div>
            <p className="body-title" style={{margin:0, textAlign:"left"}}>{he.decode(props.data.inters[0].editionTitle)}</p>
            <p className="body-title" style={{marginTop:"20px"}}>{props.data.inters[0].title}</p>

            <div className="well" style={{fontFamily:"courier"}} dangerouslySetInnerHTML={{ __html: props.data.transcript }} />

            <Taxonomy data={props.data}/>
        </div>
    )
}

export default InterVirtual