import { Network } from 'vis';
import React, { Component, createRef } from 'react';

const nodes = [
  {id: 1, label: 'Node 1'},
  {id: 2, label: 'Node 2'},
  {id: 3, label: 'Node 3'},
  {id: 4, label: 'Node 4'},
  {id: 5, label: 'Node 5'}
];

const edges = [
  {from: 1, to: 3},
  {from: 1, to: 2},
  {from: 2, to: 4},
  {from: 2, to: 5},
  {from: 3, to: 3}
];

const data = {
  nodes: nodes,
  edges: edges
};

const options = {
    height: 500,
    layout: {
        hierarchical: false
    }
};

class NetworkGraph extends Component {

  constructor(props) {
      super(props);
      this.network = {};
      this.appRef = createRef();


    }

  handleSelectNode(event) {
      const nodeId = event.nodes[0];
      if (nodeId) {
          alert(nodeId);
      }
  }

  componentDidMount() {
     this.network = new Network(this.appRef.current, data, options);
     this.network.on("selectNode", this.handleSelectNode);
   }

  render() {

    const divStyle = {
      width: '800px',
      height: '500px',
      margin: '30px auto',
      display: 'flex'
    };

    return (
      <div style={divStyle}>
        <div ref={this.appRef} />
      </div>
    );
  }
}

export default NetworkGraph;
