import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { WEB_SOCKETS_URL, APP_PREFIX, SUBSCRIBE_URL} from '../utils/Constants';
import { Tag as TagD } from 'antd';
import { Button, FormControl, FormGroup, InputGroup, Table, Grid, Row, Col} from 'react-bootstrap';
import SockJsClient from 'react-stomp'
var tags = " ";
class Tag extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tags: [],
            value: " " ,
            validate:this.props.disabled,
        };
        this.handleTag = this.handleTag.bind(this);
        this.getValidationState = this.getValidationState.bind(this);
    }
    
    componentDidMount(){
        this.setState({
            socket:   <SockJsClient
                url={WEB_SOCKETS_URL}
                topics={[ SUBSCRIBE_URL + this.props.gameId + '/tags']}
                ref={ (client) => { this.clientRef = client }}
                onMessage={(message) => this.props.handleMessageTag(message)} />
        });
        
    }

    handleTag = (e) => {
        const form = e.target;
        var input = form["tag"].value;

        if(input.length <= 1){
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
            tags: [...this.state.tags, input],
        })
        e.preventDefault();
    }

    sendMessage = (msg) => {
        try {
            this.clientRef.sendMessage(APP_PREFIX + this.props.gameId + '/tags', JSON.stringify({ gameId: this.props.gameId, authorId: this.props.userId, msg: msg, vote: 1, paragraph: this.props.index}));
            return true;

        } catch(e) {
            return false;
        }
    }
    
    getValidationState(){
        const length = this.state.value.length;
        if (length < 1){
            return "warning";
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
            tagViews.push(<TagD color="#e1b12c" key={mIndex} >{m}</TagD>)
        });
        return (
            <div> 
                {this.state.socket}
                <Grid>
                    <Row>
                        <Col md={4} mdOffset={4} xs={5}>
                        <form id="form" autoComplete="off" onSubmit={(e) => {this.handleTag(e)}}>
                        <FormGroup validationState={ this.getValidationState()}>
                            <InputGroup>
                                <FormControl 
                                    disabled={this.props.disabled}
                                    placeholder="Tag this paragraph" 
                                    id="tag"
                                    type="text"
                                    spellCheck="true"
                                    onChange={this.handleChange.bind(this)} 
                                    autoFocus />
                                </InputGroup>
                                <FormControl.Feedback />
                                <Button className="btn btn-primary" disabled={this.props.disabled} type="submit">
                                    <span className="glyphicon glyphicon-plus"></span>
                                </Button>
                        </FormGroup>
                        </form>
                        </Col>    
                    </Row>
                <Table>
                    <thead>
                        <tr>
                            <th><span className="glyphicon glyphicon-tag"></span></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <div>{tagViews}</div>			
                            </td>
                        </tr>
                    </tbody>
            	</Table>
                </Grid>
            </div>
        );
    }
}


export default withRouter(Tag);