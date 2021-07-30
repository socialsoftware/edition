import React, { useEffect, useState } from 'react'
import { Link, useHistory, useLocation } from 'react-router-dom'
import {ReactComponent as LeftArrow} from '../../../resources/assets/caret-left-fill.svg'
import {ReactComponent as Person} from '../../../resources/assets/person-fill.svg'
import {ReactComponent as Rotate} from '../../../resources/assets/arrow-repeat.svg'
import { getParticipantsPage, addNewMember, removeMember, changeRole, approveMember } from '../../../util/API/VirtualAPI'
import plus from '../../../resources/assets/plus_white.png'
import he from 'he'

const Participants = (props) => {

    const history = useHistory()
    const location = useLocation()
    const [data, setData] = useState(null)
    const [newMember, setNewMember] = useState(null)
    const [addError, setAddError] = useState(null)

    useEffect(() => {
        var path = location.pathname.split('/')
        let mounted = true
        getParticipantsPage(path[5])
            .then(res => {
                if(mounted) setData(res.data)
            })
            .catch((err) => {
                console.log(err);
                alert("Acesso Negado")
                history.push("/")
            })
            return function cleanup() {
                mounted = false
            }
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    const mapMembersToTable = () => {
        return data.activeMemberSet.map((member, i) => {
            return (
                <tr key={i}>
                    <td>
                        <span style={{display:"flex"}}>
                            <Person></Person>
                            <Link className="virtual-link" to={`/edition/user/${member.username}`}>{member.username}</Link>
                        </span>
                    </td>
                    <td>{member.firstname}</td>
                    <td>{member.lastname}</td>
                    <td>{member.email}</td>
                    <td>
                    {
                        member.canSwitchRole?
                            <span className="virtual-submit-button" style={{maxHeight:"20px"}} onClick={() => changeRoleHandler(member.username)}>
                                <Rotate style={{width:"15px", height:"15px", marginRight:"2px"}}></Rotate>
                                <span>
                                    {member.admin?
                                        props.messages.general_manager
                                        :props.messages.general_editor
                                    }
                                </span>
                            </span>
                        :
                            <p>
                                {member.admin?
                                        props.messages.general_manager
                                        :props.messages.general_editor
                                    }
                            </p>

                    }
                    </td>
                    <td>
                        {
                            member.canRemoveMember?
                                <span className="virtual-submit-button" style={{maxHeight:"20px"}} onClick={() => removeMemberHandler(member.externalId)}>
                                    <span style={{fontSize:"15px", fontWeight:600, marginRight:"3px"}}>X</span>
                                    <span>{props.messages.general_remove}</span>
                                </span>
                            :null
                        }
                    </td>
                </tr>
            )
        })
    }

    const mapPedingToTable = () => {
        return data.pendingMemberSet.map((pending, i) => {
            return(
                <tr key={i}>
                    <td>
                        <span style={{display:"flex"}}>
                            <Person></Person>
                            <Link className="virtual-link" to={`/edition/user/${pending.username}`}>{pending.username}</Link>
                        </span>
                    </td>
                    <td>{pending.firstname}</td>
                    <td>{pending.lastname}</td>
                    <td>{pending.email}</td>
                    <td>
                        {
                            data.admin?
                                <span className="virtual-submit-button" onClick={() => approveMemberHandler(pending.username)}>
                                    <img alt="imgPlus" src={plus} style={{width:"15px", height:"15px", marginRight:"5px"}}></img>
                                    <p>{props.messages.general_add}</p>
                                </span>
                            :null
                        }
                    </td>
                </tr>
            )
        })
    }

    const addNewMemberHandler = () => {
        setAddError(null)
        if(newMember){
            addNewMember(data.externalId, newMember)
                .then(res => {
                    setData(res.data)
                    setNewMember("")
                })
                .catch((err) => {
                    console.log(err);
                    setAddError("Utilizador não existe")
                    setNewMember("")
                })
        }
    }

    const removeMemberHandler = (memberId) => {
        removeMember(data.externalId, memberId)
            .then(res => {
                setData(res.data)
                if(res.data === "" && res.status === 200) history.push("/")

            })
            .catch(res => {
                console.log(res);
            })
    }

    const changeRoleHandler = (username) => {
        changeRole(data.externalId, username)
            .then(res => {
                setData(res.data)
            })
            .catch(res => {
                console.log(res);
            })
    }

    const approveMemberHandler = (username) => {
        approveMember(data.externalId, username)
            .then(res => {
                setData(res.data)
            })
            .catch(res => {
                console.log(res);
            })
    }

    return (
        <div className="virtual-editions">
            <button className="virtual-back-button" onClick={() => history.goBack()}>
                <LeftArrow></LeftArrow>
                <p>{props.messages.general_back}</p>
            </button>

            <span className="virtual-body-title">
                <p>{data?he.decode(data.title):null}</p>
            </span>
            {
                addError?
                    <p style={{textAlign:"left"}}>{addError}</p>
                :null
            }
            {
                data?data.admin?
                <div style={{display:"flex", alignItems:"center", marginTop:"30px"}}>
                    <input
                        style={{maxHeight:"20px", marginRight:"5px"}}
                        className="virtual-input-input"
                        placeholder={props.messages.login_username}
                        value={newMember}
                        onChange={e => setNewMember(e.target.value)}/>
                    <span className="virtual-submit-button" onClick={() => addNewMemberHandler()}>
                        <img alt="imgPlus" src={plus} style={{width:"15px", height:"15px", marginRight:"5px"}}></img>
                        <p style={{fontSize:"14px"}}>{props.messages.general_add}</p>
                    </span>
                </div>
                :null:null
            }
            <table className="virtual-participation-table">
                <thead>
                    <tr>
                        <th>{props.messages.login_username}</th>
                        <th>{props.messages.user_firstName}</th>
                        <th>{props.messages.user_lastName}</th>
                        <th>e-mail</th>
                        <th>{props.messages.user_role}</th>
                        <th>{props.messages.general_remove}</th>
                    </tr>
                    
                </thead>
                <tbody>
                    {
                        data?
                            mapMembersToTable()
                        :null
                    }
                </tbody>
            </table>

            <table className="virtual-participation-table" style={{marginTop:"50px"}}>
                <caption>
                    {props.messages.participantsForm_message2}
                </caption>
                <thead>
					<tr>
                        <th>{props.messages.login_username}</th>
                        <th>{props.messages.user_firstName}</th>
                        <th>{props.messages.user_lastName}</th>
                        <th>email</th>
                        <th>{props.messages.general_add}</th>
					</tr>
				</thead>
                <tbody>
                    {
                        data?
                        mapPedingToTable()
                        :null
                    }
                </tbody>
            </table>
            
        </div>
    )
}

export default Participants