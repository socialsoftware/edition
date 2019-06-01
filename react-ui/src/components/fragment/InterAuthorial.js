import React from 'react';
import { FormattedMessage } from 'react-intl';
import axios from 'axios';
import ReactHTMLParser from 'react-html-parser';
import ReactDOM from 'react-dom';
import { MetaInfo } from './MetaInfo';

export class InterAuthorial extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            fragmentId: props.fragmentId,
            interId: props.interId,
            title: props.title,
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
        const selDel = this.state.checkBoxes[1].checked;
        const selIns = this.state.checkBoxes[2].checked;
        const selSubst = this.state.checkBoxes[3].checked;
        const selNotes = this.state.checkBoxes[4].checked;
        const selFacs = this.state.checkBoxes[5].checked;

        axios.get('http://localhost:8080/api/services/frontend/source-writer', {
            params: {
                xmlId: this.state.fragmentId,
                urlId: this.state.interId,
                diff: selDiff,
                del: selDel,
                ins: selIns,
                subst: selSubst,
                notes: selNotes,
                facs: selFacs,
            },
        }).then((res) => {
            const transcription = ReactHTMLParser(res.data);

            ReactDOM.render(<p>{transcription}</p>, document.getElementById('transcriptionDiv'));
        });
    }

    render() {
        // TODO : figure out a way to add titles back to divs containing the checkBoxes

        if (!this.state.isLoaded) {
            return (
                <div>Loading inter info</div>
            );
        }

        const transcription = ReactHTMLParser(this.state.transcription);

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
                                        value="Yes"
                                        ref={node => this.state.checkBoxes.push(node)}
                                        onClick={event => this.changeDisplayOptions(event)} />
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
                                        value="Yes"
                                        ref={node => this.state.checkBoxes.push(node)}
                                        onClick={event => this.changeDisplayOptions(event)} />
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
                                        defaultChecked
                                        ref={node => this.state.checkBoxes.push(node)}
                                        onClick={event => this.changeDisplayOptions(event)} />
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
                                        value="Yes"
                                        ref={node => this.state.checkBoxes.push(node)}
                                        onClick={event => this.changeDisplayOptions(event)} />
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
                                        defaultChecked
                                        ref={node => this.state.checkBoxes.push(node)}
                                        onClick={event => this.changeDisplayOptions(event)} />
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
                        </h4>
                        <br />
                        <div id="transcriptionDiv" className="well" style={{ fontFamily: 'courier' }}>
                            {transcription}
                        </div>
                    </div>

                    <br />
                    <MetaInfo fragId={this.state.fragmentId} interId={this.state.interId} />
                </div>
            </div>
        );

        // TODO : get meta-info, decide what needs to be included in it.
    }
}
