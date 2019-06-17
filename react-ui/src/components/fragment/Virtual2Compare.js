import React from 'react';

export class Virtual2Compare extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            ids: props.ids,
        };
    }

    render() {
        return <div>THIS SHOULD BE THE COMPARISION BETWEEN MULTIPLE VIRTUAL INTERS</div>;
    }
}
