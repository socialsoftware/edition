import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import {FormGroup, Checkbox, } from 'react-bootstrap';
import { WEB_SOCKETS_URL} from '../utils/Constants';
import SockJsClient from 'react-stomp'
class Vote extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tags: [],
            count: 0,
        };
        this.handleVote = this.handleVote.bind(this);
    }
    
    componentDidMount(){
        this.setState({
            tags: this.props.tags,
        })
    }

    handleVote = (param) => (e) =>{
        let msg;
        if (e.target.checked) {
            msg = {tag: param.tag, vote: 1};
          } else {
            msg = {tag: param.tag, vote: -1};
          }
        this.sendMessage(msg);
    }

    sendMessage = (msg, selfMsg) => {
        try {
          this.clientRef.sendMessage('/ldod-game/votes', JSON.stringify({ userId: localStorage.getItem("currentUser"), msg: msg}));
          return true;
        } catch(e) {
          return false;
        }
    }

    
    render() {
        const voteViews = [];   
        let messages = this.state.tags;
        messages.forEach((m, mIndex) => {
            voteViews.push(
            <div>
                <Checkbox key={m.authorId + mIndex} onClick={this.handleVote(m)}>{m.tag} {this.state.count}
                </Checkbox>
            </div>)
            
        }); 
        return (
            <form>
            <SockJsClient
                    url={WEB_SOCKETS_URL}
                    topics={['/topic/votes']}
                    ref={ (client) => { this.clientRef = client }}
                    onMessage={(message) => this.props.handleMessageVote(message)} />
                <FormGroup>
                    <div>
                        {voteViews}
                    </div>
                </FormGroup>
            </form>
        );
    }
}


export default withRouter(Vote);