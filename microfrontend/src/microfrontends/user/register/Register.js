import React, { useState } from 'react'
import { 
    NAME_MIN_LENGTH, NAME_MAX_LENGTH, 
    USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH,
    EMAIL_MAX_LENGTH,
    PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH
} from '../../../constants/index.js';
import { useHistory } from "react-router-dom";
import '../../../resources/css/user/Register.css'
import { signup } from '../../../util/utilsAPI';
import Conduct_pt from '../../about/pages/Conduct_pt'
import Conduct_en from '../../about/pages/Conduct_en'
import Conduct_es from '../../about/pages/Conduct_es'

const Register = (props) => {

    const history = useHistory()

    const [name, setName] = useState('')
    const [surname, setSurrname] = useState('')
    const [username, setUsername] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')

    const validateDetails = (request) => {
        if(request.name.length>NAME_MIN_LENGTH && request.name.length< NAME_MAX_LENGTH &&
            request.username.length>USERNAME_MIN_LENGTH && request.username.length<USERNAME_MAX_LENGTH &&
                request.email.length<EMAIL_MAX_LENGTH &&
                    request.username.length>PASSWORD_MIN_LENGTH && request.username.length<PASSWORD_MAX_LENGTH){
                        return true
                    }
                    else return false
    }

    //AUTHENTICATION METHODS
    const handleRegister = () => {
        const signupRequest = {
            name: name+' '+surname,
            email: email,
            username: username,
            password: password
        }
        if(validateDetails(signupRequest)){
            signup(signupRequest) //API COMMUNICATION
            .then(res => {
                console.log(res.data);
                history.push("/auth/signin")
            })
            .catch(error => {
                console.log(error)
                if(error.status === 401) {
                    console.log(error)  
                    console.log(401)      
                } else {
                    console.log(error)                                        
                }
            });
        }
        
    }
    ///

    return(
        <div className="registo">
            <p className="registo-title">{props.messages.signup}</p>
            <div className="registo-input-div">
                <div className="registo-input-div-flex"> 
                    <p className="registo-input-name">{props.messages.user_firstName}</p>
                    <input className="registo-input-input" onChange={e => setName(e.target.value)}></input>
                </div>
                <div className="registo-input-div-flex">
                    <p className="registo-input-name">{props.messages.user_lastName}</p>
                    <input className="registo-input-input" onChange={e => setSurrname(e.target.value)}></input>
                </div>
                <div className="registo-input-div-flex">
                    <p className="registo-input-name">{props.messages.login_username}</p>
                    <input className="registo-input-input" onChange={e => setUsername(e.target.value)}></input>
                </div>
                <div className="registo-input-div-flex">
                    <p className="registo-input-name">{props.messages.login_password}</p>
                    <input className="registo-input-input" onChange={e => setEmail(e.target.value)}></input>
                </div>
                <div className="registo-input-div-flex">
                    <p className="registo-input-name">{props.messages.user_email}</p>
                    <input className="registo-input-input" onChange={e => setPassword(e.target.value)} type="password"></input>
                </div>
                <div className="registo-input-div-flex">
                    <p className="registo-input-name">{props.messages.header_conduct}</p>
                    <div className="registo-input-conduta">
                        <input type="checkbox" className="registo-input-conduta-select"></input>
                        <p className="registo-input-conduta-text">{props.messages.header_conduct_accept}</p>
                    </div>
                </div>
            </div>
            <div className="registo-text">
                {props.language==="pt"?<Conduct_pt/>:props.language==="en"?<Conduct_en/>:<Conduct_es/>}
            </div>
            <button className="registo-button" onClick={() => handleRegister()}>{props.messages.signup}</button>
            
        </div>
    )

}

export default Register