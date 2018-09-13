import React, { Component } from 'react';
import { Route, withRouter, Link, Switch } from 'react-router-dom';
import { getCurrentUser,  getActiveGames } from '../utils/APIUtils';
import { ACCESS_TOKEN, LDOD_MESSAGE } from '../utils/Constants';
import AppContext from './AppContext';
import {Provider} from './/AppContext';
import Login from '../user/login/Login';
import Profile from '../user/profile/Profile';
import Header from '../common/Header';
import Game from '../game/Game';
import GameLeadeboard from '../game/GameLeaderboard';
import NotFound from '../common/NotFound';
import PrivateRoute from '../common/PrivateRoute';
import FacebookLogin from '../social/FacebookLogin';
import LinkedinLogin from '../social/LinkedinLogin';
import GoogleLogin from '../social/GoogleLogin';
import TwitterLogin from '../social/TwitterLogin';
import { notification } from 'antd';
import { Jumbotron, Button, Col, Grid, Row} from 'react-bootstrap'; 

class App extends Component {
    constructor(props) {
        super(props);
        
        this.state = {
            currentUser: null,
            isAuthenticated: false,
            isLoading: false,
            activeGames: [],
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
     //   localStorage.setItem("currentFragment", 0);
    }

    // TODO: CHECK THIS DUE TO REFRESH and OLD TOKENS
    componentWillUnmount() {
        //localStorage.clear();
    }

    loadCurrentUser() {
        this.setState({
            isLoading: true
        });
        getCurrentUser()
            .then(response => {
                localStorage.setItem("currentUser", response.username);
                this.setState({
                    currentUser: response,
                    isAuthenticated: true,
                    isLoading: false
                });
            }).catch(error => {
                this.setState({
                    isLoading: false
                });
            });
    }


    handleLogout(redirectTo="/", notificationType="success", description="You're successfully logged out.") {
        localStorage.removeItem(ACCESS_TOKEN);
        localStorage.clear();
        
        this.setState({
            currentUser: null,
            isAuthenticated: false
        });
        
        this.props.history.push(redirectTo);
        
        notification[notificationType]({
            message: LDOD_MESSAGE,
            description: description,
        });
    }
    
    async handleLogin() {
        notification.success({
            message: LDOD_MESSAGE,
            description: "You're successfully logged in.",
        });
        await this.getAndSetupGames();
        this.loadCurrentUser();
        this.props.history.push("/");
        return 
    }

    async getAndSetupGames(){
        let request = await getActiveGames();
        this.setState({
            activeGame: request,
            game: request[0],
        })
    }
    
    render() {
        var styles ={
            fontFamily : 'Ubuntu',
            backgroundColor: '#3498db'
        }
    return (
        <Grid fluid>
            <AppContext>
            <Provider value={{currentUser: this.state.currentUser, game: this.state.game}}>  
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
                                <Col md={4} mdOffset={2} xs={5}>
                                    <Link to="/game">
                                        <Button bsStyle="primary">Classic game</Button>
                                    </Link>
                                </Col>
                                <Col md={4} mdOffset={2} xs={5}>
                                    <Link to="/todo">
                                        <Button bsStyle="primary">Custom game</Button>
                                    </Link>
                                </Col>
                            </Row>
                        </div>
                        }>
                        </Route>
                        <Route path="/login" render={(props) =>
                        <div>
                            <Login onLogin={this.handleLogin} {...props} />
                            SOCIAL NOT WORKING
                            <TwitterLogin onLogin={this.handleLogin} {...props}/>
                            <br/>
                            <GoogleLogin onLogin={this.handleLogin} {...props}/>
                            <br/>
                            <FacebookLogin onLogin={this.handleLogin} {...props}/>
                            <br/>
                            <LinkedinLogin onLogin={this.handleLogin} {...props}/>
                            <br/>
                        </div>}>
                        </Route>
                        <PrivateRoute path="/user/:username" authenticated={this.state.isAuthenticated} currentUser={this.state.currentUser}
                        component={Profile}>
                        </PrivateRoute>
                        <PrivateRoute path="/game" authenticated={this.state.isAuthenticated} currentUser={this.state.currentUser}
                        component={Game}>
                        </PrivateRoute>
                        <PrivateRoute path="/leaderboard" authenticated={this.state.isAuthenticated} currentUser={this.state.currentUser}
                        component={GameLeadeboard}>
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
