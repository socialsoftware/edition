import React, { Component, createRef } from 'react';
import Graph from 'vis-react';

class NetworkGraphReact extends Component {

  constructor(props) {
      super(props);
      this.network = {};
      this.appRef = createRef();

    }


  render() {
    const graph = {
      nodes: [
          {id: 1, label: 'Node 1'},
          {id: 2, label: 'Node 2'},
          {id: 3, label: 'Node 3'},
          {id: 4, label: 'Node 4'},
          {id: 5, label: 'Node 5'}
        ],
      edges: [
          {from: 1, to: 2},
          {from: 1, to: 3},
          {from: 2, to: 4},
          {from: 2, to: 5}
        ]
    };

    const options = {
        layout: {
            hierarchical: true
        },
        edges: {
            color: "#000000"
        }
    };

    const events = {
        select: function(event) {
            var { nodes, edges } = event;
        }
    }



    return (
      <Graph graph={graph} options={options} events={events} />
    );
  }
}

export default NetworkGraphReact;
