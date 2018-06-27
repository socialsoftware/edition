import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Alert, Button, Glyphicon } from 'react-bootstrap';

class NotFound extends Component {
    render() {
        return (
             <div>
                <Alert bsStyle="danger">
                    <h1>404</h1>
                    Page not found.
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

export default NotFound;