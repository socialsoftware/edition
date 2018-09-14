import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import {  Button, Glyphicon, Grid, Jumbotron} from 'react-bootstrap';


class ServerError extends Component {
    
    render() {
        return (
            <Grid fluid>
				<Jumbotron  style={{ backgroundColor: 'white' }} >
                    <div className="text-center">
                        <i className="fa  fa-spin fa-wrench fa-5x"></i>
                    </div>
                    <h1 className="text-center">Error 500: Ooops! Something went wrong...</h1>
                </Jumbotron>
                <div className="text-center"> 
                <Button bsStyle="primary" componentClass={Link} to="/">
                    Go Back <Glyphicon glyph="home"/> 
                </Button>   
                </div>	
            </Grid>
        );
    }
}

export default ServerError;