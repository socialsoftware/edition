import axios from 'axios';

export class RepositoryService {
  constructor() {
    this.axios = axios.create({
      //baseURL: 'https://ldod.uc.pt',
      baseURL: 'http://localhost:8080',
      timeout: 1000,
      headers: {
        'X-Custom-Header': 'BlendedWorkflow'
      }
    });
    //this.axios.setHeader('accept-encoding', 'null');

    this.state = {
      //acronym: "LdoD-Arquivo",
      acronym: "LdoD-test"
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
