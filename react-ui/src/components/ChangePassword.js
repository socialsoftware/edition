import React from 'react';
import axios from 'axios';
import { FormattedMessage } from 'react-intl';
import { Form } from 'react-bootstrap';
import { connect } from 'react-redux';
import { SERVER_URL } from '../utils/Constants';
import { getToken } from '../utils/StorageUtils';

const mapStateToProps = state => ({ info: state.info });

class ChangePassword extends React.Component {
    constructor(props) {
        super(props);

        this.handleCurrentChange = this.handleCurrentChange.bind(this);
        this.handleNewChange = this.handleNewChange.bind(this);
        this.handleRepeatChange = this.handleRepeatChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);

        this.state = {
            currentPassword: '',
            newPassword: '',
            repeatPassword: '',
        };
    }

    handleCurrentChange(event) {
        this.setState({
            currentPassword: event.target.value,
        });
    }

    handleNewChange(event) {
        this.setState({
            newPassword: event.target.value,
        });
    }

    handleRepeatChange(event) {
        this.setState({
            repeatPassword: event.target.value,
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        console.log(this.state);

        console.log(this.props.info);
        // TODO : send the request to change the password to the backend

        const username = this.props.info.username;

        axios.post(`${SERVER_URL}/api/services/frontend/restricted/change-password`, null, {
            params: {
                username,
                currentPassword: this.state.currentPassword,
                newPassword: this.state.newPassword,
                retypedPassword: this.state.repeatPassword,

            },
            headers: { Authorization: `Bearer ${getToken()}` },
        }).then((res) => {
            console.log(res);
            alert('Password changed successfully');
            this.props.history.push('/');
        },
                (res) => {
                    console.log(res);
                    alert('Error changing password. Please try again');
                });
    }

    render() {
        return (
            <div className="container text-center">

                <div className="row">
                    <h3>
                        <FormattedMessage id="user.password" />
                    </h3>
                </div>

                <div className="row">
                    <br /> <br />

                    <Form onSubmit={this.handleSubmit} className="form-horizontal" id="password">


                        <div className="form-group">
                            <label htmlFor="currentPassword" className="col-sm-4 control-label">
                                <FormattedMessage id="user.password.current" />
                            </label>
                            <div className="col-sm-4">
                                <input
                                    type="password"
                                    className="form-control"
                                    id="currentPassword"
                                    name="currentPassword"
                                    placeholder="Current Password"
                                    value={this.state.currentPassword}
                                    onChange={this.handleCurrentChange} />
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="newPassword" className="col-sm-4 control-label">
                                <FormattedMessage id="user.password.new" />
                            </label>
                            <div className="col-sm-4">
                                <input
                                    type="password"
                                    className="form-control"
                                    id="newPassword"
                                    name="newPassword"
                                    placeholder="New Password"
                                    value={this.state.newPassword}
                                    onChange={this.handleNewChange} />
                            </div>
                        </div>
                        <div className="form-group">
                            <label htmlFor="repeatPassword" className="col-sm-4 control-label">
                                <FormattedMessage id="user.password.retype" />
                            </label>
                            <div className="col-sm-4">
                                <input
                                    type="password"
                                    className="form-control"
                                    id="repeatPassword"
                                    name="repeatPassword"
                                    placeholder="Repeat Password"
                                    value={this.state.repeatPassword}
                                    onChange={this.handleRepeatChange} />
                            </div>

                        </div>
                        <div className="form-group">
                            <div className="col-sm-12">
                                <button type="submit" className="btn btn-primary">
                                    <FormattedMessage id="general.update" />
                                </button>
                            </div>
                        </div>
                    </Form>
                </div>
            </div>
        );
    }
}

export default connect(mapStateToProps)(ChangePassword);
