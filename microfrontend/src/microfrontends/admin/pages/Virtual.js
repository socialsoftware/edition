import React, { useEffect, useState } from 'react'
import { getAdminVirtualList, deleteAdminVirtualEdition } from '../../../util/API/AdminAPI';
import CircleLoader from "react-spinners/RotateLoader";
import he from 'he'

const Virtual = (props) => {

    const [list, setList] = useState([])

    useEffect(() => {
        getAdminVirtualList()
            .then(res => {
                setList(res.data)
            })
    }, [])

    const mapEditorsToTable = (edition) => {
        return edition.participantSet.map((participant, i) => {
            return (
                <span key={i}>{participant.userName} </span>
            )
        })
    }
    const mapCategoriesToTable = (edition) => {
        return edition.categorySet.map((category, i) => {
            return (
                <span key={i}>{category.name}, </span>
            )
        })
    }
    const mapTextsToTable = (edition) => {
        return edition.annotationTextList.map((text, i) => {
            return (
                <span key={i}>{text}, </span>
            )
        })
    }

    const deleteVirtualEditionHandler = (externalId) => {
        deleteAdminVirtualEdition(externalId)
            .then(res => {
                setList(res.data)
            })
    }

    const mapEditionsToTable = () => {
        return list.map((edition, i) => {
            return (
                <tr key={i}>
                    <td>{edition.acronym}</td>
                    <td style={{maxWidth:"180px"}}>{he.decode(edition.title)}</td>
                    <td style={{maxWidth:"180px"}}>
                        {mapEditorsToTable(edition)}
                    </td>
                    <td>
                        {mapCategoriesToTable(edition)}
                    </td>
                    <td>
                        {mapTextsToTable(edition)}
                    </td>
                    <td style={{padding:"8px"}}>
                        <span className="admin-delete-button" onClick={() => deleteVirtualEditionHandler(edition.externalId)}>
                            <p style={{marginRight:"5px"}}>X</p>
                            <p style={{fontWeight:400}}>{props.messages.general_remove}</p>
                        </span>
                    </td>
                </tr>
            )
        })
    }
    
    return (
        <div className="admin">
            <p className="admin-title">Manage Virtual Editions</p>
            {
                list.length>0?
                    <table className="admin-table">
                        <thead>
                            <tr>
                                <th>Acronym</th>
                                <th>Title</th>
                                <th>Editors</th>
                                <th>Categories</th>
                                <th>Annotations</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {mapEditionsToTable()}
                        </tbody>
                    </table>
                :
                <div style={{marginTop:"50px"}}>
                    <CircleLoader loading={list.length===0}></CircleLoader>
                </div>
            }
        </div>
    )
}

export default Virtual