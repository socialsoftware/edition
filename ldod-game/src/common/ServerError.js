import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Alert, Button, Glyphicon } from 'react-bootstrap';

class ServerError extends Component {
    
    render() {
        return (
            <div>
                <Alert bsStyle="danger">
                    <h1>500</h1>
                    Oops! Something went wrong...
                </Alert>
                <Link to="/">
                    <Button bsStyle="primary">
                    Go Back <Glyphicon glyph="home"/> 
                    </Button>
                </Link>
            </div>
        );
    }
}

export default ServerError;