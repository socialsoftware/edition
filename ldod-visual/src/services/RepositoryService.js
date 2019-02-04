import axios from 'axios';

export class RepositoryService {
  constructor() {

    //this.axios.setHeader('accept-encoding', 'null');

    this.config = {
      headers: {
        'Authorization': "bearer" + localStorage.getItem('accessToken')
      }
    };

    this.bodyParameters = {
      key: "value"
    }

    this.axios = axios.create({
      baseURL: 'https://ldod.uc.pt',
      //baseURL: 'http://localhost:8080',
      timeout: 1000,
      headers: {
        'X-Custom-Header': 'BlendedWorkflow',
        'Authorization': 'Bearer' + localStorage.getItem('accessToken')
      }
    });

    this.state = {
      //acronym: "LdoD-Arquivo"
      //acronym: "LdoD-test"
      acronym: "LdoD-Twitter"
    };
  }

  // Specifications
  getFragments() {
    return this.axios.get("/virtualeditions/acronym/" + this.state.acronym + "/fragments");
  }

  getTranscriptions() {
    return this.axios.get("/virtualeditions/acronym/" + this.state.acronym + "/transcriptions");
  }

  getIntersByDistance(interId, heteronym, text, date, taxonomy) {
    return this.axios.post('/recommendation/' + interId + '/intersByDistance', {
      'heteronymWeight': heteronym,
      'textWeight': text,
      'dateWeight': date,
      'taxonomyWeight': taxonomy
    });
  }

  //http://localhost:8080/virtualeditions/acronym/LdoD-test/interId/281861523767368/tfidf
  getFragmentTfIdf(interId) {
    return this.axios.get("/virtualeditions/acronym/" + this.state.acronym + "/interId/" + interId + "/tfidf");
  }

}
