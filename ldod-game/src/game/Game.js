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
    }

    async componentDidMount(){
       await this.loadVirtualEdition();
       this.setState({
        component: <SockJsClient
                    url={WEB_SOCKETS_URL}
                    topics={['/topic/config']}
                    ref={ (client) => { this.clientRef = client }}
                    onConnect={ () => { this.start() }}
                    onMessage={this.onMessageReceive.bind(this)} /> 
        });
        
    }

    start(){
        try {
            this.clientRef.sendMessage('/ldod-game/start', JSON.stringify({ userId: localStorage.getItem("currentUser"), virtualEdition: "LdoD-ok"}));
            return true;
          } catch(e) {
            return false;
          }
        
    }

    onMessageReceive(message){
        if( message === this.state.users) return;
        this.setState({
            users: message,
        })
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
        this.setState({
            isLoading: false,
        })
    }

    async endGame(){
        let request = await endOfGame("LdoD-ok");
        console.log(request);
    }

    render() {
        if(this.state.isLoading) {
            return (
                <div>
                    <Alert
                        message="Loading resources"
                        description="Getting everything and waiting for users to join."
                        type="info"/>
                    <LoadingIndicator />
                </div>
            );
        }
        return ( 
            <div>
                <Icon type="user" />: {this.state.users}
                {this.state.component}
                {this.start()}
                <VirtualEdition virtualEdition={this.state.virtualEdition} end={this.endGame}/>
            </div>
    );
  }
}

export default withRouter(Game);