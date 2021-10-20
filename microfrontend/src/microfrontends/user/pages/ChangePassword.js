import React, { useEffect, useState } from 'react'
import { useHistory } from 'react-router-dom'
import { changePassword } from '../../../util/API/UserAPI';

const ChangePassword = (props) => {

    const history = useHistory()
    const [currentPassword, setCurrentPassword] = useState('')
    const [newPassword, setNewPassword] = useState('')
    const [retypedPassword, setRetypedPassword] = useState('')
    const [error, setError] = useState("")

    useEffect(() => {
        if(!props.user) history.push("/")

    })

    const handleChange = () => {
        const changeRequest = {
            username: props.user.username,
            currentPassword: currentPassword,
            newPassword: newPassword,
            retypedPassword: retypedPassword,
        }
        if(newPassword !== retypedPassword) setError("Palavras-passe não coincidem")
        else{
            changePassword(changeRequest)
                .then(res => {
                    if(res.data === "success") history.push("/")
                    else setError("Palavra passe atual errada")
                })
        }
    }
    
    return (
        <div style={{marginTop:"150px"}}>
            {
                error?
                    <p style={{color:"red"}}>{error}</p>
                :null
            }
            <p className="registo-title">{props.messages.user_password}</p>
            <div className="registo-input-div">
                <div className="registo-input-div-flex"> 
                    <p className="registo-input-name">{props.messages.user_password_current}</p>
                    <input type="password" className="registo-input-input" onChange={e => setCurrentPassword(e.target.value)}></input>
                </div>
                <div className="registo-input-div-flex">
                    <p className="registo-input-name">{props.messages.user_password_new}</p>
                    <input type="password" className="registo-input-input" onChange={e => setNewPassword(e.target.value)}></input>
                </div>
                <div className="registo-input-div-flex">
                    <p className="registo-input-name">{props.messages.user_password_retype}</p>
                    <input type="password" className="registo-input-input" onChange={e => setRetypedPassword(e.target.value)}></input>
                </div>
            </div>
            <button className="registo-button" onClick={() => handleChange()}>{props.messages.general_update}</button>
        </div>
    )
} 

export default ChangePassword