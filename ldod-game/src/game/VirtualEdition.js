/* React imports */
import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';

/* Game imports */
import { notification } from 'antd';
import { Icon } from 'antd';
import { Button, ProgressBar, Glyphicon } from 'react-bootstrap';
import { LDOD_MESSAGE } from '../utils/Constants';
import { getVirtualEditionIndex } from '../utils/APIUtils';
import Fragment  from './Fragment';
import './VirtualEdition.css';
import LoadingIndicator  from '../common/LoadingIndicator';

class VirtualEdition extends Component {
    constructor(props) {
        super(props);
        this.state = {
            fragments: [],
            title: " ",
            acronym: " ",
            pub: false,
            taxonomy: [],
            members: [],
            index: -1,
            view: false,
            isLoading: false,
        };
        this.loadVirtualEdition = this.loadVirtualEdition.bind(this);
        this.nextFragment = this.nextFragment.bind(this);
    }

    loadVirtualEdition() {
        this.setState({
            isLoading: true,
        })

        let request = getVirtualEditionIndex("LdoD-ok");

        request.then(response =>{
            localStorage.setItem("virtualEdition", JSON.stringify(response.virtualEditionInterList));
            this.setState({
                fragments: response.virtualEditionInterList,
                title: response.title,
                acronym: response.acronym,
                pub: response.pub,
                isLoading: false
            })
            notification.success({
                message: LDOD_MESSAGE,
                description: "Virtual Edition: " + response.title + " loaded successfully.",
            })
        })
        .catch(error => {
            if(error.status === 401) {
                notification.warning({
                    message: LDOD_MESSAGE,
                    description: "Please login first!",
                    icon: <Icon type="warning" style={{ color: '#f1c40f' }} />,
                });
            } else {
                notification.error({
                    message: LDOD_MESSAGE,
                    description: "Virtual Edition not loaded, something went wrong.",
                });
            }
        });

    }

    componentDidMount() {
        this.loadVirtualEdition();
    }

    nextFragment(){
        this.setState((prevState, props) => ({
            index: prevState.index + 1,
            view: true,
        })); 
    }

    render() {
        const fragmentViews = [];
        this.state.fragments.forEach((f, fIndex) => {
            fragmentViews.push(<Fragment
                key={f.title}
                fragment={f}/>)
        });
        if(this.state.isLoading) {
            return <LoadingIndicator />;
        }
        if(this.state.view) {
            var i = this.state.index;
            return (
              <div>
                    <ProgressBar min={0} bsStyle="success"active now={this.state.index} />
                    <Fragment key={this.state.fragments[i].title} fragment={this.state.fragments[i]} acronym={this.state.acronym}/>
                    <div className="next">
                        <Button bsStyle="primary" onClick={this.nextFragment}>
                            <Glyphicon glyph="arrow-right"/> 
                        </Button>
                    </div>
              </div>
            );
        }
        return (
            <div>
                <Button bsStyle="primary" onClick={this.nextFragment}>
                    <Glyphicon glyph="ok"/> 
                </Button>
                {/*{fragmentViews*/}   
            </div>
        );
    }
    
}

export default withRouter(VirtualEdition);