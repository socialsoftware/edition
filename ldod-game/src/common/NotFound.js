import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Alert, Button, Glyphicon } from 'react-bootstrap';

class NotFound extends Component {
    render() {
        return (
             <div>
                <Alert bsStyle="danger">
                    <h2 class="text-center">Page not found.</h2>
                </Alert>
                <Link to="/">
                    <div class="text-center"> 
                        <Button bsStyle="primary">
                        Go Back <Glyphicon glyph="home"/> 
                        </Button>
                    </div>
                </Link>
            </div>  
        );
    }
}

export default NotFound;