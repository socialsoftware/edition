/* eslint-env jquery */

import React from 'react';
import axios from 'axios';
import ReactHTMLParser from 'react-html-parser';
import { FormattedMessage } from 'react-intl';
import ReactDOM from 'react-dom';
import { MetaInfo } from './MetaInfo';

export class InterEditorial extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            fragmentId: props.fragmentId,
            interId: props.interId,
            title: props.title,
            transcription: '',
            checkBoxes: [],
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

    changeDisplayOptions() {
        const selDiff = this.state.checkBoxes[0].checked;

        console.log(selDiff);

        // TODO: add get call after adding method to controller

        axios.get('http://localhost:8080/api/services/frontend/expert-writer', {
            params: {
                xmlId: this.state.fragmentId,
                urlId: this.state.interId,
                diff: selDiff,
            },
        }).then((res) => {
            const transcription = ReactHTMLParser(res.data);

            ReactDOM.render(<p>{transcription}</p>, document.getElementById('transcriptionDiv'));
        });
    }

    render() {
        if (!this.state.isLoaded) {
            return (
                <div>Loading inter info</div>
            );
        }


        const transcription = ReactHTMLParser(this.state.transcription);

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
                                    <input
                                        id="diffBox"
                                        type="checkbox"
                                        name="diff"
                                        value="Yes"
                                        ref={node => this.state.checkBoxes.push(node)}
                                        onClick={event => this.changeDisplayOptions(event)} />
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
                        <div id="transcriptionDiv" className="well" style={{ fontFamily: 'georgia', fontSize: 'medium' }}>
                            {transcription}
                        </div>
                    </div>

                    <br />
                    <MetaInfo fragId={this.state.fragmentId} interId={this.state.interId} />
                </div>
            </div>
        );
    }
}
