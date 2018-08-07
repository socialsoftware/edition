import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import {Glyphicon, Button, FormControl, FormGroup, InputGroup} from 'react-bootstrap';
import './Tag.css';
import { Tag as TagD } from 'antd';
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
        // TODO: websocket needs to update tag which then cals the props.setTag in order to Fragment have info
        this.props.setTag(input);
        tags += "<br>" + input;
        var display = document.getElementById("display")
        display.innerHTML="<p>" + tags + "</p>";
        document.getElementById("form").reset();
        e.preventDefault();
    }


    render() {
        /*const tagViews = [];
        let messages = this.props.ws.getMessages();
        messages.forEach((m, mIndex) => {
            tagViews.push(<TagD color="blue">{m.tag}</TagD>)
        });*/
        return (
            <div> 
                <div id="display"></div>
                <form id="form" onSubmit={(e) => {this.handleTag(e)}}>
                    <FormGroup>
                        <InputGroup>
                            <InputGroup.Addon><Glyphicon glyph="tag" /></InputGroup.Addon>
                            <FormControl id="tag" type="text" autoFocus/>
                        </InputGroup>
                        <Button type="submit">Tag this paragraph</Button>
                    </FormGroup>
                </form>
                {/*tagViews*/}
            </div>
        );
    }
}


export default withRouter(Tag);