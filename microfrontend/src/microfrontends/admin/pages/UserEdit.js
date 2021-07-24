import React, { useEffect, useState } from 'react'
import { useHistory, useLocation } from 'react-router-dom';
import { getUserForm, setUserForm } from '../../../util/API/AdminAPI';

const UserEdit = (props) => {

    const history = useHistory()
    const location = useLocation()
    const [user, setUser] = useState(null)
    const [originalUser, setOriginalUser] = useState(null)
    const [erro, setErro] = useState("")

    useEffect(() => {
        var path = location.pathname.split('/')
        if(path[4]){
            getUserForm(path[4])
                .then(res => {
                    setUser(res.data)
                    setOriginalUser(res.data)
                })
        }
    }, [location.pathname])

    const changeFieldHandler = (type, value) => {
        let aux = {...user}
        aux[type] = value
        setUser(aux)
    }

    const setUserHandler = () => {
        setErro("")
        if(verifyValues()){
            setUserForm(user)
            .then(res => {
                if(res.data==="valid") history.goBack()
                else{
                    setErro("Nome de utilizador inválido/repetido")
                    setUser(originalUser)
                }
            })
        }
        else{
            setUser(originalUser)
        }
        
    }

    const verifyValues = () => {
        if(!validateEmail(user.email)){
            setErro("Email inválido")
            return false
        }
        else if(user.firstName === ""){
            setErro("Primeiro nome inválido")
            return false
        }
        else if(user.lastName === ""){
            setErro("Último nome inválido")
            return false
        }
        return true
    }

    function validateEmail(email) 
    {
        var re = /\S+@\S+\.\S+/;
        return re.test(email);
    }

    return (
        <div className="admin">
            <p className="admin-title">{props.messages.user_edit}</p>
            {
                erro!==""?
                    <p style={{color:"red", textAlign:"center"}}>{erro}</p>
                :null
            }
            {
                user?
                <div className="admin-edit">
                    <div className="admin-edit-flex">
                        <span className="admin-input-title">{props.messages.login_username}</span>
                        <input className="admin-input" value={user.newUsername} onChange={e => changeFieldHandler("newUsername", e.target.value)}></input>
                    </div>
                    <div className="admin-edit-flex">
                        <span className="admin-input-title">{props.messages.user_firstName}</span>
                        <input className="admin-input" value={user.firstName} onChange={e => changeFieldHandler("firstName", e.target.value)}></input>
                    </div>
                    <div className="admin-edit-flex">
                        <span className="admin-input-title">{props.messages.user_lastName}</span>
                        <input className="admin-input" value={user.lastName} onChange={e => changeFieldHandler("lastName", e.target.value)}></input>
                    </div>
                    <div className="admin-edit-flex">
                        <span className="admin-input-title">{props.messages.user_email}</span>
                        <input className="admin-input" value={user.email} onChange={e => changeFieldHandler("email", e.target.value)}></input>
                    </div>
                    <div className="admin-edit-flex">
                        <span className="admin-input-title">{props.messages.user_role_user}</span>
                        <input type="checkbox" className="admin-checkbox" checked={user.user} onChange={e => changeFieldHandler("user", !user.user)}></input>
                    </div>
                    <div className="admin-edit-flex">
                        <span className="admin-input-title">{props.messages.user_role_admin}</span>
                        <input type="checkbox" className="admin-checkbox" checked={user.admin} onChange={e => changeFieldHandler("admin", !user.admin)}></input>
                    </div>
                    <div className="admin-edit-flex">
                        <span className="admin-input-title">{props.messages.user_enabled}</span>
                        <input type="checkbox" className="admin-checkbox" checked={user.enabled} onChange={e => changeFieldHandler("enabled", !user.enabled)}></input>
                    </div>
                    <div className="admin-edit-flex">
                        <span className="admin-input-title">{props.messages.user_password_new}</span>
                        <input type="password" className="admin-input" value={user.newPassword} onChange={e => changeFieldHandler("newPassword", e.target.value)}></input>
                    </div>
                    <span className="admin-edit-flex" style={{justifyContent:"center"}} onClick={() => setUserHandler()}>
                        <span className="admin-blue-button">{props.messages.general_update}</span>
                    </span>
                </div>
                :null
            }
            
        </div>
    )
}

export default UserEdit