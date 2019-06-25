import React from 'react';
import axios from 'axios';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';
import { InterEmpty } from './InterEmpty';
import { InterEditorial } from './InterEditorial';
import { InterAuthorial } from './InterAuthorial';
import { InterVirtual } from './InterVirtual';
import { Inter2Compare } from './Inter2Compare';
import { Virtual2Compare } from './Virtual2Compare';
import Navigation from './Navigation';

const mapStateToProps = state => ({ ids: state.compareIds, type: state.type, interId: state.interId, config: Object.keys(state.moduleConfig) });

class FragmentMain extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            fragmentId: props.match.params.fragId,
            fragInfo: null,
            interId: props.match.params.interId,
            isLoaded: false,
        };
    }

    getFragmentInfo() {
        axios.get('http://localhost:8080/api/services/frontend/frag-info', {
            params: {
                xmlId: this.state.fragmentId,
            },
        }).then((result) => {
            this.setState({
                fragInfo: result.data,
                isEditionLoaded: true,
            });
        });
    }

    componentDidMount() {
        this.getFragmentInfo();
    }

    render() {
        if (!this.state.isEditionLoaded) {
            return (
                <div>Loading fragment info....</div>
            );
        }

        let inter = <InterEmpty fragmentId={this.state.fragmentId} title={this.state.fragInfo.title} />;

        const interId = this.props.interId != null ? this.props.interId : this.state.interId;

        if (this.props.ids && this.props.ids.length > 1) {
            if (this.props.type === 'EXPERT') {
                inter = (
                    <Inter2Compare ids={this.props.ids} key={this.props.ids} />
                );
            } else if (this.props.type === 'VIRTUAL') {
                inter = (
                    <Virtual2Compare ids={this.props.ids} key={this.props.ids} />
                );
            }
        } else if (interId && (!this.props.ids || (this.props.ids && this.props.ids.length === 1))) {
            if (interId.includes('CRIT')) {
                inter = (
                    <InterEditorial
                        fragmentId={this.state.fragmentId}
                        title={this.state.fragInfo.title}
                        interId={interId} />
                );
            } else if (interId.includes('VIRT')) {
                if (!this.props.config.includes('edition-virtual')) {
                    return (
                        <Redirect to="/error" />
                    );
                }

                inter = (
                    <InterVirtual
                        fragmentId={this.state.fragmentId}
                        title={this.state.fragInfo.title}
                        interId={interId} />
                );
            } else {
                inter = (
                    <InterAuthorial
                        fragmentId={this.state.fragmentId}
                        title={this.state.fragInfo.title}
                        interId={interId} />
                );
            }
        }


        return (
            <div id="fragmentBody">
                <div className="container">
                    <div className="row" style={{ marginLeft: 0, marginRight: 0 }}>
                        {inter}
                        <Navigation fragId={this.state.fragmentId} interId={interId} />
                    </div>
                </div>
            </div>
        );
    }
}

export default connect(mapStateToProps)(FragmentMain);
