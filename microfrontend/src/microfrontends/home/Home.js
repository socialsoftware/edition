import React, { useState, useEffect } from 'react'
import '../../resources/css/home/Navbar.css'
import Navbar from './Navbar'
import { BrowserRouter, Route, Switch} from "react-router-dom";
import AuthenticationLogin from '../user/login/Login';
import AuthenticationRegister from '../user/register/Register';
import { ACCESS_TOKEN } from '../../constants/index.js'
import { getCurrentUser } from '../../util/utilsAPI';
import OAuth2RedirectHandler from '../user/authentication/OAuth2/OAuth2RedirectHandler'
import { createStore } from 'redux';
import reducer from '../../util/redux/reducer';
import { Provider } from 'react-redux';

const Home = () => {

    //REDUX STORE
    const store = createStore(reducer)

    const [currentUser, setCurrentUser] = useState(null)
    const [isAuthenticated, setIsAuthenticated] = useState(false)
    const [isLoading, setIsLoading] = useState(false)


    //AUTHENTICATION METHODS
    useEffect(() => {
        loadCurrentUser()
    }, [])

    const loadCurrentUser = () => {
        setIsLoading(true)

        getCurrentUser()
        .then(response => {
            console.log(response)
            setCurrentUser(response)
            setIsAuthenticated(true)
            setIsLoading(false)
        }).catch(error => {
            console.log(error)
            setIsLoading(false) 
        })
      }

    const handleLogout = () => {
        localStorage.removeItem(ACCESS_TOKEN);
        setCurrentUser(null)
        setIsAuthenticated(false)
    }

    const handleLogin = () => {
        loadCurrentUser()
    }
    //////////
    
    return (
        <div className="home">
            <Provider store={store}>
                <BrowserRouter>
                    <Navbar 
                        isAuthenticated={isAuthenticated}
                        currentUser={currentUser} 
                        onLogout={() => {
                            handleLogout()
                        }}
                        />
                    <Switch>
                        <Route path="/auth/signin" 
                            render={(props) => <AuthenticationLogin onLogin={() => {
                                handleLogin()
                            }} {...props} />}>
                            </Route>
                        <Route path="/auth/signup" >
                            <AuthenticationRegister/>
                        </Route>
                        <Route path="/oauth2/redirect" component={OAuth2RedirectHandler}></Route>  
                    </Switch>
                </BrowserRouter>
            </Provider>
        </div>
    )
}

export default Home