import React, { useEffect, useState } from 'react'
import InterEmpty from '../InterEmpty'
import Inter2Compare from './Inter2Compare'
import InterAuthorial from './InterAuthorial'
import InterEditorial from './InterEditorial'
import { getIntersByArrayExternalId, getInterWithDiff, 
    getAuthorialInterWithDiffs, getInterCompare} from '../../../../util/API/FragmentAPI'

const Body_expert = (props) => {

    const [data, setData] = useState(null)
    
    useEffect(() => {
        getIntersByArrayExternalId(props.externalId, props.selectedInters)
            .then(res => {
                dataHandler(res.data)
            })
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [props.externalId, props.selectedInters])

    const callbackDiffHandler = (bool) => {
        let array = []
        array.push(data.inters[0].externalId)
        getInterWithDiff(array, bool)
            .then(res => {
                dataHandler(res.data)
            })
    }

    const callbackSelectedHandler = (obj) => {
        let array = []
        array.push(data.inters[0].externalId)
        getAuthorialInterWithDiffs(array, obj)
            .then(res => {
                dataHandler(res.data)
            })
    }

    const callbackCompareSelectedHandler = (obj) => {
        let array = []
        for(let el of data.inters){
            array.push(el.externalId)
        }
        getInterCompare(array, obj)
            .then(res => {
                dataHandler(res.data)
            })
    }

    const dataHandler = (obj) => {
        if(data === null) setData(obj)
        else{
            let aux = {...data}
            if(obj["fragment"]!==null) aux["fragment"] = obj["fragment"]
            if(obj["ldoD"]!==null) aux["ldoD"] = obj["ldoD"]
            if(obj["ldoDuser"]!==null) aux["ldoDuser"] = obj["ldoDuser"]
            if(obj["inters"]!==null) aux["inters"] = obj["inters"]
            if(obj["hasAccess"]!==null) aux["hasAccess"] = obj["hasAccess"]
            if(obj["transcript"]!==null) aux["transcript"] = obj["transcript"]
            if(obj["setTranscriptionSideBySide"]!==null) aux["setTranscriptionSideBySide"] = obj["setTranscriptionSideBySide"]
            if(obj["variations"]!==null) aux["variations"] = obj["variations"]
            if(obj["writerLineByLine"]!==null) aux["writerLineByLine"] = obj["writerLineByLine"]
            if(obj["surface"]!==null) aux["surface"] = obj["surface"]
            aux["nextPb"] = obj["nextPb"]
            aux["nextSurface"] = obj["nextSurface"]
            aux["prevPb"] = obj["prevPb"]
            aux["prevSurface"] = obj["prevSurface"]
            setData(aux)
        }
    }


    return (
        <div>
            {
            data && data.inters?data.inters.length===1?
                data.inters[0].type==="EDITORIAL"?
                    <InterEditorial data={data} messages={props.messages} callbackDiffHandler={(bool) => callbackDiffHandler(bool)}/>  
                :   data.inters[0].type==="AUTHORIAL"?
                    <InterAuthorial data={data} messages={props.messages} callbackSelectedHandler={(obj) => callbackSelectedHandler(obj)}/>
                    :null
                :   data.inters.length>1 && (data.inters[0].type==="EDITORIAL" || data.inters[0].type==="AUTHORIAL")?
                        <Inter2Compare data={data} callbackCompareSelectedHandler={(obj) => callbackCompareSelectedHandler(obj)} messages={props.messages}/>
                    :!data.inters?
                        <InterEmpty/>
                        :null
                :  null
                        
            }
        </div>
    )
}

export default Body_expert