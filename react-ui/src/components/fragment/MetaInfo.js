import React from 'react';
import axios from 'axios';
import { FormattedMessage } from 'react-intl';
import { SERVER_URL } from '../../utils/Constants';

export class MetaInfo extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            metaInfo: null,
            isLoaded: false,
            fragId: props.fragId,
            interId: props.interId,
        };
    }

    getMetaInfo() {
        axios.get(`${SERVER_URL}/api/services/frontend/meta-info`, {
            params: {
                xmlId: this.state.fragId,
                urlId: this.state.interId,
            },
        }).then((res) => {
            this.setState({
                metaInfo: res.data,
                isLoaded: true,
            });
        });
    }

    componentDidMount() {
        this.getMetaInfo();
    }

    // TODO : get and do something with meta-info from backend

    render() {
        if (!this.state.isLoaded) { return <strong>Loading Meta info</strong>; }

        const metaComponents = [];

        if (this.state.metaInfo.title !== '') {
            metaComponents.push(<strong><FormattedMessage id={'general.title'} />:</strong>);
            metaComponents.push(` ${this.state.metaInfo.title}`);
            metaComponents.push(<br />);
        }

        if (this.state.metaInfo.idno) {
            metaComponents.push(<strong><FormattedMessage id={'general.identification'} />:</strong>);
            metaComponents.push(` ${this.state.metaInfo.idno}`);
            metaComponents.push(<br />);
        }

        metaComponents.push(<strong><FormattedMessage id={'general.heteronym'} />: </strong>);
        if (this.state.metaInfo.heteronym !== 'não atribuído') { // default for null heteronym
            metaComponents.push(` ${this.state.metaInfo.heteronym}`);
        } else {
            metaComponents.push(<FormattedMessage id={'general.heteronym.notassigned'} />);
        }
        metaComponents.push(<br />);

        if (this.state.metaInfo.dimension) {
            metaComponents.push(<strong><FormattedMessage id={'general.format'} />: </strong>);
            metaComponents.push(<FormattedMessage id={'general.leaf'} />);
            const dim = this.state.metaInfo.dimension.split('x');
            metaComponents.push(<small> ({dim[0]}cm X {dim[1]}cm)</small>);
            metaComponents.push(<br />);
        }

        if (this.state.metaInfo.material === 'PAPER') {
            metaComponents.push(<strong><FormattedMessage id={'general.material'} />: </strong>);
            metaComponents.push(<FormattedMessage id={'general.paper'} />);
            metaComponents.push(<br />);
        }

        if (this.state.metaInfo.columns && this.state.metaInfo.columns !== 0) {
            metaComponents.push(<strong><FormattedMessage id={'general.columns'} />: </strong>);
            metaComponents.push(` ${this.state.metaInfo.columns}`);
            metaComponents.push(<br />);
        }

        if (this.state.metaInfo.ldoDKey === true) {
            metaComponents.push(<strong>LdoD Mark: </strong>);
            metaComponents.push(<FormattedMessage id={'search.ldod.with'} />);
            metaComponents.push(<br />);
        } else if (this.state.metaInfo.ldoDKey) {
            metaComponents.push(<strong>LdoD Mark: </strong>);
            metaComponents.push(<FormattedMessage id={'search.ldod.without'} />);
            metaComponents.push(<br />);
        }

        if (this.state.metaInfo.handNotes) {
            for (let i = 0; i < this.state.metaInfo.handNotes.length; i++) {
                const note = this.state.metaInfo.handNotes[i];
                metaComponents.push(<strong><FormattedMessage id={'general.manuscript'} />: </strong>);
                metaComponents.push('(');
                metaComponents.push(<em>{Object.keys(note)[0]}</em>);
                metaComponents.push(')');
                metaComponents.push(<strong>:</strong>);
                metaComponents.push(` ${note[Object.keys(note)[0]]}`);
                metaComponents.push(<br />);
            }
        }

        if (this.state.metaInfo.typeNotes) {
            for (let i = 0; i < this.state.metaInfo.typeNotes.length; i++) {
                const note = this.state.metaInfo.typeNotes[i];
                metaComponents.push(<strong><FormattedMessage id={'general.typescript'} />: </strong>);
                metaComponents.push('(');
                metaComponents.push(<em>{Object.keys(note)[0]}</em>);
                metaComponents.push(')');
                metaComponents.push(<strong> : </strong>);
                metaComponents.push(`${note[Object.keys(note)[0]]}`);
                metaComponents.push(<br />);
            }
        }

        if (this.state.metaInfo.volume) {
            metaComponents.push(<strong><FormattedMessage id={'tableofcontents.volume'} />:</strong>);
            metaComponents.push(` ${this.state.metaInfo.volume}`);
            metaComponents.push(<br />);
        }

        if (this.state.metaInfo.journal) {
            metaComponents.push(<strong><FormattedMessage id={'general.journal'} />:</strong>);
            metaComponents.push(` ${this.state.metaInfo.journal}`);
            metaComponents.push(<br />);
        }

        if (this.state.metaInfo.number !== '') {
            metaComponents.push(<strong><FormattedMessage id={'tableofcontents.number'} />:</strong>);
            metaComponents.push(` ${this.state.metaInfo.number}`);
            metaComponents.push(<br />);
        }

        if (this.state.metaInfo.startPage !== 0) {
            metaComponents.push(<strong><FormattedMessage id={'tableofcontents.page'} />:</strong>);
            metaComponents.push(` ${this.state.metaInfo.startPage} `);
            if (this.state.metaInfo.endPage && this.state.metaInfo.startPage !== this.state.metaInfo.endPage) { metaComponents.push(`- ${this.state.metaInfo.endPage}`); }
            metaComponents.push(<br />);
        }

        if (this.state.metaInfo.pubPlace) {
            metaComponents.push(<strong><FormattedMessage id={'general.published.place'} />:</strong>);
            metaComponents.push(` ${this.state.metaInfo.pubPlace}`);
            metaComponents.push(<br />);
        }

        if (this.state.metaInfo.date !== '') {
            metaComponents.push(<strong><FormattedMessage id={'general.date'} />:</strong>);
            metaComponents.push(` ${this.state.metaInfo.date} `);
            if (this.state.metaInfo.datePrecision) {
                metaComponents.push('(');
                metaComponents.push(<em>{this.state.metaInfo.datePrecision}</em>);
                metaComponents.push(')');
            }
            metaComponents.push(<br />);
        }

        if (this.state.metaInfo.note !== '') {
            metaComponents.push(<strong><FormattedMessage id={'general.note'} />:</strong>);
            metaComponents.push(` ${this.state.metaInfo.notes}`);
            metaComponents.push(<br />);
        }

        if (this.state.metaInfo.annexNotes) {
            for (let i = 0; i < this.state.metaInfo.annexNotes.length; i++) {
                const note = this.state.metaInfo.annexNotes[i];
                metaComponents.push(<strong><FormattedMessage id={'general.note'} />:</strong>);
                metaComponents.push(` ${note}`);
                metaComponents.push(<br />);
            }
        }

        if (this.state.metaInfo.surfaces) {
            metaComponents.push(<strong><FormattedMessage id={'general.facsimiles'} />:</strong>);
            for (let i = 0; i < this.state.metaInfo.surfaces.length; i++) {
                const surface = this.state.metaInfo.surfaces[i];
                metaComponents.push(<a href={Object.keys(surface)[0]}>{` ${surface[Object.keys(surface)[0]]}.${i + 1}`}</a>);
            }
            metaComponents.push(<br />);
        }


        // TODO: add the rest of the meta info.

        return <div className="well">{metaComponents}</div>;
    }
}
