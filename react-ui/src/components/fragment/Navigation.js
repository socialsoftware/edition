import React from 'react';
import axios from 'axios';
import { FormattedMessage } from 'react-intl';

export class Navigation extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            fragId: props.fragId,
            interId: props.interId,
            checkBoxes: [],
            expertEditions: null,
            expertInterInfo: null,
            sourceInterInfo: null,
            virtualInterInfo: null,
            isEditionLoaded: false,
            isExpertLoaded: false,
            isSourceLoaded: false,
            isVirtualLoaded: false,
        };
    }

    getFragExpertSourceInfo() {
        axios.get('http://localhost:8080/api/services/frontend/expert-edition')
            .then((result) => {
                this.setState({
                    expertEditions: result.data,
                    isEditionLoaded: true,
                });
            });
        axios.get('http://localhost:8080/api/services/frontend/expert-inter', {
            params: {
                xmlId: this.state.fragId,
            },
        }).then((result) => {
            this.setState({
                expertInterInfo: result.data,
                isExpertLoaded: true,
            });
        });

        axios.get('http://localhost:8080/api/services/frontend/source-inter', {
            params: {
                xmlId: this.state.fragId,
            },
        }).then((result) => {
            this.setState({
                sourceInterInfo: result.data,
                isSourceLoaded: true,
            });
        });

        axios.get('http://localhost:8080/api/services/frontend/virtual-inter', {
            params: {
                xmlId: this.state.fragId,
            },
        }).then((result) => {
            this.setState({
                virtualInterInfo: result.data,
                isVirtualLoaded: true,
            });
        });
    }

    selectedInter(event) {
        const selectedInters = [];

        console.log(event);

        for (let i = 0; i < this.state.checkBoxes.length; i++) {
            if (this.state.checkBoxes[i].checked) {
                selectedInters.push(this.state.checkBoxes[i].value);
            }
        }

        console.log(this.state.checkBoxes);

        console.log(selectedInters);

        console.log(document.getElementById('interDiv'));
    }

    componentDidMount() {
        this.getFragExpertSourceInfo();
    }

    render() {
        if (!this.state.isEditionLoaded || !this.state.isExpertLoaded || !this.state.isSourceLoaded || !this.state.isVirtualLoaded) {
            return <div>Loading Edition Info</div>;
        }

        // Build source inter options
        const sourceRow = [];
        for (let i = 0; i < this.state.sourceInterInfo.length; i++) {
            const sourceInfo = this.state.sourceInterInfo[i];

            const ref = `http://localhost:9000/fragments/fragment/${this.state.fragId}/inter/${sourceInfo.urlId}`;

            const checkBox = this.state.interId === sourceInfo.urlId ? (
                <input
                    type="checkbox"
                    name={sourceInfo.externalId}
                    value={sourceInfo.externalId}
                    ref={node => this.state.checkBoxes.push(node)}
                    onClick={event => this.selectedInter(event)}
                    defaultChecked />
            ) : (
                <input
                    type="checkbox"
                    name={sourceInfo.externalId}
                    value={sourceInfo.externalId}
                    ref={node => this.state.checkBoxes.push(node)}
                    onClick={event => this.selectedInter(event)} />
            );

            sourceRow.push(
                (
                    <tr>
                        <td />
                        <td>
                            {checkBox}
                        </td>
                        <td><a
                            href={ref}>{sourceInfo.shortName}</a>
                        </td>
                        <td />
                    </tr>
                ),
            );
        }

        // Build expert inter options
        const expertRow = [];
        for (let i = 0; i < this.state.expertEditions.length; i++) {
            const info = this.state.expertEditions[i];
            const interInfo = this.state.expertInterInfo[Object.keys(info)[0]];

            if (!interInfo) { continue; }

            const navOptions = [];

            for (let j = 0; j < interInfo.length; j++) {
                const interData = interInfo[j];

                const ref = `http://localhost:9000/fragments/fragment/${this.state.fragId}/inter/${interData.urlId}`;
                const nextRef = `http://localhost:9000/fragments/fragment/${interData.nextXmlId}/inter/${interData.nextUrlId}`;
                const prevRef = `http://localhost:9000/fragments/fragment/${interData.prevXmlId}/inter/${interData.prevUrlId}`;

                const checkBox = this.state.interId === interData.urlId ? (
                    <input
                        type="checkbox"
                        name={interData.externalId}
                        value={interData.externalId}
                        ref={node => this.state.checkBoxes.push(node)}
                        onClick={event => this.selectedInter(event)}
                        defaultChecked />
                ) : (
                    <input
                        type="checkbox"
                        name={interData.externalId}
                        value={interData.externalId}
                        ref={node => this.state.checkBoxes.push(node)}
                        onClick={event => this.selectedInter(event)} />
                );

                navOptions.push(
                    (
                        <tr>
                            <td />
                            <td>
                                {checkBox}
                            </td>

                            <td><a
                                href={prevRef}><span
                                    className="glyphicon glyphicon-chevron-left" /></a></td>
                            <td><a
                                href={ref}>{interData.number}</a>
                            </td>
                            <td><a
                                href={nextRef}><span
                                    className="glyphicon glyphicon-chevron-right" /></a></td>
                            <td />
                        </tr>
                    ),
                );
            }

            expertRow.push(
                (
                    <div className="text-center">
                        <table width="100%">
                            <caption className="text-center">
                                <a
                                    href="">
                                    {info[Object.keys(info)[0]]}
                                </a>
                            </caption>
                            <thead>
                                <tr>
                                    <th style={{ width: '10%' }} />
                                    <th style={{ width: '10%' }} />
                                    <th style={{ width: '25%' }} />
                                    <th style={{ width: '10%' }} />
                                    <th style={{ width: '25%' }} />
                                    <th style={{ width: '20%' }} />
                                </tr>
                            </thead>
                            <tbody>
                                {navOptions}
                            </tbody>
                        </table>
                    </div>
                ),
            );
        }

        // Build virtual inter options.

        const virtualRow = [];

        const editionNames = Object.keys(this.state.virtualInterInfo);

        for (let i = 0; i < editionNames.length; i++) {
            const interInfo = this.state.virtualInterInfo[editionNames[i]];

            const navOptions = [];

            for (let j = 0; j < interInfo.length; j++) {
                const interData = interInfo[j];

                const ref = `http://localhost:9000/fragments/fragment/${this.state.fragId}/inter/${interData.urlId}`;
                const nextRef = `http://localhost:9000/fragments/fragment/${interData.nextXmlId}/inter/${interData.nextUrlId}`;
                const prevRef = `http://localhost:9000/fragments/fragment/${interData.prevXmlId}/inter/${interData.prevUrlId}`;

                const checkBox = this.state.interId === interData.urlId ? (
                    <input
                        type="checkbox"
                        name={interData.externalId}
                        value={interData.externalId}
                        ref={node => this.state.checkBoxes.push(node)}
                        onClick={event => this.selectedInter(event)}
                        defaultChecked />
                ) : (
                    <input
                        type="checkbox"
                        name={interData.externalId}
                        value={interData.externalId}
                        ref={node => this.state.checkBoxes.push(node)}
                        onClick={event => this.selectedInter(event)} />
                );

                navOptions.push(
                    (
                        <tr>
                            <td />
                            <td>
                                {checkBox}
                            </td>

                            <td><a
                                href={prevRef}><span
                                    className="glyphicon glyphicon-chevron-left" /></a></td>
                            <td><a
                                href={ref}>{interData.number}</a>
                            </td>
                            <td><a
                                href={nextRef}><span
                                    className="glyphicon glyphicon-chevron-right" /></a></td>
                            <td />
                        </tr>
                    ),
                );
            }

            virtualRow.push(
                (
                    <div className="text-center">
                        <table width="100%">
                            <caption className="text-center">
                                <a
                                    href="">
                                    {editionNames[i]}
                                </a>
                            </caption>
                            <thead>
                                <tr>
                                    <th style={{ width: '10%' }} />
                                    <th style={{ width: '10%' }} />
                                    <th style={{ width: '25%' }} />
                                    <th style={{ width: '10%' }} />
                                    <th style={{ width: '25%' }} />
                                    <th style={{ width: '20%' }} />
                                </tr>
                            </thead>
                            <tbody>
                                {navOptions}
                            </tbody>
                        </table>
                    </div>
                ),
            );
        }

        return (
            <div className="col-md-3">
                <div id="fragment" className="row">
                    <div id="Fr001" />

                    <div
                        className="btn-group"
                        id="baseinter"
                        data-toggle="checkbox"
                        style={{ width: '100%' }}>
                        <h5 className="text-center">
                            <FormattedMessage id={'authorial.source'} />
                        </h5>
                        <div className="text-center" style={{ paddingTop: '8px' }}>
                            <table width="100%">
                                <thead>
                                    <tr>
                                        <th style={{ width: '10%' }} />
                                        <th style={{ width: '10%' }} />
                                        <th style={{ width: '60%' }} />
                                        <th style={{ width: '20%' }} />
                                    </tr>
                                </thead>
                                <tbody>
                                    {sourceRow}
                                </tbody>
                            </table>
                        </div>
                    </div><h5 className="text-center">
                        <FormattedMessage id={'edition.experts'} />
                        <a
                            id="infoexperts"
                            data-placement="bottom"
                            className="infobutton"
                            role="button"
                            data-toggle="popover"
                            data-content="TODO"> <span
                                className="glyphicon glyphicon-info-sign" /></a>
                    </h5>
                    {expertRow}
                </div>
                <br /> <br />

                <div id="virtualinter" data-toggle="checkbox">
                    <h5 className="text-center">
                        <FormattedMessage id={'virtual.editions'} />
                        <a
                            id="infovirtualeditions"
                            data-placement="bottom"
                            className="infobutton"
                            role="button"
                            data-toggle="popover"
                            data-content="TODO"> <span
                                className="glyphicon glyphicon-info-sign" />
                        </a>
                    </h5>
                    {virtualRow}
                </div>
            </div>
        );
    }
}
