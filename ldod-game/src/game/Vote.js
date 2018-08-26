import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Glyphicon} from 'react-bootstrap';
import { WEB_SOCKETS_URL} from '../utils/Constants';
import SockJsClient from 'react-stomp'
import './Vote.css';
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
        let vote;
        if (e.target.checked) {
            vote =  1;
        } else {
            vote = -1;
        }
        this.sendMessage(param.tag, vote); 
    }

    sendMessage = (msg, vote, selfMsg) => {
        try {
          this.clientRef.sendMessage('/ldod-game/votes', JSON.stringify({ urlId: this.props.id, voterId: localStorage.getItem("currentUser"), msg: msg, vote: vote}));
          return true;
        } catch(e) {
          return false;
        }
    }

    handleMessageVote(message) {
        var dictionary = this.state.votes;
        let copy = [...this.state.votes];
        var temp = { authorId: message[1], tag: message[2], vote: message[3]};
        for(var i in dictionary){
            if(dictionary[i].tag === temp.tag){
                copy.splice(i, 1, temp);
                this.setState(({
                    votes: copy,
                }));
            }
        }
    }

    render() {
        const voteViews = [];   
        let votes = this.state.votes;
        votes.forEach((m, mIndex) => {
            voteViews.push(
            <div className="div-votes" key={mIndex}>
                <div>
                    <label>
                        <input type="checkbox" onClick={this.handleVote(m)}></input>
                        <span className="title">{m.tag}</span>
                        <span className="icon-up"><Glyphicon glyph="chevron-up" /></span>
                        <span className="label label-primary">{m.vote}</span>
                    </label>
                </div>
                {/*<span>{ m.tag }</span>
                <Checkbox key={mIndex} onClick={this.handleVote(m)} inline>
                    <Glyphicon glyph="ok" />{m.vote}
                </Checkbox>*/}
            </div>)
            
        }); 
        return (
            <div>
            {/*<form>*/}
            <SockJsClient
                    url={WEB_SOCKETS_URL}
                    topics={['/topic/votes']}
                    ref={ (client) => { this.clientRef = client }}
                    onMessage={(message) => this.handleMessageVote(message)} />
                <span className="icon-tags"><Glyphicon glyph="tags" /></span>
                <br></br>
                {voteViews}
                {/*<FormGroup>
                    {voteViews}
                </FormGroup>*/}
            {/*</form>*/}
            </div>
        );
    }
}


export default withRouter(Vote);