import React from 'react'
import InterVirtual from './InterVirtual'
import Virtual2Compare from './Virtual2Compare'

const Body_virtual = (props) => {
    return (
        <div>
            {props.data && props.data.inters? props.data.inters.length>0?
                props.data.inters[0].type==="VIRTUAL"?
                    props.data.inters.length===1?
                    <InterVirtual data={props.data} messages={props.messages}/>  
                    :<Virtual2Compare data={props.data} messages={props.messages}/>
                    :null
                    :null
                    :null
            }
            
        </div>
    )
}

export default Body_virtual