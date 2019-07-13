import React from 'react';
import axios from 'axios';
import { FormattedMessage } from 'react-intl';
import { connect } from 'react-redux';
import { setCompareIdsType, setInterId } from '../../actions/actions';
import { SERVER_URL } from '../../utils/Constants';

const mapStateToProps = state => ({ config: state.moduleConfig });

class Navigation extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            fragId: props.fragId,
            interId: props.interId,
            sourceCheckBoxes: [],
            expertCheckBoxes: [],
            virtualCheckBoxes: [],
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

    getFragNavigationInfo() {
        axios.get(`${SERVER_URL}/api/services/frontend/expert-edition`)
            .then((result) => {
                this.setState({
                    expertEditions: result.data,
                    isEditionLoaded: true,
                });
            });
        axios.get(`${SERVER_URL}/api/services/frontend/expert-inter`, {
            params: {
                xmlId: this.state.fragId,
            },
        }).then((result) => {
            this.setState({
                expertInterInfo: result.data,
                isExpertLoaded: true,
            });
        });

        axios.get(`${SERVER_URL}/api/services/frontend/source-inter`, {
            params: {
                xmlId: this.state.fragId,
            },
        }).then((result) => {
            this.setState({
                sourceInterInfo: result.data,
                isSourceLoaded: true,
            });
        });

        if (this.props.config.includes('edition-virtual')) {
            axios.get(`${SERVER_URL}/api/services/frontend/virtual-inter`, {
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
    }

    selectedScholarInter() {
        const selectedInters = [];

        this.state.expertCheckBoxes = this.state.expertCheckBoxes.filter(e => e != null);
        this.state.sourceCheckBoxes = this.state.sourceCheckBoxes.filter(e => e != null);

        let urlId = null;

        for (let i = 0; i < this.state.sourceCheckBoxes.length; i++) {
            if (this.state.sourceCheckBoxes[i].checked) {
                selectedInters.push(this.state.sourceCheckBoxes[i].value);
                urlId = this.state.sourceCheckBoxes[i].name;
            }
        }

        for (let i = 0; i < this.state.expertCheckBoxes.length; i++) {
            if (this.state.expertCheckBoxes[i].checked) {
                selectedInters.push(this.state.expertCheckBoxes[i].value);
                urlId = this.state.expertCheckBoxes[i].name;
            }
        }

        this.state.virtualCheckBoxes = this.state.virtualCheckBoxes.filter(e => e != null);

        this.state.virtualCheckBoxes.forEach(ele => ele.checked = false);

        if (selectedInters.length === 1) {
            this.props.setInterId(urlId);
        } else { this.props.setCompareIdsType(selectedInters, 'EXPERT'); }
    }

    selectedVirtualInter() {
        const selectedInters = [];
        this.state.virtualCheckBoxes = this.state.virtualCheckBoxes.filter(e => e != null);

        let urlId = null;

        for (let i = 0; i < this.state.virtualCheckBoxes.length; i++) {
            if (this.state.virtualCheckBoxes[i].checked) {
                selectedInters.push(this.state.virtualCheckBoxes[i].value);
                urlId = this.state.virtualCheckBoxes[i].name;
            }
        }
        this.state.expertCheckBoxes = this.state.expertCheckBoxes.filter(e => e != null);
        this.state.sourceCheckBoxes = this.state.sourceCheckBoxes.filter(e => e != null);


        this.state.expertCheckBoxes.forEach(ele => ele.checked = false);
        this.state.sourceCheckBoxes.forEach(ele => ele.checked = false);

        if (selectedInters.length === 1) {
            this.props.setInterId(urlId);
        } else { this.props.setCompareIdsType(selectedInters, 'VIRTUAL'); }
    }

    componentDidMount() {
        this.getFragNavigationInfo();
    }

    render() {
        if (!this.state.isEditionLoaded || !this.state.isExpertLoaded || !this.state.isSourceLoaded
            || !(this.state.isVirtualLoaded || !this.props.config.includes('edition-virtual'))) {
            return <div>Loading Navigation Info</div>;
        }

        this.state.expertCheckBoxes = [];
        this.state.sourceCheckBoxes = [];
        this.state.virtualCheckBoxes = [];

        // Build source inter options
        const sourceRow = [];
        for (let i = 0; i < this.state.sourceInterInfo.length; i++) {
            const sourceInfo = this.state.sourceInterInfo[i];

            const ref = `http://localhost:9000/fragments/fragment/${this.state.fragId}/inter/${sourceInfo.urlId}`;

            const checkBox = this.state.interId === sourceInfo.urlId ? (
                <input
                    type="checkbox"
                    name={sourceInfo.urlId}
                    value={sourceInfo.externalId}
                    ref={node => this.state.sourceCheckBoxes.push(node)}
                    onClick={event => this.selectedScholarInter(event)}
                    defaultChecked />
            ) : (
                <input
                    type="checkbox"
                    name={sourceInfo.urlId}
                    value={sourceInfo.externalId}
                    ref={node => this.state.sourceCheckBoxes.push(node)}
                    onClick={event => this.selectedScholarInter(event)} />
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
                        name={interData.urlId}
                        value={interData.externalId}
                        ref={node => this.state.expertCheckBoxes.push(node)}
                        onClick={event => this.selectedScholarInter(event)}
                        defaultChecked />
                ) : (
                    <input
                        type="checkbox"
                        name={interData.urlId}
                        value={interData.externalId}
                        ref={node => this.state.expertCheckBoxes.push(node)}
                        onClick={event => this.selectedScholarInter(event)} />
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

        if (this.props.config.includes('edition-virtual')) {
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
                            name={interData.urlId}
                            value={interData.externalId}
                            ref={node => this.state.virtualCheckBoxes.push(node)}
                            onClick={event => this.selectedVirtualInter(event)}
                            defaultChecked />
                    ) : (
                        <input
                            type="checkbox"
                            name={interData.urlId}
                            value={interData.externalId}
                            ref={node => this.state.virtualCheckBoxes.push(node)}
                            onClick={event => this.selectedVirtualInter(event)} />
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
        }

        return (
            <div className="col-md-3">
                <div id="fragment" className="row">
                    <div id={this.state.fragId} />
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

                {this.props.config.includes('edition-virtual') &&
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
                }
            </div>
        );
    }
}

export default connect(mapStateToProps, { setCompareIdsType, setInterId })(Navigation);
