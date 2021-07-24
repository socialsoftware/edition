import React, { useEffect, useState } from 'react'
import { getLdoDVisual } from '../../../util/API/ReadingAPI';

const LdoDVisual = () => {
    
    const [aux, setAux] = useState(null)

    useEffect(() => {
        getLdoDVisual()
            .then(res => {
                setAux(res)
            })
    }, [])

    return (
        <div>
            <p>temporariamente indisponível</p>
        </div>
    )
}

export default LdoDVisual