import React, { useState } from 'react'
import { 
    NAME_MIN_LENGTH, NAME_MAX_LENGTH, 
    USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH,
    EMAIL_MAX_LENGTH,
    PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH
} from '../../../constants/index.production';
import { useHistory } from "react-router-dom";
import { signup } from '../../../util/API/UserAPI';
import ConductPt from '../../about/pages/Conduct_pt'
import ConductEn from '../../about/pages/Conduct_en'
import ConductEs from '../../about/pages/Conduct_es'

const Register = (props) => {

    const history = useHistory()

    const [name, setName] = useState('')
    const [surname, setSurrname] = useState('')
    const [username, setUsername] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [conduct, setConduct] = useState(false)

    const [error, setError] = useState("")

    const validateDetails = (request) => {
        if(request.firstName.length>NAME_MIN_LENGTH && request.firstName.length< NAME_MAX_LENGTH &&
            request.lastName.length>NAME_MIN_LENGTH && request.lastName.length< NAME_MAX_LENGTH &&
                request.username.length>USERNAME_MIN_LENGTH && request.username.length<USERNAME_MAX_LENGTH &&
                    request.email.length<EMAIL_MAX_LENGTH &&
                        request.username.length>PASSWORD_MIN_LENGTH && request.username.length<PASSWORD_MAX_LENGTH){
                            return true
                        }
                        else return false
    }

    //AUTHENTICATION METHODS
    const handleRegister = () => {
        setError("")
        const signupRequest = {
            firstName: name,
            lastName: surname,
            username: username,
            password: password,
            email: email,
            conduct: conduct,
            socialMediaService: "",
            socialMediaId: "",
        }
        if(validateDetails(signupRequest)){
            signup(signupRequest) //API COMMUNICATION
            .then(res => {
                if(res.data !== "error") history.push("/auth/signin")
                else{
                    window.scrollTo(0, 0)
                    setError("Nome de utilizador repetido")
                } 
            })
            .catch(error => {
                console.log(error)
            });
        }
        else{
            window.scrollTo(0, 0)
            setError("Campos vazios/inválidos")
        }
        
    }
    ///

    return(
        <div className="registo">
            {
                error?
                    <p style={{color:"red"}}>{error}</p>
                :null
            }
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
                    <input className="registo-input-input" onChange={e => setPassword(e.target.value)} type="password"></input>
                </div>
                <div className="registo-input-div-flex">
                    <p className="registo-input-name">{props.messages.user_email}</p>
                    <input className="registo-input-input" onChange={e => setEmail(e.target.value)}></input>
                </div>
                <div className="registo-input-div-flex">
                    <p className="registo-input-name">{props.messages.header_conduct}</p>
                    <div className="registo-input-conduta">
                        <input type="checkbox" checked={conduct} onChange={() => setConduct(!conduct)} className="registo-input-conduta-select"></input>
                        <p className="registo-input-conduta-text">{props.messages.header_conduct_accept}</p>
                    </div>
                </div>
            </div>
            <div className="registo-text">
                {props.language==="pt"?<ConductPt/>:props.language==="en"?<ConductEn/>:<ConductEs/>}
            </div>
            <button className="registo-button" onClick={() => handleRegister()}>{props.messages.signup}</button>
            
        </div>
    )

}

export default Register