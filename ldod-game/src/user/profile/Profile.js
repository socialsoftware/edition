import React, { Component } from 'react';
import { getUserProfile, getPublicVirtualEditions4User } from '../../utils/APIUtils';
import { Avatar, Tabs } from 'antd';
import LoadingIndicator  from '../../common/LoadingIndicator';
import './Profile.css';
import NotFound from '../../common/NotFound';
import ServerError from '../../common/ServerError';

const TabPane = Tabs.TabPane;

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
            localStorage.setItem(username + "publicVEs", JSON.stringify(response));
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

        const tabBarStyle = {
            textAlign: 'center'
        };

        return (
            <div className="profile">
                {
                    this.state.user ? (
                        <div className="user-profile">
                            <div className="user-details">
                                <div className="user-avatar">
                                    <Avatar size="large" style={{ backgroundColor: '#2ecc71' }} icon="user">
                                        {this.state.user.username[0].toUpperCase()}
                                        {this.state.user.username.slice(1,this.state.user.username.length)}
                                    </Avatar>
                                </div>
                                <div className="user-summary">
                                    <div className="username">{this.state.user.username}</div>
                                    <div className="user-joined">
                                        Joined 
                                    </div>
                                    <div className="user-joined">
                                        Virtual Editions: 
                                        
                                    </div>
                                </div>
                            </div>
                            <div className="user-poll-details">
                                <Tabs defaultActiveKey="1"
                                    animated={false}
                                    tabBarStyle={tabBarStyle}
                                    size="large"
                                    className="profile-tabs">
                                    <TabPane tab={`${this.state.user.username} Ranking`} key="1">
                                    </TabPane>
                                    <TabPane tab={`${this.state.user.username} Votes`}  key="2">
                                    </TabPane>
                                </Tabs>
                            </div>
                        </div>
                    ): null
                }
            </div>
        );
    }
}

export default Profile;