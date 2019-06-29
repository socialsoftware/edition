import React from 'react';
import axios from 'axios';
import { FormattedMessage } from 'react-intl';
import { Form } from 'react-bootstrap';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';
import { SERVER_URL } from '../utils/Constants';
import { setAccessToken, setUserInfo } from '../actions/actions';

const mapStateToProps = state => ({ token: state.token });

class Login extends React.Component {
    constructor(props) {
        super(props);

        this.handleLogin = this.handleLogin.bind(this);

        this.handleUsernameChange = this.handleUsernameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);

        this.state = {
            username: '',
            password: '',
        };
    }

    handleLogin(event) {
        event.preventDefault(); // disable default handling of form.

        const loginInfo = Object.assign({}, this.state);

        axios.post(`${SERVER_URL}/api/auth/signin`, loginInfo)
            .then((result) => {
                console.log(result.data);
                this.props.setAccessToken(result.data.accessToken);
                this.props.history.push('/');

                sessionStorage.setItem('TOKEN', result.data.accessToken);

                axios.get(`${SERVER_URL}/api/user`, {
                    headers: { Authorization: `Bearer ${result.data.accessToken}` },
                }).then((res) => {
                    this.props.setUserInfo(res.data);
                });
            },
                  (result) => {
                      console.log('Login rejected');
                      console.log(result);
                  })
            .catch(((reason) => {
                console.log('Login failed');
                console.log(reason);
            }));
    }

    handleUsernameChange(event) {
        this.setState({
            username: event.target.value,
        });
    }

    handlePasswordChange(event) {
        this.setState({
            password: event.target.value,
        });
    }

    render() {
        if (this.props.token !== '') {
            return <Redirect to="/" />;
        }

        return (
            <div className="row">
                <div className="login-form">
                    <h2 style={{ textAlign: 'center' }}>
                        <FormattedMessage id={'header.title'} />
                    </h2>
                    <Form onSubmit={this.handleLogin} className="form-horizontal" >
                        <div className="form-group">
                            <div className="col-md-offset-4 col-md-4">
                                <input type="hidden" />
                                <input
                                    type="text"
                                    className="form-control"
                                    id="username_or_email"
                                    name="username"
                                    placeholder="Username"
                                    value={this.state.username}
                                    onChange={this.handleUsernameChange} />
                            </div>
                            <br /> <br />
                            <div className="col-md-offset-4 col-md-4">
                                <input
                                    type="password"
                                    className="form-control"
                                    id="password"
                                    name="password"
                                    placeholder="Password"
                                    value={this.state.password}
                                    onChange={this.handlePasswordChange} />
                            </div>
                        </div>
                        <div className="form-group">
                            <div className="login-form col-md-offset-5 col-md-2">
                                <button disabled={!this.state.username} className="btn btn-primary form-control" type="submit">
                                    <FormattedMessage id={'general.signin'} />
                                </button>
                            </div>
                        </div>
                    </Form>
                </div>
            </div>
        );
    }
}

export default connect(mapStateToProps, { setAccessToken, setUserInfo })(Login);
