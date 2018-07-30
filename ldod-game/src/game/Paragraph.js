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
            <div className="panel">
                <Panel bsStyle="primary" defaultExpanded>
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