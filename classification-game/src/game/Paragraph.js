import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import Tag from './Tag';
import Vote from './Vote';
import Review  from './Review';
import { WEB_SOCKETS_URL, SUBSCRIBE_URL, APP_PREFIX, VOTE_TIME} from '../utils/Constants';
import { Steps } from 'antd';
import { Grid, Alert} from 'react-bootstrap';
import LoadingIndicator  from '../common/LoadingIndicator';
import SockJsClient from 'react-stomp'
var ReactCountdownClock = require("react-countdown-clock")
const Step = Steps.Step;
class Paragrah extends Component {
    constructor(props) {
        super(props);
        this.state = {
            title: "",
            urlId: "",
            paragraphText: "",
            tags: [],
            round: 1,
            seconds: 5,
            disabled: false,
            fullText: "",
            userSuggestedTags: [],
        };
       this.connect = this.connect.bind(this);
       this.handleMessage = this.handleMessage.bind(this);
    }

    componentDidUpdate(prevProps, prevState) {

        if (prevProps.round !== this.props.round && prevProps.paragraphText === this.props.paragraphText) {
            this.setState({
                title: this.props.title,
                urlId: this.props.urlId,
                paragraphText: this.props.paragraphText,
                fullText: this.props.text,
                seconds: this.props.seconds,
                round: this.props.round,
                totalTime: this.props.totalTime,
                disabled: false,
                isLoading: true,
                socket: <SockJsClient
                url={WEB_SOCKETS_URL}
                topics={[ SUBSCRIBE_URL + this.props.gameId +'/sync']}
                ref={ (client) => { this.clientRef = client }}
                onConnect={ () => { this.connect() }}
                onMessage={(message) => this.handleMessage(message)} />
            });
        }

        else if (prevProps.round !== this.props.round && prevProps.paragraphText !== this.props.paragraphText) {
            this.setState({
                tags: [],
                title: this.props.title,
                urlId: this.props.urlId,
                paragraphText: this.props.paragraphText,
                fullText: this.props.text,
                seconds: this.props.seconds,
                round: this.props.round,
                totalTime: this.props.totalTime,
                disabled: false,
                isLoading: true,
                socket: <SockJsClient
                url={WEB_SOCKETS_URL}
                topics={[ SUBSCRIBE_URL + this.props.gameId +'/sync']}
                ref={ (client) => { this.clientRef = client }}
                onConnect={ () => { this.connect() }}
                onMessage={(message) => this.handleMessage(message)} />
            });
        }

        
    }
    
    connect() {
        try{
            this.clientRef.sendMessage(APP_PREFIX + this.props.gameId + '/sync', JSON.stringify({ gameId: this.props.gameId, userId: this.props.userId}));
            return true;
        } catch(e) {
            return false;
        }
    }

    handleMessage(message){
        if(message[0] === "continue") {
            this.setState({
                isLoading: false,
            })
        }
    }
    
    handleMessageTag(message) {
        
        var dictionary = this.state.tags;
        let copy = [...this.state.tags];
        var repeated = false;
        var temp = { fragmentUrlId: message[0], authorId: message[1], tag: message[2], vote: message[3]};
         
        // Check repeated suggested tags and only save first instance
        for(var i in dictionary){
            if(dictionary[i].tag === temp.tag){
                copy.splice(i, 1, temp);
                this.setState(({
                    tags: copy,
                }));
                repeated = true;
            }
        }

        //Only allow one user submission per paragraph
        if(temp.authorId === localStorage.getItem("currentUser")){
            this.setState(({
                userSuggestedTags: [...this.state.userSuggestedTags, temp.tag+ "   "],
                disabled: true,
            }));
        }

        if(!repeated){
            this.setState(({
                tags: [...this.state.tags, temp],
            }));
        }
    }
    
    render() {
        let style = {   marginTop: "-45px",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        paddingBottom: "25px",};
        let stepsRender =  
            <Steps direction="horizontal" current={this.props.round-1}>
                <Step title="Tag"/>
                <Step title="Vote"/>
                <Step title="Review"/>
            </Steps>
        let paragrahRender = 
            <div>
                <h4 className="text-center">{this.state.title}</h4>
                <div className="well" style={{ fontFamily: 'georgia', fontSize: 'medium'}}>
                    <div dangerouslySetInnerHTML={{__html: this.state.paragraphText}}></div>
                </div>
            </div>

        let roundRender;
        if(this.state.isLoading) {
            return (
                <div>
                    <Alert bsStyle="info">
                        <strong>Syncing before next step...</strong>
                    </Alert>
                    {this.state.socket}
                    <LoadingIndicator />
                </div>
            );
        }
        if (this.props.round === 1) {
            roundRender =
                <div>
                <div style={style}>
                        <ReactCountdownClock 
                            seconds={this.props.seconds}
                            color="#2ecc71"
                            size={80}
                            showMilliseconds={false}
                            onComplete={()=>this.props.chooseNextStep("voting")}/>
                    </div>
                    {stepsRender}
                    {paragrahRender}
                    <Tag 
                        gameId={this.props.gameId}
                        userId={this.props.userId}
                        index={this.props.index}
                        handleMessageTag={this.handleMessageTag.bind(this)} 
                        disabled={this.state.disabled}/>
                </div>
          } 
          else if(this.props.round === 2) {
            roundRender =
                <div>
                <div style={style}>
                <ReactCountdownClock 
                            seconds={VOTE_TIME+0.01}
                            color="#2ecc71"
                            size={80}
                            showMilliseconds={false}
                            onComplete={()=>this.props.chooseNextStep("taggingNextParagraph")}/>
                </div>
                    {stepsRender}
                    {paragrahRender}    
                    <Vote 
                        seconds={this.props.seconds} 
                        gameId={this.props.gameId}
                        userSuggestedTags={this.state.userSuggestedTags}
                        userId={this.props.userId}
                        limit={this.props.limit}
                        index={this.props.index}
                        initialTags={this.state.tags}/>
                </div>
            } 
        else if(this.props.round === 3){
            return(
                <div>
                    <Review
                        gameId={this.props.gameId}
                        userId={this.props.userId} 
                        limit={this.props.limit}
                        steps={stepsRender} 
                        endFragment={this.props.endFragment} 
                        totalTime={this.state.totalTime} 
                        initialTags={this.state.tags} 
                        title={this.state.title} 
                        fullText={this.state.fullText}/>
                </div>
            );
        }
        return (
            <Grid fluid>
                {roundRender}
            </Grid>
        );
    }
}


export default withRouter(Paragrah);