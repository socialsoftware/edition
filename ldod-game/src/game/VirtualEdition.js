/* React imports */
import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';

/* Game imports */
import Fragment  from './Fragment';
import { FRAGMENT_LIST_SIZE, LDOD_MESSAGE } from '../utils/Constants';
import { getVirtualEditionIndex, } from '../utils/APIUtils';
import { Layout, notification } from 'antd';
import { Icon } from 'antd';
import { Button, ProgressBar, Glyphicon } from 'react-bootstrap';



class VirtualEdition extends Component {
    constructor(props) {
        super(props);
        this.state = {
            fragments: [],
            index: 0,
            view: false
        };
        this.loadVirtualEdition = this.loadVirtualEdition.bind(this);
        this.nextFragment = this.nextFragment.bind(this);
    }

    loadVirtualEdition() {
        let request = getVirtualEditionIndex("LdoD-ok");

        request.then(response =>{
            localStorage.setItem("virtualEdition", JSON.stringify(response.virtualEditionInterList));
            this.setState({
                fragments: response.virtualEditionInterList,
            })
            notification.success({
                message: LDOD_MESSAGE,
                description: "Virtual Edition loaded",
            })
        }).catch(error => {
            notification.warning({
                message: LDOD_MESSAGE,
                description: "Virtual Edition not loaded",
                icon: <Icon type="warning" style={{ color: '#f1c40f' }} />,
            });
        });

    }

    componentWillMount() {
        this.loadVirtualEdition();
    }

    nextFragment(){
        /*for (var i = 0; i < this.state.fragments.length; i++) {
            console.log(this.state.fragments[i].meta.title);
            <Fragment key={this.state.fragments[i].meta.title} fragment={this.state.fragments[i]}/>    
        }*/
        var i = this.state.index;
        this.setState({
            index: (i+1),
            view: true,
        })
    }

    render() {
        const fragmentViews = [];
        this.state.fragments.forEach((f, fIndex) => {
            fragmentViews.push(<Fragment
                key={f.title}
                fragment={f}/>)
        });
        if(this.state.view) {
            var i = this.state.index;
            return (
              <div>
                    <ProgressBar min={0} bsStyle="success"active now={this.state.index} />
                    <Fragment key={this.state.fragments[i].title} fragment={this.state.fragments[i]}/>
                    <Button bsStyle="primary" onClick={this.nextFragment}>
                        <Glyphicon glyph="arrow-right"/> 
                    </Button>
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