import React from 'react'
import InterEmpty from '../InterEmpty'
import Inter2Compare from './Inter2Compare'
import InterAuthorial from './InterAuthorial'
import InterEditorial from './InterEditorial'

const Body_expert = (props) => {
    return (
        <div>
            {
            props.data && props.data.inters?props.data.inters.length===1?
                props.data.inters[0].type==="EDITORIAL"?
                    <InterEditorial data={props.data} messages={props.messages} callbackDiffHandler={(bool) => props.callbackDiffHandler(bool)}/>  
                :   props.data.inters[0].type==="AUTHORIAL"?
                    <InterAuthorial data={props.data} messages={props.messages} callbackSelectedHandler={(obj) => props.callbackSelectedHandler(obj)}/>
                    :null
                :   props.data.inters.length>1 && (props.data.inters[0].type==="EDITORIAL" || props.data.inters[0].type==="AUTHORIAL")?
                        <Inter2Compare data={props.data} callbackCompareSelectedHandler={(obj) => props.callbackCompareSelectedHandler(obj)} messages={props.messages}/>
                    :!props.data.inters?
                        <InterEmpty/>
                        :null
                :  null
                        
            }
        </div>
    )
}

export default Body_expert