import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Panel } from 'react-bootstrap';

class Paragrah extends Component {
    constructor(props) {
        super(props);
        this.state = {
            title: "",
            text: "",
        };
    }

    componentDidUpdate(prevProps, prevState) {

        if (prevProps.text !== this.props.text) {
            this.setState({
                title: this.props.title,
                text: this.props.text,
            });
        }
    }

    render() {
        return (
            <div>
                <Panel bsStyle="primary">
                    <Panel.Heading>
                        <Panel.Title componentClass="h4" toggle>{this.state.title}</Panel.Title>
                    </Panel.Heading>
                    <Panel.Collapse>
                        <Panel.Body>
                            <div dangerouslySetInnerHTML={{__html: this.state.text}}></div>
                        </Panel.Body>
                    </Panel.Collapse>
                </Panel>
            </div>
        );
    }
}


export default withRouter(Paragrah);