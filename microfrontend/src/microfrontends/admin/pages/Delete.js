import React, { useEffect, useState } from 'react'
import { deleteSingleFragment, getFragmentDeleteList, deleteAllFragment } from '../../../util/API/AdminAPI';
import CircleLoader from "react-spinners/RotateLoader";

const Delete = (props) => {

    const [list, setList] = useState([])

    useEffect(() => {
        getFragmentDeleteList()
            .then(res => {
                setList(res.data)
            })
    }, [])

    const deleteFragmentHandler = (externalId) => {
        deleteSingleFragment(externalId)
            .then(res => {
                setList(res.data)
            })
    }

    const deleteAllFragmentHandler = () => {
        deleteAllFragment()
            .then(res => {
                setList(res.data)
            })
    }

    const mapFragmentsToTable = () => {
        return list.map((frag, i) => {
            return (
                <tr key={i}>
                    <td style={{textAlign:"left"}}>
                        {frag.title}
                    </td>
                    <td>
                        <span className="admin-delete-button" onClick={() => deleteFragmentHandler(frag.externalId)}>
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
            <p className="admin-title">{props.messages.deletefragment_title}</p>

        {
            list.length>0?
            <div>
                <table className="admin-table">
                    <thead>
                        <tr>
                            <th style={{borderRight:"none"}}>{props.messages.general_title}</th>
                            <th>
                                <span className="admin-delete-button" onClick={() => deleteAllFragmentHandler()}>
                                    <p style={{marginRight:"5px"}}>X</p>
                                    <p style={{fontWeight:400}}>{props.messages.general_removeAll}</p>
                                </span>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        {mapFragmentsToTable()}
                    </tbody>
                </table>
            </div>
            
            :
            <div style={{marginTop:"50px"}}>
                <CircleLoader loading={list.length===0}></CircleLoader>
            </div>
        }
        </div>
    )
}

export default Delete