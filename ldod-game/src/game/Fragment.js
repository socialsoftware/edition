import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import {Glyphicon} from 'react-bootstrap';
import Paragraph from './Paragraph';
import WebSockets from './WebSockets';
import './Fragment.css';
var ReactCountdownClock = require("react-countdown-clock")
var tags = " "
class Fragment extends Component {
    constructor(props) {
        super(props);
        this.state = {
            title: "",
            heteronyms: [],
            dates: [],
            hasLdoDLabel: false,
            number: 0,
            urlId: "",
            text: "",
            splitText: [],
            index: 0,
            seconds: 5,
        };
        this.paragraphSplit = this.paragraphSplit.bind(this);
        this.nextParagraph = this.nextParagraph.bind(this);
        this.handleTag= this.handleTag.bind(this);
    }
    
    componentDidMount() {
        this.setState({
            title: this.props.fragment.title,
            number: this.props.fragment.number,
            urlId: this.props.fragment.urlId,
            text: this.props.fragment.text,
            splitText: this.paragraphSplit(this.props.fragment.text),
        });

    }
    paragraphSplit(text){
        var res = [];
        var paragraph = text.split("<br></p>");
        if((paragraph[0].indexOf("<u>") > -1) ){
            paragraph.splice(0,1);
        }
        for (var i = 0; i < paragraph.length; i++) {
            if (/\S/.test(paragraph[i])) {
                res.push(paragraph[i]);
            }
        }
        return res;
    }

    nextParagraph(){
        if(this.state.index < this.state.splitText.length - 1){
            this.setState((prevState, props) => ({
                index: prevState.index + 1,
                seconds: prevState.seconds + 0.0000001,
            }));
        }
    }

    handleTag = (e) => {
        var input = document.forms["form"]["tag"].value;
        this.child.sendMessage(input);
        for(var x of this.child.getTags()){
            alert("authorID: "+ x.authorId + " tag: " + x.tag + "\n");
            tags+= x.tag;
        }
        tags += "<br>" + input;
        var display = document.getElementById("display")
        display.innerHTML="<p>" + tags + "</p>";
        e.preventDefault();
    }


    render() { 
        return (
            <div>
                <WebSockets onRef={ref => (this.child = ref)} />
                   
                <div className="clock">
                    <ReactCountdownClock seconds={this.state.seconds}
                    color="#c0392b"
                    size={100}
                    onComplete={this.nextParagraph}
                    />
                </div>
                <Paragraph text={this.state.splitText[this.state.index]} title={this.state.title} />
                <div>
                    <form id="form">
                        <input type="text" id="tag"/>
                        <input type="submit" onClick={(e) => {this.handleTag(e)}} value="Submit a tag for this paragraph"/>
                        <Glyphicon glyph="tag" />
                    </form>
                </div>
                <div id="display"></div>
            </div>
        );
    }
}


export default withRouter(Fragment);