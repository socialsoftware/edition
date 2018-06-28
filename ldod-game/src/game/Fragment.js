import React, { Component } from 'react';
import {
    withRouter
} from 'react-router-dom';
import { Card } from 'antd';
import { Panel } from 'react-bootstrap';
import { getVirtualEditionFragment } from '../utils/APIUtils';
import VirtualEdition from './VirtualEdition';
import { Button, Glyphicon } from 'react-bootstrap';


class Fragment extends Component {
    constructor(props) {
        super(props);
        this.state = {
            title: "",
            heteronyms: [],
            dates: [],
            hasLdoDLabel: false,
            number: 0,
            urlId: "",
            text: ""
        };
        this.loadFragment = this.loadFragment.bind(this);
    }

    componentDidMount() {
        this.setState({
            title: this.props.fragment.title,
            number: this.props.fragment.number,
            urlId: this.props.fragment.urlId,
        });
    }

    loadFragment() {
        let request = getVirtualEditionFragment("LdoD-ok", this.state.urlId);

        request.then(response =>{
            this.setState({
                text: response.text,
            })
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
                        <Button bsStyle="primary" onClick={this.loadFragment}>
                            <Glyphicon glyph="ok"/> 
                        </Button>
                        <Panel.Body>
                            <div dangerouslySetInnerHTML={{__html: this.state.text}} >
                            </div>
                        </Panel.Body>
                    </Panel.Collapse>               
                </Panel>
            </div>
        );
    }
}


export default withRouter(Fragment);