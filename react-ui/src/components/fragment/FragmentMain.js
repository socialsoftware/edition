import React from 'react';
import axios from 'axios';
import { Navigation } from './Navigation';
import { InterEmpty } from './InterEmpty';
import { InterEditorial } from './InterEditorial';
import { InterAuthorial } from './InterAuthorial';
import { InterVirtual } from './InterVirtual';

export class FragmentMain extends React.Component {
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

        if (this.state.interId) {
            if (this.state.interId.includes('CRIT')) {
                inter = (
                    <InterEditorial
                        fragmentId={this.state.fragmentId}
                        title={this.state.fragInfo.title}
                        interId={this.state.interId} />
                );
            } else if (this.state.interId.includes('VIRT')) {
                inter = (
                    <InterVirtual
                        fragmentId={this.state.fragmentId}
                        title={this.state.fragInfo.title}
                        interId={this.state.interId} />
                );
            } else {
                inter = (
                    <InterAuthorial
                        fragmentId={this.state.fragmentId}
                        title={this.state.fragInfo.title}
                        interId={this.state.interId} />
                );
            }
        }


        return (
            <div id="fragmentBody">
                <div className="container">
                    <div className="row" style={{ marginLeft: 0, marginRight: 0 }}>
                        <div id="interDiv">
                            {inter}
                        </div>
                        <Navigation fragId={this.state.fragmentId} interId={this.state.interId} />
                    </div>
                </div>
            </div>
        );
    }

}
