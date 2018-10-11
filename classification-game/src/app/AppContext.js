import React, { Component } from 'react';
export const Context = React.createContext();
export const Provider = Context.Provider;
export const Consumer = Context.Consumer;

class AppContext extends Component {
    constructor(props) {
        super(props);
    }
    
    
    render() {
        return (
            <Provider value={this.state}> 
                {this.props.children}
            </Provider>      
        );
    }
}

export default AppContext;