/* eslint-env jquery */

import React from 'react';
import axios from 'axios';
import ReactHTMLParser from 'react-html-parser';
import { FormattedMessage } from 'react-intl';

export class InterEditorial extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            fragmentId: props.fragmentId,
            interId: props.interId,
            title: props.title,
            transcription: '',
            isLoaded: false,
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

    componentDidMount() {
        this.getInterTranscription();
    }

    render() {
        if (!this.state.isLoaded) {
            return (
                <div>Loading inter info</div>
            );
        }

        console.log(ReactHTMLParser(this.state.transcription));

        const transcription = ReactHTMLParser(this.state.transcription)[1];

        return (
            <div>
                <script type="text/javascript" src="../scripts/EditorialScript.js" />
                <div id="fragmentInter" className="row">
                    <form className="form-inline">
                        <div className="form-group">
                            <div
                                id="visualisation-properties-editorial"
                                className="btn-group"
                                data-toggle="checkbox">
                                <div className="checkbox">
                                    <label htmlFor="diffBox">
                                        <FormattedMessage id={'fragment.highlightdifferences'} />
                                    </label>
                                    <input id="diffBox" type="checkbox" name="diff" value="Yes" />
                                </div>
                            </div>
                        </div>
                    </form>
                    <br />
                    <div id="fragmentTranscription">
                        <h4 className="text-center">
                            {this.state.title}
                            <a
                                href=""><span className="glyphicon glyphicon-eye-open" /></a>
                        </h4>
                        <br />
                        <div className="well" style={{ fontFamily: 'georgia', fontSize: 'medium' }}>
                            <p>{transcription}</p>
                        </div>
                    </div>

                    <br />
                    <div className="well">
                             META INFO GOES HERE
                    </div>
                </div>
            </div>
        );
    }
}
