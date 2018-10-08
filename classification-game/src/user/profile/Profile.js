import React, { Component } from 'react';
import { getUserProfile, getPublicVirtualEditions4User } from '../../utils/APIUtils';
import { Grid, Row, Col, ListGroup, ListGroupItem} from 'react-bootstrap';
import LoadingIndicator  from '../../common/LoadingIndicator';
import NotFound from '../../common/NotFound';
import ServerError from '../../common/ServerError';

class Profile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: null,
            isLoading: false,
            notFound: false,
            publicVEs: []
        }
        this.loadUserProfile = this.loadUserProfile.bind(this);
        this.getUserPublicVirtualEditions = this.getUserPublicVirtualEditions.bind(this);
    }
    
    loadUserProfile(username) {
        this.setState({
            isLoading: true});

        getUserProfile(username)
        .then(response => {
            this.setState({
                user: response,
                isLoading: false
            });
        }).catch(error => {
            if(error.status === 404) {
                this.setState({
                    notFound: true,
                    isLoading: false
                });
            } else {
                this.setState({
                    serverError: true,
                    isLoading: false
                });
            }
        });
    }

    getUserPublicVirtualEditions(username){
        getPublicVirtualEditions4User(username).then(response => {
            this.setState({
                publicVEs: response
            });
        });
    }

    componentDidMount() {
        const username = this.props.match.params.username;
        this.loadUserProfile(username);
        this.getUserPublicVirtualEditions(username);
    }

    componentWillReceiveProps(nextProps) {
        if(this.props.match.params.username !== nextProps.match.params.username) {
            this.loadUserProfile(nextProps.match.params.username);
        }
    }

    render() {
        if(this.state.isLoading) {
            return <LoadingIndicator />;
        }

        if(this.state.notFound) {
            return <NotFound />;
        }

        if(this.state.serverError) {
            return <ServerError />;
        }
        return (
            <div className="profile">
                <Grid fluid>
                    { 
                        this.state.user ? (
                    <Row>
                        <Col mdOffset={2} md={8} lgOffset={3} lg={6}>
                            <h4 className="text-center">
                                {this.state.user.firstName} {this.state.user.lastName}
                            </h4>
                            <ListGroup>
                                    Virtual Editions: 
                                    {this.state.publicVEs.map((publicVE,index) => (
                                            <div key={index}>
                                                <ListGroupItem bsStyle="info">{publicVE.title}</ListGroupItem>
                                            </div>
                                            ))}
                            </ListGroup>
                            <a href={`https://ldod.uc.pt/edition/user/${this.state.user.username}`}><em>LdoD Archive</em> profile.</a>
                            </Col>
                    </Row> ) : null }
                </Grid>
            </div>
        );
    }
}

export default Profile;