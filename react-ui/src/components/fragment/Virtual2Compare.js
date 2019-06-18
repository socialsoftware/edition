import React from 'react';
import { FormattedMessage } from 'react-intl';
import axios from 'axios';

export class Virtual2Compare extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            ids: props.ids,
            isLoaded: false,
        };
    }

    getCompareForIds() {
        axios.get('http://localhost:8080/api/services/frontend/multiple-virtual', {
            params: {
                interIds: encodeURIComponent(this.state.ids),
            },
        }).then((result) => {
            console.log(result.data);

            this.setState({
                compareData: result.data,
                isLoaded: true,
            });
        });
    }

    componentDidMount() {
        this.getCompareForIds();
    }

    render() {
        if (!this.state.isLoaded) { return <div>Loading virtual comparison info</div>; }

        const ids = Object.keys(this.state.compareData);

        const tables = [];

        for (let i = 0; i < ids.length; i++) {
            const data = this.state.compareData[ids[i]];

            tables.push(<div className="row col-md-12">
                <h5>
                    <strong><FormattedMessage id="general.edition" /> : </strong>
                    {data.reference}
                </h5>
                <table className="table table-bordered table-striped table-condensed">
                    <thead>
                        <tr>
                            <th>
                                <FormattedMessage id="virtualcompare.quote" />
                            </th>
                            <th>
                                <FormattedMessage id="virtualcompare.comment" />
                            </th>
                            <th>
                                <FormattedMessage id="virtualcompare.user" />
                            </th>
                            <th>
                                <FormattedMessage id="general.tags" />
                            </th>
                        </tr>
                    </thead>
                    <tbody />
                </table>
            </div>);
        }

        return (<div id="fragmentInter" className="row">
            <h4 className="text-center">
                <FormattedMessage id="virtualcompare.title" />
            </h4>
            <br />
            {tables}
        </div>
        );
    }
}
