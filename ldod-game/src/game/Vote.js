import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { WEB_SOCKETS_URL} from '../utils/Constants';
import SockJsClient from 'react-stomp'
import './Vote.css';
class Vote extends Component {
    constructor(props) {
        super(props);
        this.state = {
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
            votes.forEach((m, mIndex) => {
                voteViews.push(
                <div className="div-votes" key={mIndex}>
                    <div>
                        <label>
                            <input name="voteGroup" type="radio" onChange={this.onChange(m)}></input>
                            <span className="title">{m.tag}</span>
                            <span className="vote">{Math.round(m.vote)}</span>
                        </label>
                    </div>
                </div>)
        }); 

        return (
            <div>
                <SockJsClient
                    url={WEB_SOCKETS_URL}
                    topics={['/topic/votes']}
                    ref={ (client) => { this.clientRef = client }}
                    onMessage={(message) => this.handleMessageVote(message)} />
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