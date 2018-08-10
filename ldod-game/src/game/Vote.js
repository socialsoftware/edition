import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import {FormGroup, Checkbox, } from 'react-bootstrap';
class Vote extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tags: [],
        };
        
    }
    
    componentDidMount(){
        this.setState({
            tags: this.props.tags,
        })
    }

    
    render() {
        const voteViews = [];   
        let messages = this.state.tags;
        messages.forEach((m, mIndex) => {
            voteViews.push(<Checkbox key={m.authorId + mIndex} >{m.tag}</Checkbox>)
        }); 
        return (
            <form>
                <FormGroup>
                    <div>
                        {voteViews}
                    </div>
                </FormGroup>
            </form>
        );
    }
}


export default withRouter(Vote);