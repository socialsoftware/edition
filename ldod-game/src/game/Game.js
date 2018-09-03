import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import VirtualEdition from './VirtualEdition';
import { getVirtualEditionIndex, readyToStart, endOfGame } from '../utils/APIUtils';
import LoadingIndicator  from '../common/LoadingIndicator';
import { Alert, Icon } from 'antd';
import { WEB_SOCKETS_URL} from '../utils/Constants';
import SockJsClient from 'react-stomp'
class Game extends Component {
    constructor(props, context) {   
        super(props,  context);
        this.state = {
            virtualEdition: [],
            component: null,
        };
        this.loadVirtualEdition = this.loadVirtualEdition.bind(this);
        this.endGame = this.endGame.bind(this);
        this.start = this.start.bind(this);
        this.ready = this.ready.bind(this);
        this.onMessageReceive = this.onMessageReceive.bind(this);
    }

    async componentDidMount(){
       await this.loadVirtualEdition();
       this.setState({
        component: <SockJsClient
                    url={WEB_SOCKETS_URL}
                    topics={['/topic/config']}
                    ref={ (client) => { this.clientRef = client }}
                    onConnect={ () => { this.start(); this.ready() }}
                    onMessage={(message) => this.onMessageReceive(message)} /> 
        });
        
    }

    start(){
        try {
            this.clientRef.sendMessage('/ldod-game/connect', JSON.stringify({ userId: localStorage.getItem("currentUser"), virtualEdition: "LdoD-ok"}));
            return true;
          } catch(e) {
            return false;
          }
        
    }

    ready(){
        try {
            this.clientRef.sendMessage('/ldod-game/ready',JSON.stringify({ msg: "ready"}));
            return true;
          } catch(e) {
            console.log(e)
            return false;
          }
    }

    onMessageReceive(message){
        if( message[0] === this.state.users) return;
        if( message[0] === "ready"){
            this.setState({
                isLoading: false,
            })
            return; 
        }
        this.setState({
            users: message,
        })
        return;
    }
    
    async loadVirtualEdition(){
        this.setState({
            isLoading: true,
        })

        let request = await getVirtualEditionIndex("LdoD-ok");

        this.setState({
            virtualEdition: request,
        })
        
        await readyToStart(localStorage.getItem("currentUser"),"LdoD-ok");
    }

    async endGame(){
        let request = await endOfGame("LdoD-ok");
    }

    render() {
        if(this.state.isLoading) {
            return (
                <div>
                    <Alert
                        message="Loading resources"
                        description="Getting everything ready and waiting for users to join."
                        type="info"/>
                        {this.state.component}
                    <LoadingIndicator />
                </div>
            );
        }
        return ( 
            <div>
                <Icon type="user" />: {this.state.users}
                <VirtualEdition virtualEdition={this.state.virtualEdition} end={this.endGame()}/>
            </div>
    );
  }
}

export default withRouter(Game);