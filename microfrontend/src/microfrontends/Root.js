import React, { useState, useEffect, lazy, Suspense  } from 'react'
import { BrowserRouter, Redirect, Route, Switch} from "react-router-dom";
import { ACCESS_TOKEN } from '../constants/index.production'
import { getCurrentUser } from '../util/API/UserAPI';
import * as messages_en from '../constants/messages_en'
import * as messages_pt from '../constants/messages_pt'
import * as messages_es from '../constants/messages_es'
import CircleLoader from "react-spinners/RotateLoader";


import { connect } from 'react-redux';


const Navbar = lazy(() => import('./common/Navbar'))
const Home = lazy(() => import('./common/Home'))
const USER_DISPATCHER = lazy(() => import('./user/User_DISPATCHER'))
const ABOUT_DISPATCHER = lazy(() => import('./about/About_DISPATCHER'))
const DOCUMENTS_DISPATCHER = lazy(() => import('./documents/Documents_DISPATCHER'))
const EDITION_DISPATCHER = lazy(() => import('./edition/Edition_DISPATCHER'))
const SEARCH_DISPATCHER = lazy(() => import('./search/Search_DISPATCHER'))
const READING_DISPATCHER = lazy(() => import('./reading/Reading_DISPATCHER'))
const FRAGMENT_DISPATCHER = lazy(() => import('./fragment/Fragment_DISPATCHER'))
const VIRTUAL_DISPATCHER = lazy(() => import('./virtual/Virtual_DISPATCHER'))
const ADMIN_DISPATCHER = lazy(() => import('./admin/Admin_DISPATCHER'))
const OAuth2RedirectHandler = lazy(() => import('./user/authentication/OAuth2/OAuth2RedirectHandler'))

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

    useEffect(() => {
        loadCurrentUser()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [])

    const loadCurrentUser = () => {
        getCurrentUser()
        .then(response => {
            setCurrentUser(response.data)
            props.setUser(response.data)
            setIsAuthenticated(true)
            //props.resetStore()  //DAR RESET AO ESTADO REDUX PERSISTIDO
        }).catch(error => {
            handleLogout()
        })
      }

    const handleLogout = () => {
        localStorage.removeItem(ACCESS_TOKEN);
        setCurrentUser(null)
        setIsAuthenticated(false)
        props.logoutUser()
    }

    //////////

    const renderLoader = () => <div style={{marginTop:"250px"}}>
        <CircleLoader loading={true}></CircleLoader> </div>
    
    
    return (
        <div className="home">
            <Suspense fallback={renderLoader()}>
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
                    <Route exact path="/">
                        <Redirect to="/microfrontend"/>
                    </Route>
                    <Route exact path="/microfrontend" 
                        component={(props) => <Home 
                        {...props} 
                        language={language}
                        />}>
                    </Route>
                    <Route path="/oauth2/redirect" component={OAuth2RedirectHandler}></Route>  
                    <Route path="/auth" 
                        component={(props) => <USER_DISPATCHER 
                        {...props} 
                        language={language}
                        messages={messages}
                        onLogin={() => loadCurrentUser()}
                        user={currentUser}
                        />}>
                    </Route> 
                    <Route path="/admin" 
                        component={(props) => <ADMIN_DISPATCHER 
                        {...props} 
                        language={language}
                        messages={messages}
                        isAuthenticated={isAuthenticated}
                        />}>
                    </Route>
                    <Route path="/about" 
                        component={(props) => <ABOUT_DISPATCHER 
                        {...props} 
                        language={language}
                        messages={messages}
                        />}>
                    </Route>
                    <Route path="/documents" 
                        component={(props) => <DOCUMENTS_DISPATCHER 
                        {...props} 
                        language={language}
                        messages={messages}
                        />}>
                    </Route>
                    <Route path="/edition" 
                        component={(props) => <EDITION_DISPATCHER 
                        {...props} 
                        language={language}
                        messages={messages}
                        />}>
                    </Route>
                    <Route path="/search" 
                        component={(props) => <SEARCH_DISPATCHER 
                        {...props} 
                        language={language}
                        messages={messages}
                        />}>
                    </Route>
                    <Route path="/reading" 
                        component={(props) => <READING_DISPATCHER 
                        {...props} 
                        language={language}
                        messages={messages}
                        />}>
                    </Route>
                    <Route path="/fragments" 
                        component={(props) => <FRAGMENT_DISPATCHER 
                        {...props} 
                        messages={messages}
                        isAuthenticated={isAuthenticated}
                        />}>
                    </Route>
                    <Route path="/virtual" 
                        component={(props) => <VIRTUAL_DISPATCHER 
                        {...props} 
                        currentUser={currentUser}
                        isAuthenticated={isAuthenticated}
                        messages={messages}
                        />}>
                    </Route>
                    <Route path='*' exact={true}>
                        <p style={{marginTop:"200px"}}>{messages.pagenotfound_message}</p>
                    </Route>
                </Switch>
            </BrowserRouter>
            </Suspense>

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
