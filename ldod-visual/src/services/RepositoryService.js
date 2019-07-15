import axios from 'axios';

export class RepositoryService {
    constructor(editionAcronym) {

        //this.axios.setHeader('accept-encoding', 'null');

        this.setAcronym = editionAcronym;

        this.config = {
            headers: {
                'Authorization': "bearer" + localStorage.getItem('accessToken')
            }
        };

        this.bodyParameters = {
            key: "value"
        }

        this.axios = axios.create({
            //baseURL: 'https://ldod.uc.pt',
            baseURL: 'http://localhost:8080',
            timeout: 100000,
            headers: {
                'X-Custom-Header': 'BlendedWorkflow',
                'Authorization': 'Bearer' + localStorage.getItem('accessToken'),
                'Content-type': 'application/json;charset=utf-8',
                'responseEncoding': 'utf8'
            }
        });

        this.state = {
            //acronym: "LdoD-Arquivo"
            //acronym: "LdoD-test"
            //acronym: "LdoD-Twitter"
            //acronym: "LdoD-ReCritica" p testar word cloud com menos items
            acronym: "LdoD-Mallet"
        };
    }

    getPublicEditions() {
        return axios.get("/visual/editions/public/");
    }

    getFragments() {
        return this.axios.get("/visual/editions/acronym/" + this.setAcronym + "/fragments");
    }

    getFragmentTfIdf(interId) {
        return this.axios.get("/visual/editions/acronym/" + this.setAcronym + "/interId/" + interId + "/tfidf");
    }

    getIntersByDistance(interId, heteronym, text, date, taxonomy) {
        return this.axios.post('/recommendation/' + interId + '/intersByDistance', {
            'heteronymWeight': heteronym,
            'textWeight': text,
            'dateWeight': date,
            'taxonomyWeight': taxonomy
        });
    }

}
