import React, { Component } from 'react';
import { Route, withRouter, Link, Switch } from 'react-router-dom';
import { getCurrentUser,  getActiveGames } from '../utils/APIUtils';
import { ACCESS_TOKEN, LDOD_MESSAGE, FRAGMENTS_BASE_URL, INTER_BASE_URL, SITE_URL } from '../utils/Constants';
import AppContext from './AppContext';
import {Provider} from './/AppContext';
import Login from '../user/login/Login';
import Profile from '../user/profile/Profile';
import Header from '../common/Header';
import Game from '../game/Game';
import GameLeadeboard from '../game/GameLeaderboard';
import NotFound from '../common/NotFound';
import PrivateRoute from '../common/PrivateRoute';
import About from '../common/About';
import { notification } from 'antd';
import { Jumbotron, Button, Col, Grid, Row, ListGroup, ListGroupItem, Glyphicon} from 'react-bootstrap'; 
import Feedback from '../common/Feedback';

class App extends Component {
    constructor(props) {
        super(props);
        
        this.state = {
            currentUser: null,
            isAuthenticated: false,
            isLoading: false,
            activeGames: [],
            enabled: false,
        }

        this.handleLogout = this.handleLogout.bind(this);
        this.loadCurrentUser = this.loadCurrentUser.bind(this);
        this.handleLogin = this.handleLogin.bind(this);
        this.getAndSetupGames = this.getAndSetupGames.bind(this);

        notification.config({
            placement: 'topRight',
            top: 80,
            duration: 2,
        });
    }
    
    componentDidMount(){
        //if(sessionStorage.getItem(ACCESS_TOKEN)){
            this.loadCurrentUser();
            this.getAndSetupGames()
            setInterval(function () {
                this.getAndSetupGames();
            }.bind(this), 60000)
        //}
    }

    // TODO: CHECK THIS DUE TO REFRESH and OLD TOKENS
    componentWillUnmount() {
        
        //localStorage.clear();
        //caches.delete("JSESSIONID");
    }

    async loadCurrentUser() {
        this.setState({
            isLoading: true
        });
        getCurrentUser()
            .then(response => {
                localStorage.setItem("currentUser", response.username);
                
                this.setState({
                    currentUser: response,
                    isAuthenticated: true,
                });
            }).catch(error => {
                this.setState({
                });
            });
    }


    handleLogout() {
        localStorage.removeItem(ACCESS_TOKEN);
        localStorage.clear();
        
        this.setState({
            currentUser: null,
            isAuthenticated: false,
        });
        
        notification.success({
            message: LDOD_MESSAGE,
            description: "You're successfully logged out.",
        });

        this.props.history.push("/");
    }
    
    async handleLogin() {
        notification.success({
            message: LDOD_MESSAGE,
            description: "You're successfully logged in.",
        });
        this.props.history.push("/");
        await this.loadCurrentUser();
        await this.getAndSetupGames();
        return 
    }

    async getAndSetupGames(){
        if (localStorage.getItem("currentUser") === null){
            return;
        }
        let request = await getActiveGames(localStorage.getItem("currentUser"));
        var gameDate = request[0] != null ? new Date(request[0].dateTime) : null;
        this.setState({
            activeGames: request,
            //game: request[0],
            dateTime:  gameDate,
            enabled: false,//temp
            isLoading: false,
        })
        
        if(request[0] == null){ return; }
    
        var dateItHappens = new Date(request[0].dateTime);
        var millisTillOccurence = dateItHappens.getTime() - new Date().getTime();
        
        setTimeout(function () {
            this.setState({enabled: true});
            notification["info"]({
                message: LDOD_MESSAGE,
                description: "New game available!",
            });
        }.bind(this), millisTillOccurence)
    }
    
    render() {
        var styles ={
            fontFamily : 'Ubuntu',
            backgroundColor: '#3498db'
        }
        let activeGames = this.state.activeGames;
        const gamesView = [];
        if(this.state.isAuthenticated){
            activeGames.forEach((g, index) => {
                var id = g.gameExternalId;
                var gameDate = new Date(g.dateTime);
                var options = { hour: 'numeric', minute: '2-digit' };
                var date = new Date();
                var available =  (gameDate.getHours() === date.getHours() && gameDate.getMinutes() === date.getMinutes() && gameDate.getDate() === date.getDate());
                gamesView.push(
                    <ListGroupItem key={index} bsStyle={available ? "success" : "warning"}>
                            {available ? 
                            (<div><Link to={`/game/${id}`}><Glyphicon glyph="ok" />{'\u00A0'}<Button bsStyle="primary">{'\u00A0'}{g.virtualEditionTitle}:{g.virtualEditionInterDto.title}</Button></Link></div>) 
                            : 
                            (<div><Glyphicon glyph="lock" /></div>)}
                            {<div>{g.virtualEditionTitle}: <a href={SITE_URL + FRAGMENTS_BASE_URL+`${g.virtualEditionInterDto.fragmentId}` + INTER_BASE_URL + `${g.virtualEditionInterDto.urlId}`} target="_blank">{'\u00A0'}{g.virtualEditionInterDto.title}</a></div>}- {gameDate.toLocaleDateString()} {gameDate.toLocaleString(navigator.language, options)}
                    </ListGroupItem>)
            });
        }
    return (
        <Grid fluid>
            <AppContext>
            <Provider value={{currentUser: this.state.currentUser, games: this.state.activeGames}}>  
                {this.props.children}
            <Header
                isAuthenticated={this.state.isAuthenticated}
                currentUser={this.state.currentUser}
                onLogout={this.handleLogout}/>
                <div>
                    <Switch>
                        <Route exact path="/" render={() =>
                        <div>
                            <Jumbotron style={styles}>
                                <h3 className="text-center">Welcome to the LdoD Game powered by LdoD Archive</h3>
                            </Jumbotron>
                            <Row>
                                <Col md={12} xs={4}>
                                    <h4 className="text-center">Games:</h4>
                                    <ListGroup>
                                        {gamesView}
                                    </ListGroup>
                                </Col>
                            </Row>
                        </div>
                        }>
                        </Route>
                        <Route path="/login" render={(props) =>
                        <div>
                            <Login onLogin={this.handleLogin} {...props} />
                        </div>}>
                        </Route>
                        <Route path="/about" component={About}></Route>
                        <PrivateRoute path="/user/:username" authenticated={this.state.isAuthenticated} currentUser={this.state.currentUser}
                        component={Profile}>
                        </PrivateRoute>
                        <PrivateRoute path="/game/:id" authenticated={this.state.isAuthenticated} currentUser={this.state.currentUser}
                        component={Game}>
                        </PrivateRoute>
                        <PrivateRoute path="/leaderboard" authenticated={this.state.isAuthenticated} currentUser={this.state.currentUser}
                        component={GameLeadeboard}>
                        </PrivateRoute>
                        <PrivateRoute path="/feedback" authenticated={this.state.isAuthenticated} currentUser={this.state.currentUser}
                        component={Feedback}>
                        </PrivateRoute>
                        <Route component={NotFound}></Route>
                    </Switch>
                </div>
                </Provider>
                </AppContext>
             </Grid>
    );
  }
}

export default withRouter(App);
