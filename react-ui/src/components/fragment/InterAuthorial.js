import React from 'react';
import { FormattedMessage } from 'react-intl';
import axios from 'axios';
import ReactHTMLParser from 'react-html-parser';
import ReactDOM from 'react-dom';
import OpenSeadragon from 'openseadragon';
import { MetaInfo } from './MetaInfo';

export class InterAuthorial extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            fragmentId: props.fragmentId,
            interId: props.interId,
            title: props.title,
            checkBoxes: [],
            showFac: false,
            facUrls: null,
            transcriptionClass: 'well',
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

    componentDidUpdate() {
        if (document.getElementById('fac') != null) {
            const sources = [];

            for (let i = 0; i < this.state.facUrls.length; i++) {
                const url = this.state.facUrls[i];
                sources.push({ type: 'image', url: `/facs/${url}` });
            }

            // TODO: remove these temp images before finishing
            sources.push({ type: 'image', url: '/resources/img/openseadragon/images/next_rest.png' });
            sources.push({ type: 'image', url: '/resources/img/openseadragon/images/previous_rest.png' });

            /*eslint-disable */
            const viewer = OpenSeadragon({
                id: 'fac',
                prefixUrl: '/resources/img/openseadragon/images/',
                autoHideControls: false,
                visibilityRatio: 1.0,
                constrainDuringPan: true,
                showNavigator: true,
                sequenceMode: true,
                tileSources: sources,
            });
            /*eslint-enable */
        }
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
            },
        }).then((res) => {
            const transcription = ReactHTMLParser(res.data);

            ReactDOM.render(<p>{transcription}</p>, document.getElementById('transcriptionDiv'));
        });

        if (selFacs) {
            axios.get('http://localhost:8080/api/services/frontend/fac-urls', {
                params: {
                    xmlId: this.state.fragmentId,
                    urlId: this.state.interId,
                    // TODO : where do the pbIds come from????
                },
            }).then((res) => {
                this.setState({
                    facUrls: res.data,
                    transcriptionClass: 'well col-md-6',
                    showFac: true,
                });
            });
        }

        if (!selFacs && document.getElementById('fac') != null) {
            this.setState({
                transcriptionClass: 'well',
                showFac: false,
            });
        }
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
            <div className="col-md-9">
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

                        <div id="facsimileTranscription" className="row" style={{ marginRight: 0 }}>

                            {this.state.showFac &&
                            <div className="col-md-6">
                                <div className="item" id="fac" style={{ width: 100, height: 554 }} />
                            </div>
                        }

                            <div id="transcriptionDiv" className={this.state.transcriptionClass} style={{ fontFamily: 'courier' }}>
                                {transcription}
                            </div>

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
