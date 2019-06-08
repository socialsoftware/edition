import React from 'react';
import axios from 'axios';

export class InterVirtual extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            fragmentId: props.fragmentId,
            interId: props.interId,
            title: props.title,
            editionInfo: null,
            transcription: null,
            isTransLoaded: false,
            isVirtualLoaded: false,
        };
    }

    getInterTranscription() {
        axios.get('http://localhost:8080/api/services/frontend/inter-writer', {
            params: {
                xmlId: this.state.fragmentId,
                urlId: this.state.interId,
            },
        }).then((result) => {
            console.log(result.data);

            this.setState({
                transcription: result.data,
                isLoaded: true,
            });
        });
    }

    getUsesInfo() {
        axios.get('http://localhost:8080/api/services/frontend/virtual-edition', {
            params: {
                xmlId: this.state.fragmentId,
                urlId: this.state.interId,
            },
        }).then((result) => {
            console.log(result.data);

            this.setState({
                editionInfo: result.data,
                isVirtualLoaded: true,
            });
        });
    }

    componentDidMount() {
        this.getInterTranscription();
        this.getUsesInfo();
    }

    render() {
        if (!this.state.isLoaded || !this.state.isVirtualLoaded) {
            return (
                <div>Loading inter info</div>
            );
        }

        return (
            <div>
                A virtual inter page will be displayed here
            </div>
        );
    }
}
