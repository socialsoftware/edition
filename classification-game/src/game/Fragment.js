import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import Paragraph from './Paragraph';
import { ProgressBar, Grid } from 'react-bootstrap';
import { TIME_FACTOR } from '../utils/Constants';
class Fragment extends Component {
    constructor(props) {
        super(props);
        this.state = {
            fragment: { },
            secondAndText: [],
            text: "",
            paragraphText: [],
            index: 0,
            seconds: 5.0,
            round: 0,
            tags: [],
            totalTime: 0,
        };
        this.splitIntoParagraph = this.splitIntoParagraph.bind(this);
        this.nextParagraph = this.nextParagraph.bind(this);
        this.chooseNextStep = this.chooseNextStep.bind(this);
        
    }
    
    componentDidMount() {
        var text = this.splitIntoParagraph(this.props.fragment.text).text;
        var time = this.splitIntoParagraph(this.props.fragment.text).time;
        var totalTime = this.splitIntoParagraph(this.props.fragment.text).totalTime;
        this.setState({
            text: this.props.fragment.text,
            title: this.props.fragment.title,
            paragraphText: text,
            secondAndText: time,
            urlId: this.props.fragment.urlId,
            voting: false,
            tagging: true,
            round: 1,
            totalTime: totalTime,
         });

    }

    splitIntoParagraph(text){
        var secondAndTextTemp = [];
        var result = [];
        var paragraph = text.split("</p>");
        var testInput = paragraph;
        var regex = /(<([^>]+)>)/ig;
        //var largeInt = 100000000;
        var totalTime = 0;

        // Remove tags and spaces on all text
        for(var i = 0; i < testInput.length; i++){
            testInput[i] = testInput[i].replace(regex, "").trim();
            var size = testInput[i].length
            var temp = size >= 181 ? size/TIME_FACTOR : 45;
            //var temp = size >= 200 ? 45 + i/largeInt : 30 + i/largeInt;
            secondAndTextTemp[i] = temp;
            totalTime += temp;
        }  
        
        // Check first paragraph and remove it if matches criteria
        if(testInput[0].includes("L. do D.") || testInput[0].length <= 13){
            paragraph.splice(0,1);
            secondAndTextTemp.splice(0,1);
        }

        for (i = 0; i < paragraph.length; i++) {
            if (/\S/.test(paragraph[i])) {
                result.push(paragraph[i]);
            }
        }
        
        var res = { text: result, time: secondAndTextTemp, totalTime};
        return res;
    }

    nextParagraph(){
        var currentIndex= this.state.index;
        var nextIndex = currentIndex+1;
        
        // First round paragraph increment
        if(this.state.index < this.state.paragraphText.length - 1){ 
            this.setState((prevState) => ({
                index: prevState.index + 1,
                seconds: this.state.secondAndText[nextIndex],
            }));
        }
        // Change to round 2 and reset paragraph order
        else if( this.state.index === this.state.paragraphText.length-1){
            this.setState((prevState) => ({
                index: 0,
                seconds: prevState.secondAndText[0],
                round: prevState.round + 1,
            }));
        }
    }

    chooseNextStep(command){
        //console.log("chooseNextStep " + command);
        var currentIndex = this.state.index;
        var nextIndex = currentIndex+1;
        switch(command) {
            case "voting":
                this.setState((prevState) => ({
                    round: 2,
                    seconds: prevState.seconds,
                }));
                return;
            
            case "taggingNextParagraph":
                if( this.state.index === this.state.paragraphText.length-1){
                    this.setState((prevState) => ({
                        round: 3,
                    }));
                    return;
                }
                this.setState((prevState) => ({
                    round: 1,
                    index: prevState.index + 1,
                    seconds: this.state.secondAndText[nextIndex]+0.1,
                }));
                return;
            
            default:
                console.log("error");
                return;
        }   
        
    }

    render() {
        return (
            <Grid fluid>
                    <Paragraph 
                    title={this.state.title}
                    text={this.state.text}
                    userId={this.props.userId}
                    gameId={this.props.gameId}
                    limit={this.state.paragraphText.length}                
                    nextParagraph={this.nextParagraph} 
                    paragraphText={this.state.paragraphText[this.state.index]}
                    seconds={this.state.secondAndText[this.state.index]}
                    index={this.state.index} 
                    round={this.state.round} 
                    totalTime={this.state.totalTime}
                    chooseNextStep={this.chooseNextStep}
                    endFragment={this.props.endFragment}/>
                {this.state.round === 3 ? (<div>{}</div>) : 
                (
                    <ProgressBar min={0} bsStyle="success"active now={this.state.index+1} max={this.state.paragraphText.length}/>)}
            </Grid>
        );
    }
}


export default withRouter(Fragment);