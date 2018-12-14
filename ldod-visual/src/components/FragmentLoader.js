import React from "react";
import {RepositoryService} from "../services/RepositoryService";
import {addFragment, setAllFragmentsLoaded, setfragmentsHashMap, setRecommendationArray, setRecommendationLoaded} from "../actions/index";
import {connect} from "react-redux";
import HashMap from "hashmap";

const mapStateToProps = state => {
  return {
    fragments: state.fragments,
    allFragmentsLoaded: state.allFragmentsLoaded,
    fragmentsHashMap: state.fragmentsHashMap,
    fragmentIndex: state.fragmentIndex,
    currentFragmentMode: state.currentFragmentMode,
    outOfLandingPage: state.outOfLandingPage,
    recommendationArray: state.recommendationArray,
    recommendationLoaded: state.recommendationLoaded
  };
};

const mapDispatchToProps = dispatch => {
  return {
    addFragment: fragment => dispatch(addFragment(fragment)),
    setAllFragmentsLoaded: allFragmentsLoaded => dispatch(setAllFragmentsLoaded(allFragmentsLoaded)),
    setfragmentsHashMap: fragmentsHashMap => dispatch(setfragmentsHashMap(fragmentsHashMap)),
    setRecommendationArray: recommendationArray => dispatch(setRecommendationArray(recommendationArray)),
    setRecommendationLoaded: recommendationLoaded => dispatch(setRecommendationLoaded(recommendationLoaded))
  };
};

class ConnectedFragmentLoader extends React.Component {
  constructor(props) {
    super(props);

    this.map = new HashMap();

    this.state = {
      acronym: "LdoD-test",
      localHistoryCount: 0,
      idsDistanceArray: []
    };
  }

  componentDidUpdate(prevProps, prevState) {
    console.log("===============================componentDIDUPDATE!======================")
    console.log("FRAGMENTLOADER: recommendationArray.length: " + this.props.recommendationArray.length);
    console.log("FRAGMENTLOADER: fragments.length: " + this.props.fragments.length);
    if (!(this.props.recommendationLoaded)) { //&& prevProps.fragmentIndex !== this.props.fragmentIndex && this.props.currentFragmentMode && this.props.outOfLandingPage) { && this.props.recommendationArray.length === this.props.fragments.length) { tirar a cond do outoflanding page no fragment loader
      console.log("===============================PROPS CHANGED! REQUESTING NEW RECOMMENDATION ARRAY======================")

      const service = new RepositoryService();

      console.log("FRAGMENTLOADER: requesting distances")
      let idsDistanceArray = [];
      let myNewRecommendationArray = [];
      service.getIntersByDistance(this.props.fragments[this.props.fragmentIndex].interId, "0.0", "1.0", "0.0", "0.0").then(response => {
        console.log("FRAGMENTLOADER: then request");
        idsDistanceArray = response.data;
        console.log("FRAGMENTLOADER: then request 2");
        myNewRecommendationArray;
        console.log("FRAGMENTLOADER: then request 3");
        let thisArray = idsDistanceArray;
        console.log("FRAGMENTLOADER: then request 3.5");

        console.log("FRAGMENTLOADER: idsDistanceArray length: " + idsDistanceArray.length);
        var i;
        for (i = 0; i < idsDistanceArray.length; i++) {
          console.log("FRAGMENTLOADER: then request 3.6");
          let frag = this.props.fragmentsHashMap.get(idsDistanceArray[i].interId);
          console.log("FRAGMENTLOADER: then request 3.7");
          myNewRecommendationArray.push(frag);
          console.log("FRAGMENTLOADER: then request 3.8");

        }
        this.props.setRecommendationArray(myNewRecommendationArray);
        myNewRecommendationArray.map(f => console.log("FRAGMENTLOADER: interId from distance request: " + f.interId));
        this.props.setRecommendationLoaded(true);
        //thisArray.map(f => myNewRecommendationArray.push(this.props.fragmentsHashMap.get(f.interId)));

        console.log("FRAGMENTLOADER: NEW RECOMMENDATION ARRAY CALCULATED!");
      });

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
