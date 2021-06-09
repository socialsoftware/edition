import React, { useState, useEffect } from 'react';
import { ACCESS_TOKEN } from '../../../../constants/index';
import { Redirect } from 'react-router-dom'

const OAuth2RedirectHandler = (props) => {

    const getUrlParameter = (name) => {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]')
        var regex = new RegExp('[\\?&]' + name + '=([^&#]*)')

        var results = regex.exec(props.location.search)
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '))
    }

    const [token, setToken] = useState(getUrlParameter("token"))

    useEffect(() => {
    })

    

    if(token) {
        localStorage.setItem(ACCESS_TOKEN, token);
        return <Redirect to={{
            pathname: "/profile"
        }}/>; 
    } else {
        return <Redirect to={{
            pathname: "/auth/signin"
        }}/>; 
    }

}

export default OAuth2RedirectHandler