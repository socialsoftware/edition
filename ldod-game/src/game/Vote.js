import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import {Glyphicon, FormGroup, Checkbox, } from 'react-bootstrap';
import './Tag.css';
var  vote = " "
class Vote extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tags: [],
        };
        
    }
    
    componentDidMount(){
        vote = " ";
    }

    
    render() { 
        return (
            <form>
                <FormGroup>
                    <Checkbox inline>1</Checkbox> 
                    <Checkbox inline>2</Checkbox>{' '}
                    <Checkbox inline>3</Checkbox>
                </FormGroup>
            </form>
        );
    }
}


export default withRouter(Vote);