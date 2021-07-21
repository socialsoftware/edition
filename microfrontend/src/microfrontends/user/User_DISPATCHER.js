import React from 'react'
import { Route, Switch } from 'react-router-dom'
import Login from './pages/Login';
import Register from './pages/Register';
import ChangePassword from './pages/ChangePassword';
import '../../resources/css/user/User.css'

const User_DISPATCHER = (props) => {
    return (
        <Switch>
            <Route path="/auth/signin">
                <Login messages={props.messages} onLogin={props.onLogin}/>
            </Route>
            <Route path="/auth/signup">
                <Register messages={props.messages} onLogin={props.onLogin}/>
            </Route>
            <Route path="/auth/changePassword">
                <ChangePassword messages={props.messages} onLogin={props.onLogin} user={props.user}/>
            </Route>
        </Switch>
    )
}

export default User_DISPATCHER