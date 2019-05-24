import React from 'react';

export class InterEmpty extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            fragmentId: props.fragmentId,
        };
    }

    render() {
        return (
            <div id="fragmentInter" className="row">
                <h4 className="text-center">{this.state.fragmentId}</h4>
            </div>
        );
    }
}
