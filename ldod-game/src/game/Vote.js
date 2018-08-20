import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import {FormGroup, Checkbox, } from 'react-bootstrap';
import { WEB_SOCKETS_URL} from '../utils/Constants';
import SockJsClient from 'react-stomp'
class Vote extends Component {
    constructor(props) {
        super(props);
        this.state = {
            votes: [],
        };
        this.handleVote = this.handleVote.bind(this);
        this.handleMessageVote = this.handleMessageVote.bind(this);
    }
    
    componentDidMount(){
        this.setState({
            votes: this.props.initialTags,
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
          this.clientRef.sendMessage('/ldod-game/votes', JSON.stringify({ urlId: this.props.id, voterId: localStorage.getItem("currentUser"), msg: msg}));
          return true;
        } catch(e) {
          return false;
        }
    }

    handleMessageVote(message) {
        var temp = { authorId: message[0], tag: message[1].tag, vote: message[1].vote};
        this.setState(({
            votes: [...this.state.votes, temp]
        }));
    }

    render() {
        const voteViews = [];   
        let votes = this.state.votes;
        votes.forEach((m, mIndex) => {
            voteViews.push(
            <div>
                <Checkbox key={m.fragmentUrlId} onClick={this.handleVote(m)}>{m.tag} {m.vote}
                </Checkbox>
            </div>)
            
        }); 
        return (
            <form>
            <SockJsClient
                    url={WEB_SOCKETS_URL}
                    topics={['/topic/votes']}
                    ref={ (client) => { this.clientRef = client }}
                    onMessage={(message) => this.handleMessageVote(message)} />
                <FormGroup>
                    {voteViews}
                </FormGroup>
            </form>
        );
    }
}


export default withRouter(Vote);