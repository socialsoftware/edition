import React from 'react';

export class InterEmpty extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            fragmentId: props.fragmentId,
            title: props.title,
        };
    }

    render() {
        return (
            <div id="fragmentInter" className="row">
                <h4 className="text-center">{this.state.title}</h4>
            </div>
        );
    }
}
