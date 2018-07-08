import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Panel } from 'react-bootstrap';
import { getVirtualEditionFragment } from '../utils/APIUtils';


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
            text: "",
            splitText: [],
        };
        this.loadFragment = this.loadFragment.bind(this);
        this.paragraphSplit = this.paragraphSplit.bind(this);
    }

    componentDidMount() {
        this.setState({
            title: this.props.fragment.title,
            number: this.props.fragment.number,
            urlId: this.props.fragment.urlId,
        });

        this.loadFragment(this.props.acronym, this.props.fragment.urlId);
    }

    loadFragment(acronym, urlId) {
        let request = getVirtualEditionFragment(acronym, urlId);

        request.then(response =>{
            this.setState({
                text: response.text,
            })
            this.paragraphSplit(response.text);
        });
        
    }

    paragraphSplit(text){
        /*for (var i = 0; i < this.state.fragments.length; i++) {
            console.log(this.state.fragments[i].meta.title);
            <Fragment key={this.state.fragments[i].meta.title} fragment={this.state.fragments[i]}/>    
        }*/
        var fragment = text;
        var paragraph = fragment.split("</p>");
        for (var i = 0; i < paragraph.length; i++) {
            this.setState({
                splitText: [...this.state.splitText, paragraph[i]]
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


export default withRouter(Fragment);