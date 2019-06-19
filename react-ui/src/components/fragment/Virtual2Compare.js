import React from 'react';
import { FormattedMessage } from 'react-intl';
import axios from 'axios';

function TagEntry(data) {
    data = data.data;

    const userRef = `http://localhost:8080/edition/user/${data.username}`;
    const editionRef = `http://localhost:8080/edition/acronym/${data.acronym}/category/${data.urlId}`;

    return (
        <tr>
            <td>---</td>
            <td>---</td>
            <td><span className="glyphicon glyphicon-user" /> <a
                href={userRef}>{data.username}</a>
            </td>
            <td><span className="glyphicon glyphicon-tag" /> <a
                href={editionRef}>{data.name}</a>
            </td>
        </tr>
    );
}

function AnnotationEntry(data) {
    data = data.data;

    const annotationInfo = [];

    annotationInfo.push(<td>{data.quote}</td>);

    // TODO: COUNTRY INFO

    if (data.source) {
        annotationInfo.push(
            <td><a href={data.source}>tweet</a>
                <br />
                <a href={data.profile}>profile</a>
                <br />
                {data.date}
                ADD COUNTRY INFO
            </td>);
    } else {
        annotationInfo.push(<td>{data.text}</td>);
    }

    const userRef = `http://localhost:8080/edition/user/${data.username}`;

    annotationInfo.push(<td><span className="glyphicon glyphicon-user" /> <a
        href={userRef}>{data.username}</a></td>);

    if (data.tags) {
        const tagRows = [];

        for (let i = 0; i < data.tags.length; i++) {
            const tagInfo = data.tags[i];
            const tagRef = `http://localhost:8080/edition/acronym/${tagInfo.acronym}/category/${tagInfo.urlId}`;

            tagRows.push(<span className="glyphicon glyphicon-tag" />);
            tagRows.push(<a href={tagRef}> {tagInfo.name}</a>);
        }

        annotationInfo.push(<td>{tagRows}</td>);
    }

    return annotationInfo;
}

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

            const tagRows = [];
            const annotationRows = [];

            for (let j = 0; j < data.tags.length; j++) {
                tagRows.push(<TagEntry data={data.tags[j]} />);
            }

            for (let j = 0; j < data.annotations.length; j++) {
                annotationRows.push(<tr><AnnotationEntry data={data.annotations[j]} /></tr>);
            }

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
                    <tbody>
                        {tagRows}
                        {annotationRows}
                    </tbody>
                </table>
            </div>);
        }

        return (
            <div id="fragmentInter" className="row col-md-9">
                <h4 className="text-center">
                    <FormattedMessage id="virtualcompare.title" />
                </h4>
                <br />
                {tables}
            </div>
        );
    }
}
