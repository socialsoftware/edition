import React, { useEffect, useState } from 'react'
import { getUserList, changeActiveUser, deleteUser, deleteUserSessions, switchAdmin } from '../../../util/API/AdminAPI';
import { ReactComponent as Edit } from '../../../resources/assets/edit.svg'
import { useHistory } from 'react-router-dom';
import CircleLoader from "react-spinners/RotateLoader";

const User = (props) => {

    const history = useHistory()
    const [list, setList] = useState([])
    const [admin, setAdmin] = useState(null)
    const [sessionList, setSessionList] = useState([])

    useEffect(() => {
        getUserList()
            .then(res => {
                setList(res.data.userList)
                setSessionList(res.data.sessionList)
                setAdmin(res.data.ldoDAdmin)
                console.log(res.data);
            })
    }, [])

    const deleteUserHandler = (externalId) => {
        deleteUser(externalId)
            .then(res => {
                setList(res.data.userList)
            })
    }

    const changeActiveUserHandler = (externalId) => {
        changeActiveUser(externalId)
            .then(res => {
                setList(res.data.userList)
            })
    }
    
    const editUserHandler = (externalId) => {
        history.push(`/admin/user/edit/${externalId}`)
    }

    const deleteUserSessionsHandler = () => {
        deleteUserSessions()
            .then(res => {
                setSessionList(res.data.sessionList)
            })
    }

    const switchHandler = () => {
        switchAdmin()
            .then(res => {
                setAdmin(res.data.ldoDAdmin)
            })
    }
    const mapUsersToTable = () => {
        return list.map((user, i) => {
            return (
                <tr key={i}>
                    <td>{user.userName}</td>
                    <td>{user.firstName}</td>
                    <td>{user.lastName}</td>
                    <td>{user.email}</td>
                    <td>{user.enabled?props.messages.general_yes:props.messages.general_no}</td>
                    <td>{user.listOfRoles}</td>
                    <td>
                        {user.lastLogin?
                            `${user.lastLogin.year}-${user.lastLogin.monthOfYear.toString().length===1?`0${user.lastLogin.monthOfYear}`:user.lastLogin.monthOfYear}-${user.lastLogin.dayOfMonth.toString().length===1?`0${user.lastLogin.dayOfMonth}`:user.lastLogin.dayOfMonth}`
                            :null
                        }
                    </td>
                    <td style={{padding:"8px"}}>
                        <span className="admin-blue-button" onClick={() => changeActiveUserHandler(user.externalId)}>
                            <Edit style={{marginRight:"5px", fill:"#fff"}}></Edit>
                            {
                                user.active?
                                    <p style={{fontWeight:400}}>{props.messages.general_yes}</p>
                                :<p style={{fontWeight:400}}>{props.messages.general_no}</p>
                            }
                        </span>
                    </td>
                    <td style={{padding:"8px"}}>
                        <span className="admin-blue-button" onClick={() => editUserHandler(user.externalId)}>
                            <Edit style={{marginRight:"5px", fill:"#fff"}}></Edit>
                            <p style={{fontWeight:400}}>{props.messages.general_edit}</p>
                        </span>
                    </td>
                    <td style={{padding:"8px"}}>
                        <span className="admin-delete-button" onClick={() => deleteUserHandler(user.externalId)}>
                            <p style={{marginRight:"5px"}}>X</p>
                            <p style={{fontWeight:400}}>{props.messages.general_remove}</p>
                        </span>
                    </td>
                </tr>
            )
        })
    }

    const mapSessionsToTable = () => {
        return sessionList.map((session, i) => {
            return (
                <tr key={i}>
                    <td style={{padding:"10px 0"}}>{session.userName}</td>
                    <td>{session.firstName}</td>
                    <td>{session.lastName}</td>
                    <td>{session.lastRequest}</td>
                    <td>{session.sessionId}</td>
                </tr>
            )
        })
    }
    
    return (
        <div className="admin">
            <p className="admin-title">{props.messages.user_list} ({list.length})</p>
            {
            list.length>0?
            <div>
                <table className="admin-table">
                    <thead>
                        <tr>
                            <th>{props.messages.login_username}</th>
                            <th>{props.messages.user_firstName}</th>
                            <th>{props.messages.user_lastName}</th>
                            <th>{props.messages.user_email}</th>
                            <th>{props.messages.user_enabled}</th>
                            <th>{props.messages.user_roles}</th>
                            <th>{props.messages.user_lastLogin}</th>
                            <th>{props.messages.user_active}</th>
                            <th>{props.messages.general_edit}</th>
                            <th>{props.messages.general_delete}</th>
                        </tr>
                    </thead>
                    <tbody>
                        {mapUsersToTable()}
                    </tbody>
                </table>
            </div>
            
            :
            <div style={{marginTop:"50px"}}>
                <CircleLoader loading={list.length===0}></CircleLoader>
            </div>
            }
            <p className="admin-title">Sessions</p>
            {
            sessionList?sessionList.length>0?
            <div>
                <div style={{display:"flex", flexDirection:"column", float:"right"}}>
                    <span className="admin-delete-button" style={{marginTop:"20px"}} onClick={() => switchHandler()}>
                        <Edit style={{marginRight:"5px", fill:"#fff"}}></Edit>
                        {
                            admin?
                            <p style={{fontWeight:400}}>Administration Mode</p>
                            :<p style={{fontWeight:400}}>User Mode</p>
                        }
                        
                    </span>
                    <span className="admin-delete-button" style={{marginTop:"20px", marginBottom:"20px"}} onClick={() => deleteUserSessionsHandler()}>
                        <Edit style={{marginRight:"5px", fill:"#fff"}}></Edit>
                        <p style={{fontWeight:400}}>Delete User Sessions</p>
                    </span>
                </div>
                
                <table className="admin-table" >
                    <thead>
                        <tr>
                            <th>Username</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Last Request</th>
                            <th>Session ID</th>
                        </tr>
                    </thead>
                    <tbody>
                        {mapSessionsToTable()}
                    </tbody>
                </table>
            </div>

            :
                <div style={{marginTop:"50px"}}>
                    <CircleLoader loading={list.length===0}></CircleLoader>
                </div>
            :null
            }
        </div>
    )
}

export default User