import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import SockJsClient from 'react-stomp';
import { TalkBox } from 'react-talk';
import { WEB_SOCKETS_URL, LDOD_MESSAGE } from '../utils/Constants';


class Chat extends Component {
    constructor(props) {
        super(props);
        this.state = {
            clientConnected: false,
            messages: [],
            userId: "",
            chatName: " "
        };
    }

    componentDidMount() {
        console.log(this.props.currentUser.username);
        this.setState({
            userId: this.props.currentUser.username,
            chatName: this.props.currentUser.firstName + " " + this.props.currentUser.lastName,
        });
    }

    onMessageReceive = (msg, topic) => {
        this.setState(prevState => ({
            messages: [...prevState.messages, msg]
        }));
    }
    
    sendMessage = (msg, selfMsg) => {
        try {
            this.clientRef.sendMessage("/ldod-game/chat", JSON.stringify(selfMsg));
            return true;
        } catch(e) {
            return false;
        }
    }
    
    render() {
        return (
        <div>
            <TalkBox topic={LDOD_MESSAGE} currentUserId={this.state.userId}
                currentUser={this.state.chatName} messages={this.state.messages}
                onSendMessage={this.sendMessage} connected={this.state.clientConnected}/>
                
            <SockJsClient url={WEB_SOCKETS_URL} topics={["/"]}
                onMessage={this.onMessageReceive} ref={ (client) => { this.clientRef = client }}
                onConnect={ () => { this.setState({ clientConnected: true }) } }
                onDisconnect={ () => { this.setState({ clientConnected: false }) } }
                debug={ false }/>
            </div>
    );
  }
}

export default withRouter(Chat);