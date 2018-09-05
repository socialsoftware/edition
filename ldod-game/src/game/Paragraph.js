import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import './Paragraph.css';
import Tag from './Tag';
import Vote from './Vote';
import { Steps } from 'antd';
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
        const tagViews = [];
        let tags = this.state.tags;
        tags.forEach((m, mIndex) => {
            tagViews.push(
            <div>
                <p>{m.tag}<br></br></p>
            </div>
            )
        });
        let stepsRender =  
            <Steps className="steps" direction="horizontal" current={this.props.round-1}>
                <Step title="Tag"/>
                <Step title="Vote"/>
                <Step title="Review"/>
            </Steps>
        let paragrahRender = 
            <div className="content">
                <h4 className="text-center">{this.state.title}</h4>
                <div className="well" style={{ fontFamily: 'georgia', fontSize: 'medium'}}>
                    <div dangerouslySetInnerHTML={{__html: this.state.paragraphText}}></div>
                </div>
            </div>
        let clockRender = <div className="clock">
                                <ReactCountdownClock 
                                    seconds={this.props.seconds}
                                    color="#2ecc71"
                                    size={80}
                                    showMilliseconds={false}
                                    onComplete={this.props.nextParagraph}/>
                            </div>;
        let roundRender;
        if (this.props.round === 1) {
            roundRender =
                <div>
                    {clockRender}
                    {stepsRender}
                    {paragrahRender}
                    <Tag 
                        id={this.state.urlId} 
                        handleMessageTag={this.handleMessageTag.bind(this)} 
                        disabled={this.state.disabled}/>
                </div>
          } else {
            roundRender =
                <div>
                    {clockRender}
                    {stepsRender}
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
                    <div className="clock">
                        <ReactCountdownClock seconds={this.props.seconds}
                            color="#2ecc71"
                            size={80}
                            showMilliseconds={false}
                            onComplete={this.props.endFragment}/>
                    </div>                
                    {stepsRender}
                    <section className="intro">
                        <div className="col-lg-6 col-sm-12 left">
                            <div className="content">
                                <h4 className="text-center">{this.state.title}</h4>
                                <div className="well" style={{ fontFamily: 'georgia', fontSize: 'small'}}>
                                    <div dangerouslySetInnerHTML={{__html: this.state.fullText}}></div>
                                </div>
                            </div>
                        </div>
                        <div className="col-lg-6 col-sm-12 right">
                            <div>
                                <h4 className="text-center">Tags submitted:</h4>
                                <div className="well" style={{ fontFamily: 'georgia', fontSize: 'small'}}>
                                    {tagViews} 
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
            );
        }
        return (
            <div>
                {roundRender}
            </div>
        );
    }
}


export default withRouter(Paragrah);