import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Panel, ProgressBar } from 'react-bootstrap';
import './Paragraph.css';
import Tag from './Tag';
import Vote from './Vote';
var ReactCountdownClock = require("react-countdown-clock")

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
        };
    }

    componentDidUpdate(prevProps, prevState) {

        if (prevProps.paragraphText !== this.props.paragraphText || prevProps.round !== this.props.round) {
            this.setState({
                title: this.props.fragment.title,
                urlId: this.props.fragment.urlId,
                paragraphText: this.props.paragraphText,
                fullText: this.props.fragment.text,
                seconds: this.props.seconds,
                round: this.props.round,
                disabled: false,
            });
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
        let paragrahRender = 
            <div>
                <Panel bsStyle="primary" defaultExpanded>
                    <Panel.Heading>
                        <Panel.Title className="panel-title" componentClass="h4" toggle>{this.state.title}</Panel.Title>
                    </Panel.Heading>
                    <Panel.Body >
                        <div dangerouslySetInnerHTML={{__html: this.state.paragraphText}}></div>
                    </Panel.Body>
                </Panel>
            </div>
        
        let roundRender;
        if (this.props.round === 1) {
            roundRender =
                <div>
                    <span className="text">Round 1:</span>
                    <div className="clock">
                        <ReactCountdownClock 
                            seconds={this.props.seconds}
                            color="#2ecc71"
                            size={80}
                            showMilliseconds={false}
                            onComplete={this.props.nextParagraph}/>
                    </div>
                    {paragrahRender}
                    <Tag 
                        id={this.state.urlId} 
                        handleMessageTag={this.handleMessageTag.bind(this)} 
                        disabled={this.state.disabled}/>
                </div>
          } else {
            roundRender =
                <div>
                    <span className="text">Round 2:</span>
                    <div className="clock">
                        <ReactCountdownClock seconds={this.props.seconds}
                            color="#8e44ad"
                            size={80}
                            showMilliseconds={false}
                            onComplete={this.props.nextParagraph}
                        />
                    </div>
                    {paragrahRender}    
                    <Vote 
                        seconds={this.props.seconds} 
                        id={this.state.urlId} 
                        initialTags={this.state.tags}/>
                </div>
            }
        if(this.props.round === 3){
            return(
                <div>
                    <span className="text">Round 3:</span>
                    <div className="clock">
                        <ReactCountdownClock seconds={this.props.seconds}
                            color="#f9ca24"
                            size={80}
                            showMilliseconds={false}
                            onComplete={this.props.endFragment}
                        />
                    </div>                
                    <Panel bsStyle="primary" defaultExpanded>
                        <Panel.Heading>
                            <Panel.Title className="panel-title" componentClass="h4" toggle>{this.state.title}</Panel.Title>
                        </Panel.Heading>
                        <Panel.Body >
                            <div dangerouslySetInnerHTML={{__html: this.state.fullText}}></div>
                        </Panel.Body>
                    </Panel>
            </div>
            );
        }
        return (
            <div>
                {roundRender}
                {/*<div className="div-progress">
                    <ProgressBar min={0} bsStyle="success"active now={this.props.round} max={3}/>
                </div> TODO */}
            </div>
        );
    }
}


export default withRouter(Paragrah);