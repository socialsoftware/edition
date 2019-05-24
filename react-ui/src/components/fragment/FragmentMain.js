import React from 'react';
import { InterAuthorial } from './InterAuthorial';

export class FragmentMain extends React.Component {
    constructor(props) {
        console.log(props);

        super(props);
        this.state = {
            fragmentId: props.match.params.fragId,
        };
    }

    render() {
        return (
            <InterAuthorial fragmentId={this.state.fragmentId} />
        );
    }

}
