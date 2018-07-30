import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { WEB_SOCKETS_URL} from '../utils/Constants';
var stompClient = null;
var subscription = null;

class WebSockets extends Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: " ",
        };  
        this.onMessageReceived = this.onMessageReceived.bind(this);   
        this.onConnected = this.onConnected.bind(this);
        this.sendMessage = this.sendMessage.bind(this);   
    }
    
    componentDidMount(){
        this.props.onRef(this)
        this.setState({
            userId: this.props.currentUser.username,
        });
        var socket = new SockJS(WEB_SOCKETS_URL);
        stompClient = Stomp.over(socket);
        stompClient.connect({}, this.onConnected);

    }
    
    onConnected(){
        subscription = stompClient.subscribe('/topic/ping', this.onMessageReceived,{ id: this.state.userId});
        stompClient.send('/ldod-game/hello', {}, "Hello, STOMP");
    
    }
    
    onMessageReceived(payload) {
        var message = JSON.parse(payload.body).message;
        console.log("Server sent " + message);
    }
    
    componentWillUnmount(){
        subscription.unsubscribe();
        stompClient.disconnect();
        this.props.onRef(null)
    }

    sendMessage(msg){
        stompClient.send('/ldod-game/hello', {}, msg);
    }
    
    render() {
        return (
            <div>  
            </div>
        );
    }
}

export default withRouter(WebSockets);