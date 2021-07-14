import React, { useState, useEffect } from 'react'
import '../resources/css/home/Navbar.css'
import Navbar from './common/Navbar'
import { BrowserRouter, Route, Switch} from "react-router-dom";
import Login from './user/login/Login';
import Register from './user/register/Register';
import { ACCESS_TOKEN } from '../constants/index.js'
import { getCurrentUser } from '../util/utilsAPI';
import OAuth2RedirectHandler from './user/authentication/OAuth2/OAuth2RedirectHandler'
import * as messages_en from '../constants/messages_en'
import * as messages_pt from '../constants/messages_pt'
import * as messages_es from '../constants/messages_es'
import About_DISPATCHER from './about/About_DISPATCHER';
import Documents_DISPATCHER from './documents/Documents_DISPATCHER';
import Home from './common/Home';
import Edition_DISPATCHER from './edition/Edition_DISPATCHER';
import Search_DISPATCHER from './search/Search_DISPATCHER';
import Reading_DISPATCHER from './reading/Reading_DISPATCHER';
import Fragment_DISPATCHER from './fragment/Fragment_DISPATCHER';
import Virtual_DISPATCHER from './virtual/Virtual_DISPATCHER'
import Admin_DISPATCHER from './admin/Admin_DISPATCHER'
import { connect } from 'react-redux';

const Root = (props) => {

    const [currentUser, setCurrentUser] = useState(null)
    const [isAuthenticated, setIsAuthenticated] = useState(false)
    const [language, setLanguage] = useState("pt")
    const [messages, setMessages] = useState(messages_pt);

    const languageHandler = (val) => {
        if(val === "pt" && language!==val){
            setMessages(messages_pt)
            setLanguage("pt")
        }
        else if(val === "en" && language!==val){
            // @ts-ignore
            setMessages(messages_en)
            setLanguage("en")
        }
        else if(val === "es" && language!==val){
            // @ts-ignore
            setMessages(messages_es)
            setLanguage("es")
        }
    }

    //AUTHENTICATION METHODS
    useEffect(() => {
        loadCurrentUser()
    }, [])

    const loadCurrentUser = () => {
        getCurrentUser()
        .then(response => {
            setCurrentUser(response.data)
            props.setUser(response.data)
            setIsAuthenticated(true)
            //props.resetStore()
        }).catch(error => {
            setCurrentUser(null)
            setIsAuthenticated(false)
            props.logoutUser()
        })
      }

    const handleLogout = () => {
        localStorage.removeItem(ACCESS_TOKEN);
        setCurrentUser(null)
        setIsAuthenticated(false)
        props.logoutUser()
    }

    //////////
    
    return (
        <div className="home">
            
                    <BrowserRouter>
                        <Navbar 
                            isAuthenticated={isAuthenticated}
                            currentUser={currentUser} 
                            language={language}
                            setLanguage={(val) => languageHandler(val)}
                            messages={messages}
                            onLogout={() => {
                                handleLogout()
                            }}
                            />
                        <Switch>
                            <Route exact path="/" 
                                component={(props) => <Home 
                                {...props} 
                                language={language}
                                />}>
                            </Route>
                            <Route path="/oauth2/redirect" component={OAuth2RedirectHandler}></Route>  
                            <Route path="/auth/signin" 
                                component={(props) => <Login onLogin={() => {
                                    loadCurrentUser()
                                }} 
                                {...props} 
                                messages={messages}
                                />}>
                            </Route>
                            <Route path="/auth/signup" 
                                component={(props) => <Register 
                                {...props} 
                                language={language}
                                messages={messages}
                                />}>
                            </Route>
                            <Route path="/admin" 
                                component={(props) => <Admin_DISPATCHER 
                                {...props} 
                                language={language}
                                messages={messages}
                                />}>
                            </Route>
                            <Route path="/about" 
                                component={(props) => <About_DISPATCHER 
                                {...props} 
                                language={language}
                                messages={messages}
                                />}>
                            </Route>
                            <Route path="/documents" 
                                component={(props) => <Documents_DISPATCHER 
                                {...props} 
                                language={language}
                                messages={messages}
                                />}>
                            </Route>
                            <Route path="/edition" 
                                component={(props) => <Edition_DISPATCHER 
                                {...props} 
                                language={language}
                                messages={messages}
                                />}>
                            </Route>
                            <Route path="/search" 
                                component={(props) => <Search_DISPATCHER 
                                {...props} 
                                language={language}
                                messages={messages}
                                />}>
                            </Route>
                            <Route path="/reading" 
                                component={(props) => <Reading_DISPATCHER 
                                {...props} 
                                language={language}
                                messages={messages}
                                />}>
                            </Route>
                            <Route path="/fragments" 
                                component={(props) => <Fragment_DISPATCHER 
                                {...props} 
                                messages={messages}
                                />}>
                            </Route>
                            <Route path="/virtual" 
                                component={(props) => <Virtual_DISPATCHER 
                                {...props} 
                                currentUser={currentUser}
                                isAuthenticated={isAuthenticated}
                                messages={messages}
                                />}>

                            </Route>
                        </Switch>
                    </BrowserRouter>
        </div>
    )
}



const mapDispatchToProps = (dispatch) => {
    return {
        setUser: (data) => dispatch({ type: 'SET_USER', payload: data }),
        resetStore : () => dispatch({type : 'RESET'}),
        logoutUser : () => dispatch({type : "LOGOUT"})

    }
}

export default connect(null, mapDispatchToProps)(Root)
