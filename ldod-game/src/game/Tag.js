import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import {Glyphicon, Button, FormControl, FormGroup, InputGroup} from 'react-bootstrap';
import './Tag.css';
import { Tag as TagD } from 'antd';
import { WEB_SOCKETS_URL} from '../utils/Constants';
import SockJsClient from 'react-stomp'
var tags = " ";
class Tag extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tags: [],
        };
        this.handleTag = this.handleTag.bind(this);
    }
    
    handleTag = (e) => {
        var input = document.forms["form"]["tag"].value;
        this.sendMessage(input);
        tags += "<br>" + input;
        document.getElementById("form").reset();
        this.setState({
            tags: [...this.state.tags, input]
        })
        e.preventDefault();
    }

    sendMessage = (msg, selfMsg) => {
        try {
            this.clientRef.sendMessage('/ldod-game/tags', JSON.stringify({ urlId: this.props.id, authorId: localStorage.getItem("currentUser"), msg: msg, vote: 1}));
            return true;
        } catch(e) {
            return false;
        }
    }

    render() {
        const tagViews = [];
        let messages = this.state.tags;
        messages.forEach((m, mIndex) => {
            tagViews.push(<TagD color="blue" key={mIndex} >{m}</TagD>)
        });
        return (
            <div> 
            <SockJsClient
                    url={WEB_SOCKETS_URL}
                    topics={['/topic/tags']}
                    ref={ (client) => { this.clientRef = client }}
                    onMessage={(message) => this.props.handleMessageTag(message)} />  
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
                <div className="tag-view">
                    {tagViews}
                </div>
            </div>
        );
    }
}


export default withRouter(Tag);