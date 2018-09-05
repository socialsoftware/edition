import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import VirtualEdition from './VirtualEdition';
import { getVirtualEditionIndex } from '../utils/APIUtils';
import LoadingIndicator  from '../common/LoadingIndicator';
import { Alert, Badge} from 'antd';
import { WEB_SOCKETS_URL} from '../utils/Constants';
import './Game.css';
import SockJsClient from 'react-stomp'
class Game extends Component {
    constructor(props, context) {   
        super(props,  context);
        this.state = {
            virtualEdition: [],
            socket: null,
            currentUsers: 0,
            gameId: null,
        };
        this.loadVirtualEdition = this.loadVirtualEdition.bind(this);
        this.connect = this.connect.bind(this);
        this.onMessageReceive = this.onMessageReceive.bind(this);
    }

    async componentDidMount(){
       await this.loadVirtualEdition();
       this.setState({
        socket: <SockJsClient
                    url={WEB_SOCKETS_URL}
                    topics={['/topic/config']}
                    ref={ (client) => { this.clientRef = client }}
                    onConnect={ () => { this.connect()}}
                    onMessage={(message) => this.onMessageReceive(message)} /> 
        });
        
    }

    connect(){
        try {
            this.clientRef.sendMessage('/ldod-game/connect', JSON.stringify({ userId: localStorage.getItem("currentUser"), virtualEdition: "LdoD-ok"}));
            return true;
          } catch(e) {
            return false;
          }
        
    }

    onMessageReceive(message) {
        var users = message[0]
        var command = message[1];
        var id = message[2];
        if(command === "ready"){
            this.setState({
                currentUsers: users,
                gameId: id,
                isLoading: false,
            })
            return; 
        }
    }


    
    async loadVirtualEdition(){
        this.setState({
            isLoading: true,
        })

        let request = await getVirtualEditionIndex("LdoD-ok");

        this.setState({
            virtualEdition: request,
        })
        
    }



    render() {
        if(this.state.isLoading) {
            return (
                <div>
                    <Alert
                        style={{ fontSize: '20px', fontFamily: 'Ubuntu' }}
                        message="Loading resources and waiting for at least one user to join."
                        type="info"
                        banner />
                        {this.state.socket}
                    <LoadingIndicator />
                </div>
            );
        }
        return ( 
            <div>
                <div>
                    <div className="users">
                        <Badge count={this.state.currentUsers} title="Current online users"  style={{ backgroundColor: '#2ecc71', fontSize: '15px' }}>  
                        <span className="glyphicon glyphicon-user"  style={{ fontSize: '25px', }}></span>
                    </Badge>
                    </div>
                </div>
                <VirtualEdition virtualEdition={this.state.virtualEdition} gameId={this.state.gameId}/>
            </div>
    );
  }
}

export default withRouter(Game);