import React from 'react';
import axios from 'axios';
import { Navigation } from './Navigation';
import { InterEmpty } from './InterEmpty';
import { InterEditorial } from './InterEditorial';
import { InterAuthorial } from './InterAuthorial';

export class FragmentMain extends React.Component {
    constructor(props) {
        console.log(props.match);

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

        console.log(this.state);

        if (this.state.interId) { // TODO: AUTHORIAL
            if (this.state.interId.includes('CRIT')) {
                inter = (
                    <InterEditorial
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
            <div>
                {inter}
                <Navigation fragId={this.state.fragmentId} />
            </div>
        );
    }

}
