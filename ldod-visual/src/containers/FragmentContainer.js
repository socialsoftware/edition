import React from "react";
import {RepositoryService} from "../services/RepositoryService";
import Fragment from "../components/Fragment";
import {addFragment} from "../actions/index";
import {connect} from "react-redux";

const mapStateToProps = state => {
  return {fragments: state.fragments};
};

const mapDispatchToProps = dispatch => {
  return {
    addFragment: fragment => dispatch(addFragment(fragment))
  };
};

class ConnectedFragmentContainer extends React.Component {

  render() {
    let fragmentToRender;

    if (this.props.fragments.length > 0) {
      //alert(this.props.fragments.length)
      fragmentToRender = <Fragment/>;
    } else {
      fragmentToRender = <div/>;
    }

    return <div>{fragmentToRender}</div>;
  }
}

const FragmentContainer = connect(mapStateToProps, mapDispatchToProps)(ConnectedFragmentContainer);

export default FragmentContainer;
