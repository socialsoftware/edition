import React, { useEffect, useState } from 'react'
import InterVirtual from './InterVirtual'
import Virtual2Compare from './Virtual2Compare'
import { getVirtualIntersByArrayExternalId,
        getVirtualIntersByArrayExternalIdNoUser } from '../../../../util/API/FragmentAPI'

const Body_virtual = (props) => {

    const [data, setData] = useState(null)
    
    useEffect(() => {
        if(props.isAuthenticated){
            getVirtualIntersByArrayExternalId(props.externalId, props.selectedInters, props.selectedVEAcr)
            .then(res => {
                dataHandler(res.data)
            })
        }
        else{
            getVirtualIntersByArrayExternalIdNoUser(props.externalId, props.selectedInters, props.selectedVEAcr)
            .then(res => {
                dataHandler(res.data)
            })
        }
        
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [props.externalId, props.selectedInters, props.isAuthenticated])

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
            if(obj["virtualEditionsDto"]!==null) aux["virtualEditionsDto"] = obj["virtualEditionsDto"]
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
            {data && data.inters? data.inters.length>0?
                data.inters[0].type==="VIRTUAL"?
                    data.inters.length===1?
                    <InterVirtual data={data} messages={props.messages}/>  
                    :<Virtual2Compare data={data} messages={props.messages}/>
                    :null
                    :null
                    :null
            }
            
        </div>
    )
}

export default Body_virtual