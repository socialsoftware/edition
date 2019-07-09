import React from 'react';
import axios from 'axios';
import annotator from 'annotator';
import { connect } from 'react-redux';
import { FormattedMessage } from 'react-intl';
import ReactHTMLParser from 'react-html-parser';
import Taxonomy from './Taxnonomy';
import { SERVER_URL } from '../../utils/Constants';
import { getToken } from '../../utils/StorageUtils';

const mapStateToProps = state => ({ info: state.info });

function parseTags(string) {
    string = window.$.trim(string);
    let tags = [];
    if (string) {
        tags = string.split(/,/);
    }
    console.log(tags);
    return tags;
}

class InterVirtual extends React.Component {
    constructor(props) {
        super(props);

        this.editorExtension = this.editorExtension.bind(this);
        this.loadAnnotationCats = this.loadAnnotationCats.bind(this);

        this.state = {
            fragmentId: props.fragmentId,
            interId: props.interId,
            title: props.title,
            editionInfo: null,
            transcription: null,
            annotationCategories: [],
            isCatLoaded: false,
            isTransLoaded: false,
            isVirtualLoaded: false,
        };
    }

    getInterTranscription() {
        axios.get(`${SERVER_URL}/api/services/frontend/inter-writer`, {
            params: {
                xmlId: this.state.fragmentId,
                urlId: this.state.interId,
            },
        }).then((result) => {
            this.setState({
                transcription: result.data,
                isLoaded: true,
            });
        });
    }

    getUsesInfo() {
        axios.get(`${SERVER_URL}/api/services/frontend/virtual-edition`, {
            params: {
                xmlId: this.state.fragmentId,
                urlId: this.state.interId,
            },
        }).then((result) => {
            this.setState({
                editionInfo: result.data,
                isVirtualLoaded: true,
            });
        });
    }

    componentDidMount() {
        this.getInterTranscription();
        this.getUsesInfo();
    }

    editorExtension(e) {
        function setAnnotationTags(field, annotation) {
            annotation.tags = parseTags(window.$('.tagSelector').val());
        }

        const tagsField = e.addField({
            load: loadField,
            submit: setAnnotationTags,
        });


        function loadField(field, annotation) {
            if (typeof annotation.id !== 'undefined') {
                axios.get(`${SERVER_URL}/fragments/fragment/annotation/${annotation.id}/categories`).then((res) => {
                    window.$('.tagSelector').val(res.data).trigger('change');
                });
            }
        }

        window.$('#annotator-field-1').remove('input');

        const select = window.$('<select>');
        select.attr('class', 'tagSelector');
        select.attr('style', 'width:263px;');
        window.$(tagsField).append(select);

        if (this.state.editionInfo.openVocabulary === 'true') {
            window.$('.tagSelector').select2({
                multiple: true,
                data: this.state.annotationCategories,
                tags: true,
                tokenSeparators: [',', '.'],
            });
        } else {
            window.$('.tagSelector').select2({
                multiple: true,
                data: this.state.annotationCategories,
            });
        }
        window.$('.tagSelector').on('select2:open', () => {
            window.$('.select2-dropdown').css({
                'z-index': '999999',
            });
        });
    }

    loadAnnotationCats(id) {
        axios.get(`${SERVER_URL}/api/services/frontend/inter/categories`, {
            params: {
                id,
            },
        }).then((res) => {
            console.log(res.data);
            this.setState({
                annotationCategories: res.data,
                isCatLoaded: true,
            });
        });
    }

    componentDidUpdate() {
        if (document.getElementById('content') !== null) {
            const id = this.state.editionInfo.externalId;

            const pageUri = function () {
                return {
                    beforeAnnotationCreated: function (ann) {
                        ann.uri = id;
                    },
                };
            };

            if (!this.state.isCatLoaded) {
                this.loadAnnotationCats(id);
            } else {
                const app = new annotator.App();
                app.include(annotator.ui.main, {
                    element: document.querySelector('#content'),
                    editorExtensions: [this.editorExtension],
                    viewerExtensions: [annotator.ui.tags.viewerExtension],
                });

                app.include(annotator.storage.http, {
                    prefix: `${SERVER_URL}/api/services/frontend/restricted`,
                    headers: { Authorization: `Bearer ${getToken()}` },
                });
                app.include(pageUri);
                app.include(annotator.identity.simple);
                app.start().then(() => {
                    app.ident.identity = this.props.info.username;
                    app.annotations.load({
                        uri: id,
                        limit: 0,
                        all_fields: 1,
                    });
                });
            }
        }
    }

    render() {
        if (!this.state.isLoaded || !this.state.isVirtualLoaded) {
            return (
                <div>Loading inter info</div>
            );
        }

        const transcription = ReactHTMLParser(this.state.transcription);

        return (
            <div className="col-md-9">
                <link
                    href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/css/select2.min.css"
                    rel="stylesheet" />
                <script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.1/js/select2.min.js" />
                <div
                    id="fragmentInter"
                    className="row"
                    style={{ marginLeft: 0, marginRight: 0 }}>
                    <h4>{this.state.editionInfo.editionTitle}
                         -
                        <FormattedMessage id={'general.uses'} />
                        {this.state.editionInfo.editionReference}({this.state.editionInfo.interReference})
                    </h4>

                    <div className="row" id="content">
                        <h4 className="text-center">
                            {this.state.title}
                        </h4>
                        <br />
                        <div id="transcriptionDiv" className="well" style={{ fontFamily: 'courier' }}>
                            {transcription}
                        </div>
                    </div>
                    <Taxonomy
                        fragmentId={this.state.fragmentId}
                        interId={this.state.interId}
                        externalId={this.state.editionInfo.externalId}
                        title={this.state.title}
                        annCall={this.loadAnnotationCats} />
                </div>
            </div>
        );
    }
}

export default connect(mapStateToProps)(InterVirtual);
