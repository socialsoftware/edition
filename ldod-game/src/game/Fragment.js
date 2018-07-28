import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { getVirtualEditionFragment } from '../utils/APIUtils';
import Paragraph from './Paragraph';

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
            index: 1,
            view: false,
        };
        this.loadFragment = this.loadFragment.bind(this);
        this.paragraphSplit = this.paragraphSplit.bind(this);
        this.nextParagraph = this.nextParagraph.bind(this);
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
        var paragraph = text.split("</p>");
        for (var i = 0; i < paragraph.length; i++) {
            this.setState({
                splitText: [...this.state.splitText, paragraph[i]]
            });        
        }
    }

    nextParagraph(){
        this.setState((prevState, props) => ({
            index: prevState.index + 1,
            view:true,
        })); 
    }

    render() {
        return (
            <div>
                <Paragraph text={this.state.splitText[this.state.index]} title={this.state.title} />  
            </div>
        );
    }
}


export default withRouter(Fragment);