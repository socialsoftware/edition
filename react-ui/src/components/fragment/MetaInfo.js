import React from 'react';
import axios from 'axios';

export class MetaInfo extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            metaInfo: null,
            isLoaded: false,
            fragId: props.fragId,
            interId: props.interId,
        };
    }

    getMetaInfo() {
        axios.get('http://localhost:8080/api/services/frontend/meta-info', {
            params: {
                xmlId: this.state.fragId,
                urlId: this.state.interId,
            },
        }).then((res) => {
            this.setState({
                metaInfo: res.data,
                isLoaded: true,
            });
        });
    }

    componentDidMount() {
        this.getMetaInfo();
    }

    // TODO : get and do something with meta-info from backend

    render() {
        if (this.state.isLoaded) { console.log(this.state.metaInfo); }


        return <strong>Valid meta info should go here</strong>;
    }
}
