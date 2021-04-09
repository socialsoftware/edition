import React, { useState, useEffect } from 'react'
import '../resources/css/home/Navbar.css'
import Navbar from './common/Navbar'
import { BrowserRouter, Route, Switch} from "react-router-dom";
import Login from './user/login/Login';
import Register from './user/register/Register';
import { ACCESS_TOKEN } from '../constants/index.js'
import { getCurrentUser } from '../util/utilsAPI';
import OAuth2RedirectHandler from './user/authentication/OAuth2/OAuth2RedirectHandler'
import { createStore } from 'redux';
import reducer from '../util/redux/reducer';
import { Provider } from 'react-redux';
import * as messages_en from '../constants/messages_en'
import * as messages_pt from '../constants/messages_pt'
import * as messages_es from '../constants/messages_es'
import About_DISPATCHER from './about/About_DISPATCHER';
import Documents_DISPATCHER from './documents/Documents_DISPATCHER';
import Home from './common/Home';

const Root = () => {

    //REDUX STORE
    const store = createStore(reducer)

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
            setMessages(messages_en)
            setLanguage("en")
        }
        else if(val === "es" && language!==val){
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
            console.log(response)
            setCurrentUser(response)
            setIsAuthenticated(true)
        }).catch(error => {
            console.log(error)
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
                                handleLogin()
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
                    </Switch>
                </BrowserRouter>
            </Provider>
        </div>
    )
}

export default Root