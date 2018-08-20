import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Panel } from 'react-bootstrap';
import './Paragraph.css';

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
        <Panel bsStyle="primary" defaultExpanded>
            <Panel.Heading>
                <Panel.Title className="panel-title" componentClass="h4" toggle>{this.state.title}</Panel.Title>
            </Panel.Heading>
            <Panel.Body >
                <div dangerouslySetInnerHTML={{__html: this.state.text}}></div>
            </Panel.Body>
        </Panel>
        );
    }
}


export default withRouter(Paragrah);