import React from 'react';

export class FragmentMain extends React.Component {
    constructor(props) {
        console.log(props);

        super(props);
        this.state = {
            fragmentId: props.match.params.fragId,
        };
    }

    render() {
        return <div>Hello world {this.state.fragmentId}</div>;
    }

}
