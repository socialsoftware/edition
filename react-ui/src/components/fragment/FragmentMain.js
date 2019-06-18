import React from 'react';
import axios from 'axios';
import { Navigation } from './Navigation';
import { InterEmpty } from './InterEmpty';
import { InterEditorial } from './InterEditorial';
import { InterAuthorial } from './InterAuthorial';
import { InterVirtual } from './InterVirtual';
import { Inter2Compare } from './Inter2Compare';
import { Virtual2Compare } from './Virtual2Compare';

export class FragmentMain extends React.Component {
    constructor(props) {
        super(props);

        this.navigationUpdate = this.navigationUpdate.bind(this);

        this.state = {
            fragmentId: props.match.params.fragId,
            fragInfo: null,
            interId: props.match.params.interId,
            compareIds: null,
            type: null,
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

    navigationUpdate(ids, type) {
        if (ids.length === 1) {
            this.setState({
                interId: ids[0],
                compareIds: null,
            });
        } else {
            this.setState({
                compareIds: ids,
                type,
            });
        }
    }

    render() {
        if (!this.state.isEditionLoaded) {
            return (
                <div>Loading fragment info....</div>
            );
        }

        let inter = <InterEmpty fragmentId={this.state.fragmentId} title={this.state.fragInfo.title} />;

        if (this.state.compareIds && this.state.compareIds.length > 1) {
            if (this.state.type === 'EXPERT') {
                inter = (
                    <Inter2Compare ids={this.state.compareIds} key={this.state.compareIds} />
                );
            } else if (this.state.type === 'VIRTUAL') {
                inter = (
                    <Virtual2Compare ids={this.state.compareIds} key={this.state.compareIds} />
                );
            }
        } else if (this.state.interId && (!this.state.compareIds || (this.state.compareIds && this.state.compareIds === 1))) {
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
                        {inter}
                        <Navigation fragId={this.state.fragmentId} interId={this.state.interId} callBack={this.navigationUpdate} />
                    </div>
                </div>
            </div>
        );
    }

}
