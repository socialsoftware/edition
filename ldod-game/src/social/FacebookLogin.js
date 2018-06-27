import React, { Component } from 'react';
import { notification } from 'antd';
import config from './Config';
import { socialLogin } from '../utils/APIUtils';
import { ACCESS_TOKEN } from '../utils/Constants';
import FontAwesome from 'react-fontawesome'

class FacebookLogin extends Component{
    constructor(props) {
        super(props);
        this.state = {
            username: "",
            password: "",
            socialToken: "",
            id: ""
        };
    }
    
    componentDidMount(){
         // Loads the required SDK asynchronously for Facebook
        (function(d, s, id) {
            var js, fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id)) return;
            js = d.createElement(s); js.id = id;
            js.src = "//connect.facebook.net/en_US/sdk.js";
            fjs.parentNode.insertBefore(js, fjs);
        }(document, 'script', 'facebook-jssdk'));
        
        window.fbAsyncInit = function() {
            window.FB.init({
              appId: config.facebook,
              xfbml: true,  
              cookie: true,
              version: 'v2.7' 
            });
        };
    }
    
    facebookLogin = () => {
        window.FB.login(
            function(response){
                this.statusChangeCallback(response);
            }.bind(this),{ scope : 'email,public_profile' });
    }
    
    statusChangeCallback(response) {
        console.log(response);
        if (response.status === 'connected') {
            // Logged into your app and Facebook.
            this.setState({
                socialToken: response.authResponse.accessToken,
                id: response.authResponse.userID,
            });
            this.fetchDataFacebook();
        } else if (response.status === 'not_authorized') {
            notification.error({
                message: 'LdoD Game',
                description: 'Authorize app to login'
            });
        } else {
            notification.error({
                message: 'LdoD Game',
                description: 'Error occured while importing data'
            });
        }
    }
    
    fetchDataFacebook = () => {
        /*window.FB.api('/me', function(user) {
            notification.success({
                message: 'LdoD Game',
                description: user.name + 'You\'re successfully logged in.'
            });;
        });*/
        window.FB.api('/me', function(user){
            this.setState({
                username: user.name,
            });
            console.log(user);
        }.bind(this));
        
        socialLogin(this.state.socialToken)
        .then(response => {
            //localStorage.setItem(ACCESS_TOKEN, response.accessToken);
            notification.success({
                message: 'teste',
                description: response.message,
            });
        }).catch(error => {
            notification.error({
                message: 'LdoD Game',
                description: error.message
            });
        });
    }

    render(){
        return(
            <div className="row">
                <div className="col-md-offset-5 col-md-2">
                    {/*<button onClick={() => console.log(localStorage.getItem(ACCESS_TOKEN))}/>*/}
                    <button onClick={ () => this.facebookLogin() } className="btn btn-block btn-social btn-facebook" type="submit">
                        <span>
                            <FontAwesome className='facebook-f'name='facebook-f'/>
                        </span>Facebook
                    </button>				
                </div>
            </div>
        )
    }
}

export default FacebookLogin;