import React from 'react';
import './Fragment.css';
import { connect } from "react-redux";
import { RepositoryService } from '../services/RepositoryService'

const mapStateToProps = state => {
  return {
    fragmentIndex: state.fragmentIndex,
    fragments: state.fragments
  };
};

export class ConnectedFragment extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
        list: [],
        fragmentsDistanceLoaded: false
    }
  }

  componentDidMount() {

    const service = new RepositoryService();
    service.getIntersByDistance('281861523767369', '0.0', '1.0', '0.0', '0.0').then(response => {
        this.setState({list: response.data});
        this.setState(prevState => ({
          fragmentsDistanceLoaded: !prevState.check
        }));

        //this.state.list.map(i =>alert(i.interId+":"+i.distance) );
    });


  }

  render() {

    let fragmentDistanceToRender;

    if (this.state.fragmentsDistanceLoaded) {
      //alert(this.props.fragments.length)
      this.state.list.map(i =>alert(i.interId+":"+i.distance));
      alert(this.state.list.length)
      alert(this.state.list);

    } else {
      fragmentDistanceToRender = <div></div>
    }

    return (
      <div className="box">

        {fragmentDistanceToRender}

        <p align="center"> <b> {this.props.fragmentIndex+1}/{this.props.fragments.length} </b> </p>

        <p align="center"> <b> {this.props.fragments[this.props.fragmentIndex].interId} </b> </p>

        <h1 align="center"> <b> {this.props.fragments[this.props.fragmentIndex].meta.title} </b> </h1>

        <br/>

        <p>{this.props.fragments[this.props.fragmentIndex].text}</p>

      </div>
    );
  }
}

const Fragment = connect(mapStateToProps)(ConnectedFragment);

export default Fragment;
