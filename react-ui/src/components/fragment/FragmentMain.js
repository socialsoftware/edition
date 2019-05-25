import React from 'react';
import axios from 'axios';
import { Navigation } from './Navigation';
import { InterAuthorial } from './InterAuthorial';

export class FragmentMain extends React.Component {
    constructor(props) {
        console.log(props);

        super(props);
        this.state = {
            fragmentId: props.match.params.fragId,
            fragInfo: null,
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
        return (
            <div>
                <InterAuthorial fragmentId={this.state.fragmentId} title={this.state.fragInfo.title} />
                <Navigation fragId={this.state.fragmentId} />
            </div>
        );
    }

}
