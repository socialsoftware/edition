import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import {Glyphicon, Button, FormControl, FormGroup, InputGroup, HelpBlock} from 'react-bootstrap';
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
            value: " " ,
            validate: null,
        };
        this.handleTag = this.handleTag.bind(this);
        this.getValidationState = this.getValidationState.bind(this);
    }
    
    handleTag = (e) => {
        const form = e.target;
        var input = form["tag"].value;
        var alphaExp = /^[a-zA-Z]+$/;

        if(input.length <= 1 || this.state.tags.indexOf(input) !== -1 || !input.match(alphaExp)){
            this.setState({
                validate: "error",
            })
            e.preventDefault();
            return;
        }

        this.sendMessage(input);
        tags += "<br>" + input;
        form.reset();
        this.setState({
            validate: "success",
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
    
    getValidationState(){
        const length = this.state.value.length;
        if (length > 1){
            return 'success';
        }
        return null;
    }

    handleChange(event) {
        const value = event.target.value;
        
        this.setState({
          value: value
        });
    }

    render() {
        const tagViews = [];
        let messages = this.state.tags;
        messages.forEach((m, mIndex) => {
            tagViews.push(<TagD className="tag" color="#747d8c" key={mIndex} >{m}</TagD>)
        });
        return (
            <div> 
            <SockJsClient
                    url={WEB_SOCKETS_URL}
                    topics={['/topic/tags']}
                    ref={ (client) => { this.clientRef = client }}
                    onMessage={(message) => this.props.handleMessageTag(message)} />  
                <form id="form" autoComplete="off" onSubmit={(e) => {this.handleTag(e)}}>
                    <FormGroup validationState={ this.state.validate === "error" ? this.state.validate : this.getValidationState()}>
                        <InputGroup>
                            <InputGroup.Addon><Glyphicon glyph="plus" /></InputGroup.Addon>
                            <FormControl 
                                placeholder="Tag this paragraph" 
                                id="tag"
                                type="text"
                                onChange={this.handleChange.bind(this)} 
                                autoFocus />
                            </InputGroup>
                            <FormControl.Feedback />
                        <Button disabled={this.getValidationState() === 'success' ? false : true} type="submit">Submit</Button>
                    </FormGroup>
                </form>
                <span className="icon-tags"><Glyphicon glyph="tags" /></span>
                <div className="tag-view">
                    {tagViews}
                </div>
            </div>
        );
    }
}


export default withRouter(Tag);