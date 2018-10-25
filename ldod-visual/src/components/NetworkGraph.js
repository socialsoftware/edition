import { Network } from 'vis';
import React, { Component, createRef } from 'react';
import { setFragmentIndex } from "../actions/index";
import { connect } from "react-redux";
import './NetworkGraph.css';

const mapStateToProps = state => {
    return {
        fragments: state.fragments,
        fragmentIndex: state.fragmentIndex
    };
};

const mapDispatchToProps = dispatch => {
  return {
    setFragmentIndex: fragmentIndex => dispatch(setFragmentIndex(fragmentIndex))
  };
};


//{from: 1, to: 3},
const edges = [
];

const options = {
    height: "500",
    layout: {
        hierarchical: false
    }
};



class ConnectedNetworkGraph extends Component {

  constructor(props) {
      super(props);
      this.network = {};
      this.appRef = createRef();
      this.nodes=[];

      console.log(this.props.graphData)

      var i;
      let obj;
      obj={id: this.props.graphData[0].interId,
           label: "",
           color: 'red',
           title: 'id: '+this.props.graphData[0].interId+
                  ' distance: '+this.props.graphData[0].distance}

      this.nodes.push(obj)

      for (i = 1; i < this.props.graphData.length; i++) {

          obj={id: this.props.graphData[i].interId,
               label: "",
               color: 'blue',
               title: 'id: '+this.props.graphData[i].interId+
                      ' distance: '+this.props.graphData[i].distance}

          this.nodes.push(obj)
        }


      for (i = 1; i < this.props.graphData.length; i++) {

          obj={from: this.props.graphData[0].interId,
               to: this.props.graphData[i].interId,
               length: (1000*this.props.graphData[i].distance),
               hidden: true
             }

          edges.push(obj)
        }


      this.handleSelectNode  = this.handleSelectNode.bind(this);


    }



  handleSelectNode(event) {
      const nodeId = event.nodes[0];
      if (nodeId) {
          //alert(nodeId);
        var i;
        for (i = 0; i < this.props.fragments.length; i++) {
            if (this.props.fragments[i].interId === nodeId){
              this.props.setFragmentIndex(i);
              this.props.onChange();
            }

          }

        }
    }

  componentDidMount() {
    const data = {
      nodes: this.nodes,
      edges: edges
    };

     this.network = new Network(this.appRef.current, data, options);
     this.network.on("selectNode", this.handleSelectNode);
   }

  render() {

    return (
      <div className="graph">
        <div ref={this.appRef} />
      </div>
    );
  }
}

const NetworkGraph = connect(mapStateToProps,mapDispatchToProps)(ConnectedNetworkGraph);

export default NetworkGraph;
