import React, { useState } from 'react'
import '../../../resources/css/user/AuthenticationLogin.css'
import { Link, useHistory } from "react-router-dom";
import { ACCESS_TOKEN, GOOGLE_AUTH_URL, FACEBOOK_AUTH_URL, GITHUB_AUTH_URL } from '../../../constants/index.js'
import { login } from '../../../util/utilsAPI';

const AuthenticationLogin = (props) => {

    const history = useHistory()
    const [errorDisplay, setErrorDisplay] = useState(false)
    const [emailOrUsername, setEmailOrUsername] = useState('')
    const [password, setPassword] = useState('')

    //AUTHENTICATION METHODS
    const handleLogin = (event) => {
        event.preventDefault()
        const loginRequest = {
            usernameOrEmail:emailOrUsername,
            password:password
        }
        login(loginRequest) //API COMMUNICATION
            .then(res => {
                localStorage.setItem(ACCESS_TOKEN, res.data.accessToken)
                props.onLogin()
                setErrorDisplay(false)
                history.push("/")
            })
            .catch(error => {
                setErrorDisplay(true)
                if(error.status === 401) {
                    console.log(error)  
                    console.log(401)  
                } else {
                    console.log(error)                               
                }
            })
    }
    ///

    return (
        <div className="authentication">
            <p hidden={!errorDisplay} className="authentication-error">Não foi possível entrar, por favor, verifique o seu o nome de utilizador ou a senha...</p>
            <p className="authentication-title">Arquivo LdoD</p>
            <div className="authentication-input-div">
                <input className="authentication-input-input" onChange={e => setEmailOrUsername(e.target.value)} placeholder="Nome de utilizador"></input>
                <input className="authentication-input-input" onChange={e => setPassword(e.target.value)} placeholder="Senha" type="password"></input>
            </div>
            <div className="authentication-O2">
                <button className="authentication-button" onClick={handleLogin}>Entrar</button>
                <div className="authentication-button-O2-div">
                    <a href={GOOGLE_AUTH_URL} className="authentication-button-O2-google" >Google</a>
                </div>
                <div className="authentication-button-O2-div">
                    <a href={FACEBOOK_AUTH_URL} className="authentication-button-O2-facebook">Facebook</a>
                </div>
                <div className="authentication-button-O2-div">
                    <a href={GITHUB_AUTH_URL} className="authentication-button-O2-linkedin">Linkedin</a>
                </div>
            </div>
            <div style={{marginTop:"20px"}}>
                <Link to="/auth/signup" className="authentication-registar">Registar-se como utilizador do Arquivo LdoD</Link>
            </div>
            
        </div>
    )
}

export default AuthenticationLogin