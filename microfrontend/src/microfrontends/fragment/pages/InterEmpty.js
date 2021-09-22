import React, { useEffect, useState } from 'react'
import { getFragmentWithXmlNoUser } from '../../../util/API/FragmentAPI'

const InterEmpty = (props) => {

    const [title, setTitle] = useState(null)

    useEffect(() => {
        getFragmentWithXmlNoUser(props.xmlId, props.selectedVEAcr)
        .then(res => {
            if(res.data)
                setTitle(res.data.fragment.title)
        })      
    }, [props.xmlId, props.selectedVEAcr])

    useEffect(() => {
        getFragmentWithXmlNoUser(props.xmlIdVirtualToExpert, props.selectedVEAcr)
            .then(res => {
                if(res.data)
                    setTitle(res.data.fragment.title)
            })            
    }, [props.xmlIdVirtualToExpert, props.selectedVEAcr])

    useEffect(() => {
        getFragmentWithXmlNoUser(props.xmlIdExpertToVirtual, props.selectedVEAcr)
        .then(res => {
            if(res.data)
                setTitle(res.data.fragment.title)
        })
    }, [props.xmlIdExpertToVirtual, props.selectedVEAcr])

    return (
            props.selectedInters.length===0?
                <div>
                    <p className="body-title">{title}</p>
                </div>
            :null
    )
}

export default InterEmpty