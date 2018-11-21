import React from "react";
import {RepositoryService} from "../services/RepositoryService";
import {addFragment, setAllFragmentsLoaded} from "../actions/index";
import {connect} from "react-redux";

const mapStateToProps = state => {
  return {fragments: state.fragments, allFragmentsLoaded: state.allFragmentsLoaded};
};

const mapDispatchToProps = dispatch => {
  return {
    addFragment: fragment => dispatch(addFragment(fragment)),
    setAllFragmentsLoaded: allFragmentsLoaded => dispatch(setAllFragmentsLoaded(allFragmentsLoaded))
  };
};

class ConnectedFragmentLoader extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      acronym: "LdoD-test"
    };
  }

  componentDidMount() {
    const service = new RepositoryService();
    service.getFragments(this.state.acronym).then(response => {
      response.data.fragments.map(f => this.props.addFragment(f));
      this.props.setAllFragmentsLoaded(true);
    });
  }

  render() {
    let fragmentToRender;

    if (this.props.allFragmentsLoaded) {
      console.log("fragments loaded")
    }
    return <div></div>;
  }

}

const FragmentLoader = connect(mapStateToProps, mapDispatchToProps)(ConnectedFragmentLoader);

export default FragmentLoader;
