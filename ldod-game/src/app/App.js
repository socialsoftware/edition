/* React imports */
import React, { Component } from 'react';
import {
  Route,
  withRouter,
  Link,
  Switch
} from 'react-router-dom';
import { Layout, notification } from 'antd';


/* Game imports */
import './App.css';
import { getCurrentUser } from '../utils/APIUtils';
import { ACCESS_TOKEN } from '../utils/Constants';
import VirtualEdition from '../game/VirtualEdition';
import Fragment from '../game/Fragment';
import GameConfig from '../game/admin/GameConfig';
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
import Test from '../social/Test';
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
                isLoading: false});
        });
    }

  componentWillMount() {
    // Load current user if logged in
    this.loadCurrentUser();
  }

  componentWillUnmount(){
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
      message: 'LdoD Game',
      description: description,
    });
  }

  /*
   This method is called by the Login component after successful login
   so that we can load the logged-in user details and set the currentUser &
   isAuthenticated state, which other components will use to render their JSX
  */
  handleLogin() {
    notification.success({
      message: 'LdoD Game',
      description: "You're successfully logged in.",
    });
              
    this.loadCurrentUser();
    this.props.history.push("/");
  }

  render() {
    var styles ={
      fontFamily : 'Ubuntu',
      color: 'black'
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
                <PrivateRoute authenticated={this.state.isAuthenticated} path="/config" component={GameConfig} handleLogout={this.handleLogout}>
                </PrivateRoute>
                <Route path="/leaderboard"
                  render={(props) => <GameLeaderboard/>}>
                </Route>
                <Route path="/game"
                  render={(props) => <Game/>}>
                </Route>
                <Route path="/test"
                  render={(props) => <Test onLogin={this.handleLogin} />}>
                </Route>
                <Route component={NotFound}></Route>
              </Switch>
            </div>
          </Content>
        </Layout>
    );
  }
}

export default withRouter(App);
