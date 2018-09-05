import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import Paragraph from './Paragraph';
import { ProgressBar } from 'react-bootstrap';
import './Fragment.css';
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
            round: 1,
            tags: [],
        };
        this.splitIntoParagraph = this.splitIntoParagraph.bind(this);
        this.nextParagraph = this.nextParagraph.bind(this);
        
    }
    
    componentDidMount() {
        var text = this.splitIntoParagraph(this.props.fragment.text).text;
        var time = this.splitIntoParagraph(this.props.fragment.text).time;
        console.log(time);
        this.setState({
            fragment: this.props.fragment,
            text: this.props.fragment.text,
            paragraphText: text,
            secondAndText: time,
         });

    }

    splitIntoParagraph(text){
        var secondAndTextTemp = [];
        var result = [];
        var paragraph = text.split("<br></p>");
        var testInput = paragraph;
        var regex = /(<([^>]+)>)/ig;
        var largeInt = 100000000;

        // Remove tags and spaces on all text
        for(var i = 0; i < testInput.length; i++){
            testInput[i] = testInput[i].replace(regex, "").trim();
            var size = testInput[i].length
            //var temp = size >= 200 ? size/6 : 30;
            var temp = size >= 200 ? 10 + i/largeInt : 5 + i/largeInt;
            secondAndTextTemp[i] = temp;
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
        
        var res = { text: result, time: secondAndTextTemp};
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

    render() {
        return (
            <div> 
                <Paragraph 
                    limit={this.state.paragraphText.length}                
                    fragment={this.state.fragment}
                    nextParagraph={this.nextParagraph} 
                    paragraphText={this.state.paragraphText[this.state.index]}
                    seconds={this.state.secondAndText[this.state.index]} 
                    round={this.state.round} 
                    endFragment={this.props.endFragment}/>
                <div className="div-progress">
                    <ProgressBar min={0} bsStyle="success"active now={this.state.index+1} max={this.state.paragraphText.length}/>
                </div>
            </div>
        );
    }
}


export default withRouter(Fragment);