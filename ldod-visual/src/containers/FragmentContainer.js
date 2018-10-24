import React from 'react';
import { RepositoryService } from '../services/RepositoryService'
import Fragment from '../components/Fragment';
import { addFragment } from "../actions/index";
import { connect } from "react-redux";

const mapStateToProps = state => {
    return {
        fragments: state.fragments
    };
};

const mapDispatchToProps = dispatch => {
  return {
    addFragment: fragment => dispatch(addFragment(fragment))
  };
};

class ConnectedFragmentContainer extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
        acronym: 'LdoD-test',
        fragmentsLoaded: false
    }

  }

  componentDidMount() {

    const service = new RepositoryService();
    service.getFragments(this.state.acronym).then(response => {
        response.data.fragments.map(f => this.props.addFragment( f ) );

        this.setState(prevState => ({
          fragmentsLoaded: !prevState.check
        }));
    });


  }

  render() {
      let fragmentToRender;

      if (this.props.fragments.length>0) {
        //alert(this.props.fragments.length)
        fragmentToRender = <Fragment/>
      } else {
        fragmentToRender = <div></div>
      }

      return (
        <div>{fragmentToRender}</div>
    );
  }
}

const FragmentContainer = connect(mapStateToProps,mapDispatchToProps)(ConnectedFragmentContainer);

export default FragmentContainer;
