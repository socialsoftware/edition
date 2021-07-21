import React, { useState } from 'react'
import { Link, useHistory } from "react-router-dom";
import { ACCESS_TOKEN, GOOGLE_AUTH_URL, FACEBOOK_AUTH_URL, GITHUB_AUTH_URL } from '../../../constants/index.js'
import { login } from '../../../util/API/UserAPI';

const Login = (props) => {

    const history = useHistory()
    const [errorDisplay, setErrorDisplay] = useState(false)
    const [emailOrUsername, setEmailOrUsername] = useState('')
    const [password, setPassword] = useState('')

    //AUTHENTICATION METHODS
    const handleLogin = (event) => {
        event.preventDefault()
        const loginRequest = {
            username:emailOrUsername,
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
            <p hidden={!errorDisplay} className="authentication-error">{props.messages.login_error}</p>
            <p className="authentication-title">{props.messages.header_title}</p>
            <div className="authentication-input-div">
                <input className="authentication-input-input" onChange={e => setEmailOrUsername(e.target.value)} placeholder={props.messages.login_username}></input>
                <input className="authentication-input-input" onChange={e => setPassword(e.target.value)} placeholder={props.messages.login_password} type="password"></input>
            </div>
            <div className="authentication-O2">
                <button className="authentication-button" onClick={handleLogin}>{props.messages.general_signin}</button>
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
                <Link to="/auth/signup" className="authentication-registar">{props.messages.signup_message}</Link>
            </div>
            
        </div>
    )
}

export default Login