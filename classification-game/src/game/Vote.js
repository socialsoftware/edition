import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { WEB_SOCKETS_URL, SUBSCRIBE_URL, APP_PREFIX} from '../utils/Constants';
import './Vote.css';
import { Table} from 'react-bootstrap';
import SockJsClient from 'react-stomp';
import AppContext from '../app/AppContext';
import {Provider} from '../app/AppContext';

class Vote extends Component {
    constructor(props) {
        super(props);
        this.state = {
            socket: null,
            votes: [],
            previousVotedTag: null,
        };
        this.handleMessageVote = this.handleMessageVote.bind(this);
    }
    
    componentDidMount(){
        this.setState({
            socket:  <SockJsClient
                        url={WEB_SOCKETS_URL}
                        topics={[SUBSCRIBE_URL + this.props.gameId +'/votes']}
                        ref={ (client) => { this.clientRef = client }}
                        onMessage={(message) => this.handleMessageVote(message)} />,
            votes: this.props.initialTags,
            seconds: this.props.seconds,
            topTag: this.props.topTag,
            winner: this.props.winner,
        })
        this.interval = setInterval(() => this.tick(), 1000);
    }

    componentWillUnmount() {
        clearInterval(this.interval);
        // <AppContext>
        //     <Provider value={{winner: this.state.winner, top: this.state.topTag}}>  
        //         {this.props.children}
        //     </Provider>
        // </AppContext>
    }

    componentDidUpdate(prevProps) {
        if (this.props.seconds !== prevProps.seconds) {
            this.setState({
                seconds: this.props.seconds,
                topTag: this.props.topTag,
            })
        }
    }
  
    tick() {
        this.setState(prevState => ({
          seconds: prevState.seconds - 1
        }));
    }

    sendMessage = (msg, vote) => {
        try {
          var number = this.props.round !== 3 ? this.props.index : this.props.limit;
          this.clientRef.sendMessage(APP_PREFIX + this.props.gameId + '/votes', JSON.stringify({ gameId: this.props.gameId, voterId: this.props.userId, msg: msg, vote: vote, paragraph: number}));
          return true;
        } catch(e) {
          return false;
        }
    }

    handleMessageVote(message) {
        var dictionary = this.state.votes;
        let copy = [...this.state.votes];
        var vote = Math.round(parseFloat(message[3]));
        var temp = { authorId: message[1], tag: message[2], vote: vote};
        for(var i in dictionary){
            if(dictionary[i].tag === temp.tag){
                copy.splice(i, 1, temp);
                this.setState(({
                    votes: copy,
                    topTag: message[4],
                    winner: message[5],
                }));
            }
        }
    }

    onChange = (param) => (e) => {
        if (this.props.round !== 3){
            this.setState({
                disabled: true,
            })    
            this.sendMessage(param.tag, 1.0);
            return 
        }

        if (this.state.previousVotedTag === null){
            this.setState({
                previousVotedTag: param.tag,
            })
            this.sendMessage(param.tag, 1.0); 
        }else{
            this.sendMessage(param.tag, 1.0); 
            this.sendMessage(this.state.previousVotedTag, -1.0); 
            this.setState({
                previousVotedTag: param.tag,
            })
        }
    }

    render() {
        const voteViews = [];
        let votes = this.state.votes;
        let top;
        let msg = "Choose which tag is better..."
        if(this.props.round !== 3){
            votes.forEach((m, index) => {
                    voteViews.push(
                        <div className="div-votes" key={index}>
                            <div>
                                <label>
                                    <span className="title">{m.tag}</span>
                                    <input name="voteGroup" type="radio" onChange={this.onChange(m)} disabled={this.state.disabled}></input>
                                </label>
                            </div>
                        </div>)
            });
        }

        else{
           top = <h3 className="text-center">Top tag: {this.state.topTag}</h3>;
           this.state.votes.forEach((m, index) => {
                
                    voteViews.push(
                        <div className="div-votes" key={index}>
                            <div>
                                <label>
                                    <span className="title">{m.tag}</span>
                                    <input name="voteGroup" type="radio" onChange={this.onChange(m)} disabled={this.props.hasEnded}></input>
                                    <span className="vote">{m.vote}</span>
                                </label>
                            </div>
                        </div>)
                
            });

        }

        return (
            <div>
                {this.state.socket}
                {top}
                <Table>
                    <thead>
                        <tr>
                            <th><span className="glyphicon glyphicon-tag"></span>    {this.props.round !== 3 ? msg : null}</th>
                        {this.props.round !== 3 ? <th> You suggested so far:  {this.props.userSuggestedTags} </th> : null}
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <div className="div-votes">
                                    {voteViews}
                                </div>			
                            </td>
                        </tr>
                    </tbody>
            	</Table>
            </div>
        );
    }
}


export default withRouter(Vote);