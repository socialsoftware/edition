import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { getVirtualEditionFragment } from '../utils/APIUtils';
import Paragraph from './Paragraph';
import WebSockets from './WebSockets';
import './Fragment.css';
var ReactCountdownClock = require("react-countdown-clock")

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
            index: 1,
            seconds: 5,
        };
        this.loadFragment = this.loadFragment.bind(this);
        this.paragraphSplit = this.paragraphSplit.bind(this);
        this.nextParagraph = this.nextParagraph.bind(this);
        this.handleTag= this.handleTag.bind(this);
    }
    
    componentDidMount() {
        this.setState({
            title: this.props.fragment.title,
            number: this.props.fragment.number,
            urlId: this.props.fragment.urlId,
        });

        this.loadFragment(this.props.acronym, this.props.fragment.urlId);
    }

    loadFragment(acronym, urlId) {
        let request = getVirtualEditionFragment(acronym, urlId);

        request.then(response =>{
            this.setState({
                text: response.text,
            })
            this.paragraphSplit(response.text);
        });
        
    }

    paragraphSplit(text){
        var paragraph = text.split("</p>");
        for (var i = 0; i < paragraph.length; i++) {
            this.setState({
                splitText: [...this.state.splitText, paragraph[i]]
            });        
        }
    }

    nextParagraph(){
        if(this.state.index <= 5){
            this.setState((prevState, props) => ({
                index: prevState.index + 1,
                seconds: prevState.seconds + 0.0000001,
            }));
        }
    }

    handleTag = (e) => {
        var a = document.forms["form"]["tag"].value;
        var display=document.getElementById("display")
        display.innerHTML="<p>" + a + "</p>";
        this.child.sendMessage(a);
        e.preventDefault();
    }


    render() {
        return (
            <div>
                <WebSockets currentUser={"gm"} onRef={ref => (this.child = ref)} />
                
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
                    </form>
                </div>
                <div id="display"></div>
            </div>
        );
    }
}


export default withRouter(Fragment);