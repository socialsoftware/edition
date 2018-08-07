import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import {Glyphicon} from 'react-bootstrap';
var tags = " "
class Tag extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tags: [],
        };
        this.handleTag = this.handleTag.bind(this);
    }
    
    componentDidMount(){
        tags = " ";
    }

    handleTag = (e) => {
        var input = document.forms["form"]["tag"].value;
        this.props.ws.sendMessage(input);
        /*for(var x of this.props.ws.getMessages()){
            alert("authorID: "+ x.authorId + " tag: " + x.tag + "\n");
            tags+= x.tag;
        }*/
        tags += "<br>" + input;
        var display = document.getElementById("display")
        display.innerHTML="<p>" + tags + "</p>";
        document.getElementById("form").reset();
        e.preventDefault();
    }


    render() { 
        return (
            <div> 
                <form id="form" onSubmit={(e) => {this.handleTag(e)}}>
                    <input type="text" id="tag"/>
                    <input type="submit" value="Tag this" placeholder="Submit a tag for this paragraph"/>
                    <Glyphicon glyph="tag" />
                </form>
                <div id="display"></div>
            </div>
        );
    }
}


export default withRouter(Tag);