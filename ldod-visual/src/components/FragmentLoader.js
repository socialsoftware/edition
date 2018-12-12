import React from "react";
import {RepositoryService} from "../services/RepositoryService";
import {addFragment, setAllFragmentsLoaded, setfragmentsHashMap} from "../actions/index";
import {connect} from "react-redux";
import HashMap from "hashmap";

const mapStateToProps = state => {
  return {fragments: state.fragments, allFragmentsLoaded: state.allFragmentsLoaded, fragmentsHashMap: state.fragmentsHashMap, fragmentIndex: state.fragmentIndex};
};

const mapDispatchToProps = dispatch => {
  return {
    addFragment: fragment => dispatch(addFragment(fragment)),
    setAllFragmentsLoaded: allFragmentsLoaded => dispatch(setAllFragmentsLoaded(allFragmentsLoaded)),
    setfragmentsHashMap: fragmentsHashMap => dispatch(setfragmentsHashMap(fragmentsHashMap))
  };
};

class ConnectedFragmentLoader extends React.Component {
  constructor(props) {
    super(props);

    this.map = new HashMap();

    this.state = {
      acronym: "LdoD-test",
      localHistoryCount: 0
    };
  }

  componentDidUpdate(prevProps, prevState) {
    console.log("===============================componentDIDUPDATE!======================")
    // only update chart if the data has changed
    if (prevProps !== this.props) {
      console.log("===============================PROPS CHANGED!======================")
      console.log("previous props fragmentIndex: " + prevProps.fragmentIndex)
      console.log("updated props fragmentIndex: " + this.props.fragmentIndex)
    }
  }

  componentDidMount() {
    const service = new RepositoryService();
    service.getFragments(this.state.acronym).then(response => {
      response.data.fragments.map(f => this.props.addFragment(f));
      this.props.fragments.map(f => this.map.set(f.interId, f));
      this.props.setfragmentsHashMap(this.map);
      this.props.setAllFragmentsLoaded(true);
    });
  }

  render() {

    let fragmentToRender;

    if (this.props.allFragmentsLoaded) {
      console.log("fragments loaded")
      //this.props.fragments.map(f => console.log("id " + f.interId + " on my hashmap: " + this.props.fragmentsHashMap.get(f.interId) + " with title: " + this.props.fragmentsHashMap.get(f.interId).meta.title))
    }
    return <div></div>;
  }

}

const FragmentLoader = connect(mapStateToProps, mapDispatchToProps)(ConnectedFragmentLoader);

export default FragmentLoader;
