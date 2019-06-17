import React from 'react';
import axios from 'axios';
import { FormattedMessage } from 'react-intl';
import ReactHTMLParser from 'react-html-parser';
import ReactDOM from 'react-dom';
import { MetaInfo } from './MetaInfo';

export class Inter2Compare extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            ids: props.ids,
            checkBoxes: [],
            isLoaded: false,
        };
    }

    getCompareForIds() {
        axios.get('http://localhost:8080/api/services/frontend/multiple-writer', {
            params: {
                interIds: encodeURIComponent(this.state.ids),
            },
        }).then((result) => {
            this.setState({
                compareData: result.data,
                isLoaded: true,
            });
        });
    }

    componentDidMount() {
        this.getCompareForIds();
    }

    customRender() {
        const lineByLine = this.state.checkBoxes.length === 2 ? this.state.checkBoxes[0].checked : false;
        const showSpaces = this.state.checkBoxes.length === 2 ? this.state.checkBoxes[1].checked : this.state.checkBoxes[0].checked;

        axios.get('http://localhost:8080/api/services/frontend/multiple-writer', {
            params: {
                interIds: encodeURIComponent(this.state.ids),
                lineByLine,
                showSpaces,
            },
        }).then((result) => {
            const transcriptions = [];
            const font = result.data.showSpaces === 'true' ? 'monospace' : 'georgia';

            if (result.data[this.state.ids[0]]) {
                for (let i = 0; i < this.state.ids.length; i++) {
                    const interData = result.data[this.state.ids[i]];
                    const transcription = ReactHTMLParser(interData.transcription);

                    transcriptions.push( // TODO: add a transcription font option showspaces = monospace
                        <div id="fragmentTranscription" className="col-md-6">
                            <h4>{result.data.title}</h4>
                            <div className="well">
                                <p style={{ fontFamily: font }}>{transcription}</p>
                            </div>
                        </div>,
                    );
                }
            } else if (result.data.transcription) {
                const transcription = ReactHTMLParser(result.data.transcription);

                transcriptions.push(
                    <div id="transcription">
                        <h4>{result.data.title}
                            <a
                                id="infohighlight"
                                className="infobutton"
                                role="button"
                                data-toggle="popover"
                                data-content="THIS SHOULD BE A MSG info.highlighting">
                                <span className="glyphicon glyphicon-info-sign" /></a>
                        </h4>
                        <div className="well">
                            <p style={{ fontFamily: font }}>{transcription}</p>
                        </div>
                    </div>,
                );
            }

            ReactDOM.render(transcriptions, document.getElementById('transcriptionDiv'));
        });
    }


    render() {
        if (!this.state.isLoaded) { return <div>Loading inter compare info</div>; }

        const transcriptions = [];
        const metaInfos = [];
        const variationHeads = [];
        const variationRows = [];

        console.log(this.state.compareData);

        const font = this.state.compareData.showSpaces === 'true' ? 'monospace' : 'georgia';

        if (this.state.compareData[this.state.ids[0]]) {
            for (let i = 0; i < this.state.ids.length; i++) {
                const interData = this.state.compareData[this.state.ids[i]];
                const transcription = ReactHTMLParser(interData.transcription);

                transcriptions.push( // TODO: add a transcription font option showspaces = monospace
                    <div id="fragmentTranscription" className="col-md-6">
                        <h4>{this.state.compareData.title}</h4>
                        <div className="well">
                            <p style={{ fontFamily: font }}>{transcription}</p>
                        </div>
                    </div>,
                );

                metaInfos.push(
                    <div id="interMeta" className="col-md-6">
                        <div className="well">
                            <MetaInfo fragId={interData.fragId} interId={interData.urlId} />
                        </div>
                    </div>,
                );
            }
        } else if (this.state.compareData.transcription) {
            const transcription = ReactHTMLParser(this.state.compareData.transcription);

            transcriptions.push(
                <div id="transcription">
                    <h4>{this.state.compareData.title}
                        <a
                            id="infohighlight"
                            className="infobutton"
                            role="button"
                            data-toggle="popover"
                            data-content="THIS SHOULD BE A MSG info.highlighting">
                            <span className="glyphicon glyphicon-info-sign" /></a>
                    </h4>
                    <div className="well">
                        <p style={{ fontFamily: font }}>{transcription}</p>
                    </div>
                </div>,
           );
        }

        const variationKeys = Object.keys(this.state.compareData.variations);

        for (let i = 0; i < variationKeys.length; i++) {
            const varHead = variationKeys[i].split('#');
            variationHeads.push(<th>{varHead[0]} <br /> {varHead[1]}</th>);
        }

        for (let i = 0; i < this.state.compareData.variations[variationKeys[0]].length; i++) {
            const variationDowns = [];

            for (let j = 0; j < variationKeys.length; j++) {
                variationDowns.push(<td>{ReactHTMLParser(this.state.compareData.variations[variationKeys[j]][i])}</td>);
            }

            variationRows.push(<tr>{variationDowns}</tr>);
        }

        return (
            <div id="fragmentInter" className="row col-md-9">
                <div>
                    <form className="form-horizontal">
                        <div className="control-group">
                            <div className="controls form-inline">
                                <div
                                    id="visualisation-properties-comparison"
                                    data-toggle="checkbox">
                                    {this.state.ids.length === 2 &&
                                    <label htmlFor="lineCheck" className="checkbox" style={{ paddingTop: 0, minHeight: 0, fontWeight: 'normal' }}>
                                        <FormattedMessage id={'fragment.linebyline'} />
                                    </label> }
                                    {this.state.ids.length === 2 &&
                                    <input
                                        id="lineCheck"
                                        type="checkbox"
                                        className="btn"
                                        name="line"
                                        value="Yes"
                                        ref={node => this.state.checkBoxes.push(node)}
                                        onClick={event => this.customRender(event)} />
                                    }

                                    <label htmlFor="alignCheck" className="checkbox" style={{ paddingTop: 0, minHeight: 0, fontWeight: 'normal' }}>
                                        <FormattedMessage id={'fragment.alignspace'} />
                                    </label>
                                    <input
                                        id="alignCheck"
                                        type="checkbox"
                                        className="btn"
                                        name="spaces"
                                        value="Yes"
                                        ref={node => this.state.checkBoxes.push(node)}
                                        onClick={event => this.customRender(event)} />
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <br />
                <div
                    id="fragmentComparison"
                    className="row"
                    style={{ marginLeft: 0, marginRight: 0 }}>
                    <div id="transcriptionDiv" className="row">
                        {transcriptions}
                    </div>
                    {metaInfos.length !== 0 &&
                        <div className="row">
                            {metaInfos}
                        </div>
                    }
                </div>
                <div>
                    <h4>
                        <FormattedMessage id={'fragment.variationstable'} /> ({this.state.compareData.variations[variationKeys[0]].length})
                    </h4>
                    <table className="table table-condensed">
                        <thead>
                            <tr>
                                {variationHeads}
                            </tr>
                        </thead>
                        <tbody>
                            {variationRows}
                        </tbody>

                    </table>
                </div>
            </div>
        );
    }
}
