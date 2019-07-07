import React from 'react';
import axios from 'axios';
import { connect } from 'react-redux';
import { FormattedMessage } from 'react-intl';
import { Form } from 'react-bootstrap';
import Select from 'react-select';
import 'react-select/dist/react-select.css';
import { SERVER_URL } from '../../utils/Constants';
import { getToken } from '../../utils/StorageUtils';

const mapStateToProps = state => ({ status: state.status });

class Taxonomy extends React.Component {
    constructor(props) {
        super(props);

        this.removeCategory = this.removeCategory.bind(this);
        this.changedCategory = this.changedCategory.bind(this);
        this.addCategory = this.addCategory.bind(this);


        this.state = {
            fragmentId: props.fragmentId,
            interId: props.interId,
            externalId: props.externalId,
            title: props.title,
            annCall: props.annCall,
            taxonomy: null,
            selectedCats: [],
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

        if (this.props.status) {
            axios.get(`${SERVER_URL}/api/services/frontend/categories`, {
                params: {
                    xmlId: this.state.fragmentId,
                    urlId: this.state.interId,
                },
                headers: { Authorization: `Bearer ${getToken()}` },
            }).then((result) => {
                const defaultValues = [];

                for (let i = 0; i < result.data.assigned.length; i++) {
                    defaultValues.push(result.data.assigned[i]);
                }

                this.setState({
                    categories: result.data,
                    selectedCats: defaultValues,
                    isCatLoaded: true,
                });
            });
        }
    }

    changedCategory(event) {
        this.setState({
            selectedCats: event,
        });
    }

    addCategory(event) {
        event.preventDefault();

        axios.post(`${SERVER_URL}/api/services/frontend/restricted/associate-category`, null, {
            params: {
                externalId: this.state.externalId,
                categories: encodeURIComponent(this.state.selectedCats.map(x => x.value)),
            },
            headers: { Authorization: `Bearer ${getToken()}` },
        }).then((res) => {
            console.log(res);
            this.getTaxonomyInfo();
            this.state.annCall(this.state.externalId);
        });
    }

    removeCategory(interId, catId) {
        axios.post(`${SERVER_URL}/api/services/frontend/restricted/dissociate-category`, null, {
            params: {
                externalId: interId,
                categoryId: catId,
            },
            headers: { Authorization: `Bearer ${getToken()}` },
        }).then((res) => {
            console.log(res);
            this.getTaxonomyInfo();
            this.state.annCall(this.state.externalId);
        });
    }

    componentDidMount() {
        this.getTaxonomyInfo();
    }

    render() {
        if (!this.state.isLoaded || (this.props.status && !this.state.isCatLoaded)) {
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

            const ref = `${SERVER_URL}/edition/acronym/${taxInfo.acronym}/category/${taxInfo.urlId}`;

            taxRows.push(
                <tr>
                    <td><a
                        href={ref}>{taxInfo.name}</a>
                        {this.props.status && <a
                            onClick={() => this.removeCategory(taxInfo.interExternal, taxInfo.categoryExternal)}>
                            <span className="glyphicon glyphicon-remove" /></a>}
                    </td>
                    <td>
                        {userRow}
                    </td>
                </tr>,

            );
        }

        const allValues = [];

        if (this.state.categories) {
            for (let i = 0; i < this.state.categories.assigned.length; i++) {
                allValues.push({ value: this.state.categories.assigned[i], label: this.state.categories.assigned[i] });
            }

            for (let i = 0; i < this.state.categories.nonAssigned.length; i++) {
                allValues.push({ value: this.state.categories.nonAssigned[i], label: this.state.categories.nonAssigned[i] });
            }
        }

        return ([
            <div className="row" id="taxonomy">
                <link
                    href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/css/select2.min.css"
                    rel="stylesheet" />
                <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/js/select2.min.js" />
                {this.props.status && <button
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
                                <Form
                                    onSubmit={this.addCategory}
                                    className="form">
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
                                        <Select.Creatable
                                            multi
                                            options={allValues}
                                            value={this.state.selectedCats}
                                            onChange={this.changedCategory} />
                                    </div>
                                    <div className="form-group">
                                        <button type="submit" className="btn btn-sm btn-primary">
                                            <FormattedMessage id={'general.associate'} />
                                        </button>
                                    </div>
                                </Form>
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
