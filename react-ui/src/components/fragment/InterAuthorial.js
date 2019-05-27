import React from 'react';
import { FormattedMessage } from 'react-intl';
import axios from 'axios';
import ReactHTMLParser from 'react-html-parser';

export class InterAuthorial extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            fragmentId: props.fragmentId,
            interId: props.interId,
            title: props.title,
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
        // TODO : figure out a way to add titles back to divs containing the checkboxes

        if (!this.state.isLoaded) {
            return (
                <div>Loading inter info</div>
            );
        }

        console.log(ReactHTMLParser(this.state.transcription));


        const transcription = ReactHTMLParser(this.state.transcription)[1];

        return (
            <div>
                <script type="text/javascript" src="../scripts/AuthorialScript.js" />
                <div id="fragmentInter" className="row">
                    <form className="form-inline">
                        <div className="form-group">
                            <div
                                id="visualisation-properties-authorial"
                                className="btn-group"
                                data-toggle="checkbox">
                                <div className="checkbox tip">
                                    <label htmlFor="diffCheck">
                                        <FormattedMessage id={'fragment.highlightdifferences'} />
                                    </label>
                                    <input
                                        id="diffCheck"
                                        type="checkbox"
                                        className="btn"
                                        name="diff"
                                        value="Yes" />
                                </div>
                                <div className="checkbox tip">
                                    <label htmlFor="deletedCheck">
                                        <FormattedMessage id={'fragment.showdeleted'} />
                                    </label>
                                    <input
                                        id="deletedCheck"
                                        type="checkbox"
                                        className="btn"
                                        name="del"
                                        value="Yes" />
                                </div>
                                <div className="checkbox tip">
                                    <label htmlFor="insertedCheck">
                                        <FormattedMessage id={'fragment.highlightinserted'} />
                                    </label>
                                    <input
                                        id="insertedCheck"
                                        type="checkbox"
                                        className="btn"
                                        name="ins"
                                        value="Yes"
                                        defaultChecked />
                                </div>
                                <div className="checkbox tip">
                                    <label htmlFor="subCheck">
                                        <FormattedMessage id={'fragment.highlightsubstitutions'} />
                                    </label>
                                    <input
                                        id="subCheck"
                                        type="checkbox"
                                        className="btn"
                                        name="subst"
                                        value="Yes" />
                                </div>
                                <div className="checkbox tip">
                                    <label htmlFor="noteCheck">
                                        <FormattedMessage id={'fragment.shownotes'} />
                                    </label>
                                    <input
                                        id="noteCheck"
                                        type="checkbox"
                                        className="btn"
                                        name="notes"
                                        value="Yes"
                                        defaultChecked />
                                </div>
                                <div className="checkbox tip">
                                    <label htmlFor="facsCheck">
                                        <FormattedMessage id={'fragment.showfacs'} />
                                    </label>
                                    <input
                                        id="facsCheck"
                                        type="checkbox"
                                        className="btn"
                                        name="facs"
                                        value="Yes" />
                                </div>
                            </div>
                        </div>
                    </form>

                    <br />
                    <div id="fragmentTranscription">
                        <h4 className="text-center">
                            {this.state.title}
                        </h4>
                        <br />
                        <div className="well" style={{ fontFamily: 'courier' }}>
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
