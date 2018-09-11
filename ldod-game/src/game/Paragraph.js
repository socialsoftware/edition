import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import Tag from './Tag';
import Vote from './Vote';
import Review  from './Review';
import { Steps } from 'antd';
import { Grid} from 'react-bootstrap';
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
        let clockRender = <div style={style}>
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
                    <Review
                        limit={this.props.limit}
                        id={this.state.urlId} 
                        steps={stepsRender} 
                        endFragment={this.props.endFragment} 
                        seconds={this.state.seconds} 
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