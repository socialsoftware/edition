/* React imports */
import React, { Component } from 'react';
import { Route, withRouter, Link, Switch } from 'react-router-dom';
import { Layout, notification } from 'antd';


/* Game imports */
import './App.css';
import { getCurrentUser } from '../utils/APIUtils';
import { ACCESS_TOKEN, LDOD_MESSAGE } from '../utils/Constants';
import VirtualEdition from '../game/VirtualEdition';
import Fragment from '../game/Fragment';
import Login from '../user/login/Login';
import Profile from '../user/profile/Profile';
import AppHeader from '../common/AppHeader';
import GameLeaderboard from '../game/GameLeaderboard';
import Game from '../game/Game';
import NotFound from '../common/NotFound';
import LoadingIndicator from '../common/LoadingIndicator';
import PrivateRoute from '../common/PrivateRoute';
import FacebookLogin from '../social/FacebookLogin';
import LinkedinLogin from '../social/LinkedinLogin';
import GoogleLogin from '../social/GoogleLogin';
import TwitterLogin from '../social/TwitterLogin';
import Tag from '../game/Tag';
import Chat from '../game/Chat';
import { Jumbotron, Button } from 'react-bootstrap';



const { Content } = Layout;

class App extends Component {
    constructor(props) {
        super(props);
        
        this.state = {
            currentUser: null,
            isAuthenticated: false,
            isLoading: false
        }
        
        this.handleLogout = this.handleLogout.bind(this);
        this.loadCurrentUser = this.loadCurrentUser.bind(this);
        this.handleLogin = this.handleLogin.bind(this);

        notification.config({
            placement: 'topLeft',
            top: 80,
            duration: 2,
        });
    }
    
    loadCurrentUser() {
            this.setState({
                isLoading: true
            });
            getCurrentUser()
            .then(response => {
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
    
    componentWillMount() {
        // Load current user if logged in
        this.loadCurrentUser();
    }
    
    componentWillUnmount() {
        localStorage.clear();
    }

  // Handle Logout, Set currentUser and isAuthenticated state which will be passed to other components
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
    
    handleLogin() {
        notification.success({
            message: LDOD_MESSAGE,
            description: "You're successfully logged in.",
        });
        
        this.loadCurrentUser();
        this.props.history.push("/");
    }
    
    render() {
        var styles ={
            fontFamily : 'Ubuntu',
            backgroundColor: '#3498db'
    }
    if(this.state.isLoading) {
        return <LoadingIndicator />
    }
  

    return (
        <Layout className="app-container">
          <AppHeader isAuthenticated={this.state.isAuthenticated}
            currentUser={this.state.currentUser}
            onLogout={this.handleLogout} />
          <Content className="app-content">
            <div className="container">
              <Switch>
                <Route exact path="/" render={() =>
                <div>
                  <Jumbotron style={styles}>
                    <h2>Welcome to the LdoD Game powered by LdoD Archive</h2>
                  </Jumbotron>
                  <div className="col-md-offset-5 col-md-2">
                      <Link to="/game">
                        <Button bsStyle="primary">Start playing</Button>
                      </Link>
                  </div> 
                </div>}>
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
                <Route path="/user/:username" render={(props) => 
                    <Profile isAuthenticated={this.state.isAuthenticated} currentUser={this.state.currentUser} {...props}  />}>
                </Route>
                <Route path="/virtualedition" render={(props) => 
                    <VirtualEdition/>}>
                </Route>
                <Route path="/fragment"render={(props) => 
                    <Fragment/>}>
                </Route>
                <Route path="/leaderboard"
                  render={(props) => <GameLeaderboard/>}>
                </Route>
                <Route path="/game"
                  render={(props) => <Game/>}>
                </Route>
                <Route path="/tag"
                  render={(props) => <Tag />}>
                </Route>
                 <PrivateRoute path="/chat" authenticated={this.state.isAuthenticated}
                  component={Chat} currentUser={this.state.currentUser} handleLogout={this.handleLogout}>
                </PrivateRoute>
                <Route component={NotFound}></Route>
              </Switch>
            </div>
          </Content>
        </Layout>
    );
  }
}

export default withRouter(App);
