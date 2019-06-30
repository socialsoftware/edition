import React from 'react';
import axios from 'axios';
import { connect } from 'react-redux';
import { FormattedMessage } from 'react-intl';
import { SERVER_URL } from '../../utils/Constants';

const mapStateToProps = state => ({ token: state.token });

class Taxonomy extends React.Component {
    constructor(props) {
        super(props);

        this.removeCategory = this.removeCategory.bind(this);

        this.state = {
            fragmentId: props.fragmentId,
            interId: props.interId,
            externalId: props.externalId,
            title: props.title,
            taxonomy: null,
            isLoaded: false,
            categories: null,
            isCatLoaded: false,
        };
    }

    getTaxonomyInfo() {
        axios.get(`${SERVER_URL}/api/services/frontend/taxonomy`, {
            params: {
                xmlId: this.state.fragmentId,
                urlId: this.state.interId,
            },
        }).then((result) => {
            this.setState({
                taxonomy: result.data,
                isLoaded: true,
            });
        });

        const token = sessionStorage.getItem('TOKEN');

        if (token !== null) {
            axios.get(`${SERVER_URL}/api/services/frontend/categories`, {
                params: {
                    xmlId: this.state.fragmentId,
                    urlId: this.state.interId,
                },
                headers: { Authorization: `Bearer ${token}` },
            }).then((result) => {
                this.setState({
                    categories: result.data,
                    isCatLoaded: true,
                });
            });
        }
    }

    removeCategory(interId, catId) {
        axios.post(`${SERVER_URL}/api/services/frontend/restricted/dissociate-category`, null, {
            params: {
                externalId: interId,
                categoryId: catId,
            },
            headers: { Authorization: `Bearer ${sessionStorage.getItem('TOKEN')}` },
        }).then((res) => {
            console.log(res);
            this.getTaxonomyInfo();
        });
    }

    componentDidMount() {
        this.getTaxonomyInfo();
    }

    render() {
        if (!this.state.isLoaded || (sessionStorage.getItem('TOKEN') != null && !this.state.isCatLoaded)) {
            return (
                <div>Loading taxonomy info</div>
            );
        }

        const taxRows = [];

        for (let i = 0; i < this.state.taxonomy.length; i++) {
            const taxInfo = this.state.taxonomy[i];

            const userRow = [];

            for (let j = 0; j < taxInfo.users.length; j++) {
                const userInfo = taxInfo.users[j];

                const ref = `${SERVER_URL}/edition/user/${userInfo.username}`;

                userRow.push(<a href={ref}>{userInfo.firstName} {userInfo.lastName} ({userInfo.username})</a>);
            }

            const ref = `http://localhost:9000/edition/acronym/${taxInfo.acronym}/category/${taxInfo.urlId}`;

            taxRows.push(
                <tr>
                    <td><a
                        href={ref}>{taxInfo.name}</a>
                        {this.props.token && <a
                            onClick={() => this.removeCategory(taxInfo.interExternal, taxInfo.categoryExternal)}>
                            <span className="glyphicon glyphicon-remove" /></a>}
                    </td>
                    <td>
                        {userRow}
                    </td>
                </tr>,

            );
        }

        const assignedOptions = [];
        const nonAssignedOptions = [];

        if (this.state.categories) {
            for (let i = 0; i < this.state.categories.assigned.length; i++) {
                assignedOptions.push(<option
                    value={this.state.categories.assigned[i]}
                    selected="selected">{this.state.categories.assigned[i]}</option>);
            }

            for (let i = 0; i < this.state.categories.nonAssigned.length; i++) {
                nonAssignedOptions.push(<option
                    value={this.state.categories.nonAssigned[i]}>{this.state.categories.nonAssigned[i]}</option>);
            }
        }


        return ([
            <div className="row" id="taxonomy">
                <link
                    href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/css/select2.min.css"
                    rel="stylesheet" />
                <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/js/select2.min.js" />
                {this.props.token && <button
                    className="btn btn-primary pull-right"
                    data-toggle="modal"
                    data-target="#myModal">
                    <span className="glyphicon glyphicon-plus" />
                </button>}
                <table className="table table-hover">
                    <thead>
                        <tr>
                            <th><span className="glyphicon glyphicon-tag" /></th>
                            <th><span className="glyphicon glyphicon-user" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        {taxRows}
                    </tbody>
                </table>
            </div>,

            <div className="modal fade" id="myModal" tabIndex="-1" role="dialog">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <button
                                type="button"
                                className="close"
                                data-dismiss="modal"
                                aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                            <h4 className="modal-title">{this.state.title}</h4>
                        </div>
                        <div className="modal-body">
                            <div className="row text-center">
                                <form
                                    className="form"
                                    method="POST"
                                    action="/virtualeditions/restricted/tag/associate">
                                    <div className="form-group">
                                        <div className="hidden">
                                            <input
                                                type="hidden"
                                                name="fragInterId"
                                                className="form-control"
                                                value={this.state.externalId} />
                                        </div>
                                    </div>
                                    <div className="form-group">
                                        <select
                                            name="categories[]"
                                            id="category-select"
                                            className="form-control"
                                            style={{ width: '75%' }}
                                            multiple>
                                            {nonAssignedOptions}
                                            {assignedOptions}
                                        </select>
                                    </div>
                                    <div className="form-group">
                                        <button type="submit" className="btn btn-sm btn-primary">
                                            <FormattedMessage id={'general.associate'} />
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-default" data-dismiss="modal">
                                <FormattedMessage id={'general.close'} />
                            </button>
                        </div>
                    </div>
                </div>
            </div>,
        ]
        );
    }
}

export default connect(mapStateToProps)(Taxonomy);
