import React from 'react';
import axios from 'axios';
import { FormattedMessage } from 'react-intl';
import ReactHTMLParser from 'react-html-parser';
import { Taxonomy } from './Taxnonomy';

export class InterVirtual extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            fragmentId: props.fragmentId,
            interId: props.interId,
            title: props.title,
            editionInfo: null,
            transcription: null,
            isTransLoaded: false,
            isVirtualLoaded: false,
        };
    }

    getInterTranscription() {
        axios.get('http://localhost:8080/api/services/frontend/inter-writer', {
            params: {
                xmlId: this.state.fragmentId,
                urlId: this.state.interId,
            },
        }).then((result) => {
            this.setState({
                transcription: result.data,
                isLoaded: true,
            });
        });
    }

    getUsesInfo() {
        axios.get('http://localhost:8080/api/services/frontend/virtual-edition', {
            params: {
                xmlId: this.state.fragmentId,
                urlId: this.state.interId,
            },
        }).then((result) => {
            this.setState({
                editionInfo: result.data,
                isVirtualLoaded: true,
            });
        });
    }

    componentDidMount() {
        this.getInterTranscription();
        this.getUsesInfo();
    }

    render() {
        if (!this.state.isLoaded || !this.state.isVirtualLoaded) {
            return (
                <div>Loading inter info</div>
            );
        }

        const transcription = ReactHTMLParser(this.state.transcription);

        return (
            <div className="col-md-9">
                <link
                    href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/css/select2.min.css"
                    rel="stylesheet" />
                <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/js/select2.min.js" />
                <div
                    id="fragmentInter"
                    className="row"
                    style={{ marginLeft: 0, marginRight: 0 }}>
                    <h4>{this.state.editionInfo.editionTitle}
                         -
                        <FormattedMessage id={'general.uses'} />
                        {this.state.editionInfo.editionReference}({this.state.editionInfo.interReference})
                    </h4>

                    <div className="row" id="content">
                        <h4 className="text-center">
                            {this.state.title}
                        </h4>
                        <br />
                        <div id="transcriptionDiv" className="well" style={{ fontFamily: 'courier' }}>
                            {transcription}
                        </div>
                    </div>
                    <Taxonomy fragmentId={this.state.fragmentId} interId={this.state.interId} />
                </div>
            </div>
        );
    }
}
