import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { WEB_SOCKETS_URL} from '../utils/Constants';
import SockJsClient from 'react-stomp'
import './Vote.css';
class Vote extends Component {
    constructor(props) {
        super(props);
        this.state = {
            socket: null,
            votes: [],
            seconds: 0.0,
            previousVote: null,
            previousScore: null,
        };
        this.handleVote = this.handleVote.bind(this);
        this.handleMessageVote = this.handleMessageVote.bind(this);
    }
    
    componentDidMount(){
        this.setState({
            socket:  <SockJsClient
                        url={WEB_SOCKETS_URL}
                        topics={['/topic/votes']}
                        ref={ (client) => { this.clientRef = client }}
                        onMessage={(message) => this.handleMessageVote(message)} />,
            votes: this.props.initialTags,
            seconds: this.props.seconds,
        })
        this.interval = setInterval(() => this.tick(), 1000);
    }

    componentWillUnmount() {
        clearInterval(this.interval);
    }

    componentDidUpdate(prevProps) {
        if (this.props.seconds !== prevProps.seconds) {
            this.setState({
                seconds: this.props.seconds,
            })
        }
    }
  
    tick() {
        this.setState(prevState => ({
          seconds: prevState.seconds - 1
        }));
    }

    handleVote = (param) => (e) =>{
        let vote;
        if (e.target.checked) {
            vote =  1.0 + this.state.seconds/10;
        } else {
            vote = -1.0 - this.state.seconds/10;
        }
        var res = vote.toFixed(2);
        this.sendMessage(param.tag, res); 
    }

    sendMessage = (msg, vote) => {
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
        var vote = parseInt(message[3], 10);
        var temp = { authorId: message[1], tag: message[2], vote: vote};
        for(var i in dictionary){
            if(dictionary[i].tag === temp.tag){
                copy.splice(i, 1, temp);
                this.setState(({
                    votes: copy,
                }));
            }
        }
    }

    onChange = (param) => (e) => {
        let vote;
        vote =  1.0 + this.state.seconds/10;
        var res = vote.toFixed(2);
        this.setState({
            previousVote: param.tag,
            previousScore: -vote,
        })
        if ( this.state.previousVote !== null){
            var value = this.state.previousScore.toFixed(2);
            this.sendMessage(this.state.previousVote, value);     
            this.sendMessage(param.tag, res);     
        }else{
            this.sendMessage(param.tag, res); 
        }
    }

    render() {
        const voteViews = [];
        let votes = this.state.votes;
        let top = [];
        votes.forEach((m, index) => {
            voteViews.push(
                <div className="div-votes" key={index}>
                    <div>
                        <label>
                            <input name="voteGroup" type="radio" onChange={this.onChange(m)}></input>
                            <span className="title">{m.tag}</span>
                            <span className="vote">{Math.round(m.vote)}</span>
                        </label>
                    </div>
                </div>)
            }); 
        if(this.props.round === 3){
            /*var test = Math.max(...votes.map(o => o.vote));
            for(var i in votes){
                if(votes[i].vote === test){ top.push(<h1>Current top: {votes[i].tag}</h1>);}
                break;
            }*/
            var max = votes.reduce.call(votes, (prev, current) =>
                { 
                    return (prev.vote > current.vote) ? prev : current;
                }, "array is empty")
            top = <h1>Current top: {max.tag}</h1>;
        }

        return (
            <div>
                {this.state.socket}
                {top}
                <table className="table">
                    <thead>
                        <tr>
                            <th><span className="glyphicon glyphicon-tag"></span></th>
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
            	</table>
            </div>
        );
    }
}


export default withRouter(Vote);