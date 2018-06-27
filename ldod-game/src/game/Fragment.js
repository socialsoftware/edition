import React, { Component } from 'react';
import {
    withRouter
} from 'react-router-dom';
import { Card } from 'antd';
import { Panel } from 'react-bootstrap';
import VirtualEdition from './VirtualEdition';


class Fragment extends Component {
    constructor(props) {
        super(props);
        this.state = {
            title: "",
            heteronyms: [],
            dates: [],
            hasLdoDLabel: false,
            text: ""
        };
    }

    componentDidMount() {
        this.setState({
            title: this.props.fragment.meta.title,
            heteronyms: this.props.fragment.meta.heteronyms,
            dates: this.props.fragment.meta.dates,
            hasLdoDLabel: this.props.fragment.meta.hasLdoDLabel,
            text: this.props.fragment.text
        });
    }

    render() {
        return (
            <div>
                <Panel bsStyle="primary" defaultExpanded>
                    <Panel.Heading>
                    <Panel.Title componentClass="h3" toggle>{this.state.title}</Panel.Title>
                    </Panel.Heading>
                    <Panel.Collapse>
                        <Panel.Body>{this.state.text}</Panel.Body>
                    </Panel.Collapse>               
                </Panel>
            </div>
        );
    }
}


export default withRouter(Fragment);